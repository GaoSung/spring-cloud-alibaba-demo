package com.gaosung.scdemo.auth.authorizationserver.config;

import com.gaosung.scdemo.auth.handler.CommWebResponseExceptionTranslator;
import com.gaosung.scdemo.auth.smscode.SmsCodeRefreshTokenGranter;
import com.gaosung.scdemo.auth.smscode.SmsCodeTokenGranter;
import com.gaosung.scdemo.auth.smscode.SmsCodeUserDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.*;

/**
 * 认证服务器配置
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final @NonNull AuthenticationManager authenticationManager;

    private final @NonNull DataSource dataSource;

    private final @NonNull CommRedirectResolver redirectResolver;

    private final @NonNull CommWebResponseExceptionTranslator commWebResponseExceptionTranslator;

    private final @NonNull UserDetailsService userDetailsService;

    private final @NonNull SmsCodeUserDetailsService smsCodeUserDetailsService;

    private final static String signingKey = "COMM_OAUTH2_SIGNINGKEY";

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // 通过token增强器，可以使用扩展token的内容和生成方式
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.userDetailsService(userDetailsService)
                 .tokenGranter(getTokenGranters(endpoints))
                 .tokenStore(tokenStore())
                 .tokenEnhancer(tokenEnhancerChain)
                 .authenticationManager(this.authenticationManager)
                 .redirectResolver(redirectResolver);

        endpoints.pathMapping("/oauth/confirm_access","/auth/confirm_access");

        endpoints.exceptionTranslator(commWebResponseExceptionTranslator);
    }

    /**
     * 令牌转换器，非/对称密钥加密
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 非对称加密
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("oauth2.jks"), signingKey.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2Key"));

        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap<>(1);
//            additionalInfo.put("username", authentication.getName());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

//    @Bean
//    public TokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }

    /**
     * token store 实现
     *
     * @return JwtTokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // 能够验证和解析 token
                .checkTokenAccess("isAuthenticated()")
                // 能够访问我们的公钥
                .tokenKeyAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Bean //通过读取key store的配置构造
    public KeyPair keyPair(AuthorizationServerProperties properties, ApplicationContext context){
        Resource keyStore = context
                .getResource(properties.getJwt().getKeyStore());
        char[] keyStorePassword = properties.getJwt().getKeyStorePassword()
                .toCharArray();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStore,
                keyStorePassword);
        String keyAlias = properties.getJwt().getKeyAlias();
        char[] keyPassword = Optional
                .ofNullable(properties.getJwt().getKeyPassword())
                .map(String::toCharArray).orElse(keyStorePassword);
        return keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword);
    }

    private TokenGranter getTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(endpoints.getTokenGranter());
        tokenGranters.add(new SmsCodeTokenGranter(smsCodeUserDetailsService, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        tokenGranters.add(new SmsCodeRefreshTokenGranter(tokenStore(), endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(tokenGranters);
    }

}
