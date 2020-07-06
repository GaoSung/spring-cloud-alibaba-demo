package com.gaosung.scdemo.auth.properties;

import lombok.Data;

@Data
public class ClientProperties {

    private String providerId = "comm";

    private String clientId;

    private String clientSecret;

    private String grantType = "authorization_code";

    private String scope;

    private String authorizeUrl;

    private String accessTokenUrl;

    private String checkTokenUrl;

    private String refreshTokenUrl;

    private String redirectUrl;

    private String loginPage;

}
