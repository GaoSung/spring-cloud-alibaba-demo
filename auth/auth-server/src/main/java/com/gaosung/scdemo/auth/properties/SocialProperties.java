package com.gaosung.scdemo.auth.properties;

import lombok.Data;

@Data
public class SocialProperties {

    private String filterProcessesUrl = "/connect";

    private String defaultFailureUrl = "/connect/login";

}
