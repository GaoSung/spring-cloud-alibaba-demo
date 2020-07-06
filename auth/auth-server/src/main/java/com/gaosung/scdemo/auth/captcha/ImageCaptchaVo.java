package com.gaosung.scdemo.auth.captcha;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ImageCaptchaVo implements Serializable {

    private static final long serialVersionUID = -7473671158730133618L;

    /**
     * 图片验证码
     */
    private transient BufferedImage image;

    private ImageCode imageCode = new ImageCode();

    private Long timeout;

    public ImageCaptchaVo(BufferedImage image, String code){
        this(image, code, 60 * 5L);
    }

    public ImageCaptchaVo(BufferedImage image, String code, Long timeout){
        this.image = image;
        this.imageCode.setCode(code);
        this.timeout = timeout;
        this.imageCode.setExpireTime(LocalDateTime.now().plusSeconds(timeout));
    }

    @Data
    public class ImageCode implements Serializable {
        private static final long serialVersionUID = 4382267665214839077L;
        /**
         *  验证码
         */
        private String code;
        /**
         * 失效时间 这个通常放在缓存中或维护在数据库中
         */
        private LocalDateTime expireTime;

        /**
         * 是否失效
         * @return
         */
        public boolean isExpried() {
            return LocalDateTime.now().isAfter(this.getExpireTime());
        }
    }
}
