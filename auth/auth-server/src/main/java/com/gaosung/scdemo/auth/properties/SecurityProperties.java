package com.gaosung.scdemo.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("comm.security.oauth")
public class SecurityProperties {

    private LoginProperties login = new LoginProperties();

    private ClientProperties client = new ClientProperties();

    private LoginResponseType loginResponseType = LoginResponseType.REDIRECT;

    private SessionProperties session = new SessionProperties();

    private SocialProperties social = new SocialProperties();

}
