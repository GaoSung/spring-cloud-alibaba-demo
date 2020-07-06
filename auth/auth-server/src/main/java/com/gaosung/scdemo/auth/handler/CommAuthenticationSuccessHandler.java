package com.gaosung.scdemo.auth.handler;

import com.gaosung.scdemo.auth.common.CommonResponse;
import com.gaosung.scdemo.auth.common.ResultCode;
import com.gaosung.scdemo.auth.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 浏览器环境下登录成功的处理器
 */
@Slf4j
@Component("commAuthenticationSuccessHandler")
public class CommAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.info("登录成功");

		if (HttpUtils.tryWriteToJson(request, response, CommonResponse.success(authentication))) {
			return;
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
