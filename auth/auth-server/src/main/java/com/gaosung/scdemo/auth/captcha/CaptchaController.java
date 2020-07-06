package com.gaosung.scdemo.auth.captcha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RestController
public class CaptchaController {



    @Autowired
    private ImageCaptchaGenerate imageCaptchaGenerate;

    /**
     * 获取图片验证码
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/captcha/image")
    public  void createKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接口生成验证码
        ImageCaptchaVo imageCaptcha = imageCaptchaGenerate.generate();
        //2.保存到session中
        HttpSession session = request.getSession();
        session.setAttribute(ImageCaptchaGenerate.IMAGE_CAPTCHA_SESSION_KEY, imageCaptcha.getImageCode());
        //3.写到响应流中
        response.setHeader("Cache-Control", "no-store, no-cache");// 没有缓存
        response.setContentType("image/jpeg");
        try {
            ImageIO.write(imageCaptcha.getImage(), ImageCaptchaGenerate.FORMAT_NAME,response.getOutputStream());
        } catch (Exception e) {
            log.error("获取验证码错误:{}", e);
        }
    }

}
