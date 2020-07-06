package com.gaosung.scdemo.auth.smscode;

import com.gaosung.scdemo.auth.authorizationserver.granter.AbstractCustomTokenGranter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import java.util.Map;

/**
 * @Author: yugu
 * @CreateDate: 2019/9/6
 * @Description:
 */
public class SmsCodeTokenGranter extends AbstractCustomTokenGranter {

    protected SmsCodeUserDetailsService userDetailsService;

    public SmsCodeTokenGranter(SmsCodeUserDetailsService userDetailsService, AuthorizationServerTokenServices tokenServices,
                               ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "sms_code");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails getCustomUser(Map<String, String> parameters) {
        String mobile = parameters.get("mobile");
        String smscode = parameters.get("sms_code");
        return userDetailsService.loadUserByMobileAndSmscode(mobile, smscode);
    }

}
