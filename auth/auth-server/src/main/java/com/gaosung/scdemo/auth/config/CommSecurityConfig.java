package com.gaosung.scdemo.auth.config;

import com.gaosung.scdemo.auth.captcha.CaptchaFilter;
import com.gaosung.scdemo.auth.constants.ProfileEnum;
import com.gaosung.scdemo.auth.handler.CommAuthenticationSuccessHandler;
import com.gaosung.scdemo.auth.handler.CommLogoutSuccessHandler;
import com.gaosung.scdemo.auth.properties.SecurityProperties;
import com.gaosung.scdemo.auth.smscode.SmsCodeAuthenticationSecurityConfig;
import com.gaosung.scdemo.auth.utils.SpringContextUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;

//@EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor
@EnableWebSecurity
@Order(1)
public class CommSecurityConfig extends WebSecurityConfigurerAdapter {

    private final @NonNull SecurityProperties securityProperties;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CommAuthenticationSuccessHandler commAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler commAuthenticationFailureHandler;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private CommLogoutSuccessHandler commLogoutSuccessHandler;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 让Security 忽略这些url，不做拦截处理
     *
     * @param
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 此处不可添加 /login， 否则授权页面报错
        // 主要配置某些静态资源不用安全控制
        web.ignoring().antMatchers
                ("/vendor/**",
                        "/images/**",
                        "/**/*.css",
                        "/**/*.js",
                        "/favicon.ico");
        if (!ProfileEnum.DEV.name().equalsIgnoreCase(SpringContextUtils.getActiveProfile())) {
            web.ignoring().antMatchers
                    (
                            "/swagger-ui.html/**", "/webjars/**",
                            "/swagger-resources/**", "/v2/api-docs/**",
                            "/swagger-resources/configuration/ui/**",
                            "/swagger-resources/configuration/security/**");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic().disable().csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionManagement()
                    .invalidSessionStrategy(invalidSessionStrategy)
                    .maximumSessions(securityProperties.getSession().getMaximumSessions())
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                .formLogin()
                    .loginPage(securityProperties.getLogin().getLoginPage())
                    .loginProcessingUrl(securityProperties.getLogin().getLoginProcessingUrl())
                    .successHandler(commAuthenticationSuccessHandler)
                    .failureHandler(commAuthenticationFailureHandler)
                .and()
                .rememberMe()
                    .tokenRepository(getPersistentTokenRepository())
                    .tokenValiditySeconds(3600) // Token过期时间为一个小时
                    .userDetailsService(userDetailsService)
                .and()
                .requestMatchers().antMatchers(
                    securityProperties.getLogin().getLoginPage(),
                    securityProperties.getLogin().getLoginProcessingUrl(),
                    securityProperties.getLogin().getLogOutPage(),
                    "/oauth/**","/captcha/image", "/authentication/mobile",
                    "/code/sms","/token_key", "/connect/**","/healthCheck/**")
                .and()
                .authorizeRequests()
                .antMatchers(
        securityProperties.getLogin().getLoginPage(),
                    securityProperties.getLogin().getLogOutPage(),
                    securityProperties.getLogin().getLoginProcessingUrl(),
                    "/oauth/**","/captcha/image", "/authentication/mobile",
                    "/code/sms","/token_key", "/connect/**","/healthCheck/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                    .logoutUrl(securityProperties.getLogin().getLogOutPage())
                    .logoutSuccessHandler(commLogoutSuccessHandler)
                    .deleteCookies("SESSIONID")
                    .permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    /**
     * 记住我的功能
     * @return
     */
    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        //启动时创建一张表，这个参数到第二次启动时必须注释掉，因为已经创建了一张表
        //jdbcTokenRepositoryImpl.setCreateTableOnStartup(true);
        return jdbcTokenRepositoryImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }
}
