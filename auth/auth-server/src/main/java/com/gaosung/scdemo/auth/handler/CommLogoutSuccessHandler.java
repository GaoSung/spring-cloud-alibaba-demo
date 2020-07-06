package com.gaosung.scdemo.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaosung.scdemo.auth.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CommLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            log.info("[{}]退出成功", authentication.getName());
        }
//        if (LoginResponseType.JSON.equals(securityProperties.getLoginResponseType())) {
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(objectMapper.writeValueAsString(CommonResponse.success(ResultCode.LOGOUT_SUCCESS)));
//        } else {
            response.sendRedirect(request.getContextPath()+securityProperties.getLogin().getLoginPage());
//        }
    }
}
