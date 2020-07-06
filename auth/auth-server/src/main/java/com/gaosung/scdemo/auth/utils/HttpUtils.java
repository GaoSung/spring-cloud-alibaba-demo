package com.gaosung.scdemo.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaosung.scdemo.auth.common.CommonResponse;
import com.gaosung.scdemo.auth.properties.LoginResponseType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gaoxiang on 2020/7/6
 */
public class HttpUtils {

    public static boolean tryWriteToJson(HttpServletRequest request,
                                         HttpServletResponse response,
                                         int status,
                                         CommonResponse result) throws IOException {

        String redirectType = request.getParameter("redirect_type");
        if (LoginResponseType.JSON.name().equalsIgnoreCase(redirectType)) {
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(SpringContextUtils.getBean(ObjectMapper.class).writeValueAsString(result));
            return true;
        }
        return false;
    }

    public static boolean tryWriteToJson(HttpServletRequest request,
                                         HttpServletResponse response,
                                         CommonResponse result) throws IOException {
        return tryWriteToJson(request, response, HttpStatus.OK.value(), result);
    }

}
