package com.gaosung.scdemo.auth.resource.config;

import com.gaosung.scdemo.auth.properties.SecurityProperties;
import com.gaosung.scdemo.auth.handler.CommOauth2AuthenticationEntryPoint;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableResourceServer
@Order(100)
public class CommResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final @NonNull SecurityProperties securityProperties;

    @Autowired
    private CommOauth2AuthenticationEntryPoint commOauth2AuthenticationEntryPoint;

//    @Autowired
//    private OAuth2AuthenticationManager oAuth2AuthenticationManager;

//    @Autowired
//    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId("comm_resource")
//                .tokenServices(tokenServices);
//        resources.authenticationManager(oAuth2AuthenticationManager);
        resources.authenticationEntryPoint(commOauth2AuthenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/api/**","/index")
                .and()
                .authorizeRequests().anyRequest().authenticated();

    }
}
