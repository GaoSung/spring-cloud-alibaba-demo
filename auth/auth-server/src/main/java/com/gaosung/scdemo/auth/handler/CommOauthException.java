package com.gaosung.scdemo.auth.handler;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gaosung.scdemo.auth.utils.CommOauthExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CommOauthExceptionSerializer.class)
public class CommOauthException extends OAuth2Exception {

    public CommOauthException(String msg) {
        super(msg);
    }

}
