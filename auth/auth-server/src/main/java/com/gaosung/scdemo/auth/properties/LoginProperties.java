package com.gaosung.scdemo.auth.properties;

import lombok.Data;

@Data
public class LoginProperties {

    /**
     * 登录请求提交的路径，默认值 /login
     */
    private String loginProcessingUrl = SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM;

    private String loginSuccessUrl = SecurityConstants.DEFAULT_LOGIN_SUCCESS_URL;

    /**
     * 自定义登录地址，可以为静态页面,也可以为模板引擎action url
     */
//    private String loginPage = "/client/comm/login";
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    private String logOutPage = SecurityConstants.DEFAULT_LOGOUT_PAGE_URL;

}
