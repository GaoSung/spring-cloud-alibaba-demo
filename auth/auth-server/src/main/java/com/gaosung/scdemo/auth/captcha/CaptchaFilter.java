package com.gaosung.scdemo.auth.captcha;

import com.gaosung.scdemo.auth.exception.CaptchaException;
import com.gaosung.scdemo.auth.properties.SecurityProperties;
import com.gaosung.scdemo.auth.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //表单登录的post请求
        if (StringUtils.equals(SpringContextUtils.getContextPath()+securityProperties.getLogin().getLoginProcessingUrl(), request.getRequestURI())
                && StringUtils.equalsIgnoreCase("post", request.getMethod())) {
            try {
                validate(request);
            } catch (CaptchaException captchaException) {
                //失败调用我们的自定义失败处理器
                failureHandler.onAuthenticationFailure(request, response, captchaException);
                //后续流程终止
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 图片验证码校验
     *
     * @param request
     */
    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        // 拿到之前存储的imageCode信息
        HttpSession session = request.getSession();
        ImageCaptchaVo.ImageCode imageCodeInSession = (ImageCaptchaVo.ImageCode) session.getAttribute(ImageCaptchaGenerate.IMAGE_CAPTCHA_SESSION_KEY);
        String codeInRequest = ServletRequestUtils.getStringParameter(request, "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new CaptchaException("验证码的值不能为空");
        }
        if (imageCodeInSession == null) {
            throw new CaptchaException("验证码不存在");
        }
        if (imageCodeInSession.isExpried()) {
            session.removeAttribute(ImageCaptchaGenerate.IMAGE_CAPTCHA_SESSION_KEY);
            throw new CaptchaException("验证码已过期");
        }
        if (!StringUtils.equals(imageCodeInSession.getCode(), codeInRequest)) {
            throw new CaptchaException("验证码不匹配");
        }
        //验证通过 移除缓存
        session.removeAttribute(ImageCaptchaGenerate.IMAGE_CAPTCHA_SESSION_KEY);
    }
}
