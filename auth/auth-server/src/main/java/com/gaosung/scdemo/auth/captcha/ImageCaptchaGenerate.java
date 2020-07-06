package com.gaosung.scdemo.auth.captcha;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageCaptchaGenerate implements CaptchaGenerate {

    public static final String IMAGE_CAPTCHA_SESSION_KEY="image_captcha_session_key";

    public static final String FORMAT_NAME="JPEG";

    @Autowired
    private Producer producer;//config bean中配置

    @Override
    public ImageCaptchaVo generate() {
        String code = producer.createText();
        BufferedImage bufferedImage = producer.createImage(code);
        return new ImageCaptchaVo(bufferedImage, code);//5分钟过期
    }
}
