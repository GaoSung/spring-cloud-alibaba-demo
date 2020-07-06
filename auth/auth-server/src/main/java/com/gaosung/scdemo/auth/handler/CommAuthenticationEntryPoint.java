package com.gaosung.scdemo.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaosung.scdemo.auth.common.ResultCode;
import com.gaosung.scdemo.auth.properties.SecurityProperties;
import com.gaosung.scdemo.auth.common.CommonResponse;
import com.gaosung.scdemo.auth.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Primary
@Component
public class CommAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("未登录");

//        if (LoginResponseType.JSON.equals(securityProperties.getLoginResponseType())) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(objectMapper.writeValueAsString(CommonResponse.fail(HttpStatus.UNAUTHORIZED.value(), "请先登录")));
//            return;
//        }

        if (HttpUtils.tryWriteToJson(request, response, HttpStatus.UNAUTHORIZED.value(), CommonResponse.fail(ResultCode.UNAUTHORIZED))) {
            return;
        }

        if (isAbsoluteUrl(securityProperties.getLogin().getLoginPage())) {
            response.sendRedirect(securityProperties.getLogin().getLoginPage());
        } else {
            response.sendRedirect(request.getContextPath() + securityProperties.getLogin().getLoginPage());
        }
    }

    public static boolean isAbsoluteUrl(String url) {
        if (url == null) {
            return false;
        }
        final Pattern ABSOLUTE_URL = Pattern.compile("\\A[a-z0-9.+-]+://.*",
                Pattern.CASE_INSENSITIVE);

        return ABSOLUTE_URL.matcher(url).matches();
    }
}
