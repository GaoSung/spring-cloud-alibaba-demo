package com.gaosung.scdemo.auth.smscode;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SmsCode implements Serializable {

    private static final long serialVersionUID = 2542102392921182949L;

    /**
     * 短信验证码
     */
    private String code;

    private Long timeout; // 秒

    /**
     * 失效时间点
     */
    private LocalDateTime expireTime;

    public SmsCode(String code){
        this(code, 60 * 5L);
    }

    public SmsCode(String code, Long timeout){
        this.code = code;
        this.timeout = timeout;
        this.setExpireTime(LocalDateTime.now().plusSeconds(timeout));
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(this.getExpireTime());
    }
}
