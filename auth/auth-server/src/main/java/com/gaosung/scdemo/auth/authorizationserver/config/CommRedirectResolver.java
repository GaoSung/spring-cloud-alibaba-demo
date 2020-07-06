package com.gaosung.scdemo.auth.authorizationserver.config;

import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;
import java.util.List;

@Component
public class CommRedirectResolver extends DefaultRedirectResolver {

    @Override
    protected boolean redirectMatches(String requestedRedirect, String redirectUri) {
        UriComponents requestedRedirectUri = UriComponentsBuilder.fromUriString(requestedRedirect).build();
        UriComponents registeredRedirectUri = UriComponentsBuilder.fromUriString(redirectUri).build();
        boolean schemeMatch = this.isEqual(registeredRedirectUri.getScheme(), requestedRedirectUri.getScheme());
        boolean userInfoMatch = this.isEqual(registeredRedirectUri.getUserInfo(), requestedRedirectUri.getUserInfo());
        boolean hostMatch = this.hostMatches(registeredRedirectUri.getHost(), requestedRedirectUri.getHost());
        boolean portMatch = registeredRedirectUri.getPort() == requestedRedirectUri.getPort();
//        boolean pathMatch = this.isEqual(registeredRedirectUri.getPath(), StringUtils.cleanPath(requestedRedirectUri.getPath()));
//        boolean queryParamMatch = this.matchQueryParams(registeredRedirectUri.getQueryParams(), requestedRedirectUri.getQueryParams());
        return schemeMatch && userInfoMatch && hostMatch && portMatch;
    }

    private boolean isEqual(String str1, String str2) {
        if (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) {
            return true;
        } else {
            return !StringUtils.isEmpty(str1) ? str1.equals(str2) : false;
        }
    }

    private boolean matchQueryParams(MultiValueMap<String, String> registeredRedirectUriQueryParams, MultiValueMap<String, String> requestedRedirectUriQueryParams) {
        Iterator iter = registeredRedirectUriQueryParams.keySet().iterator();

        List registeredRedirectUriQueryParamsValues;
        List requestedRedirectUriQueryParamsValues;
        do {
            if (!iter.hasNext()) {
                return true;
            }

            String key = (String)iter.next();
            registeredRedirectUriQueryParamsValues = (List)registeredRedirectUriQueryParams.get(key);
            requestedRedirectUriQueryParamsValues = (List)requestedRedirectUriQueryParams.get(key);
        } while(registeredRedirectUriQueryParamsValues.equals(requestedRedirectUriQueryParamsValues));

        return false;
    }
}
