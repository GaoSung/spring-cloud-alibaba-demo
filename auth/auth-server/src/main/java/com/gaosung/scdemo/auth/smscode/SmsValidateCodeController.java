package com.gaosung.scdemo.auth.smscode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsValidateCodeController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/code/sms")
    public SmsCode createCode(@RequestHeader("deviceId") String deviceId, String mobile) {
        SmsCode smsCode = createSmsCode();
        System.out.println("验证码发送成功：" + smsCode);

        String key = "code:sms:"+ deviceId;
        stringRedisTemplate.opsForValue().set(key, smsCode.getCode());

        return smsCode;
    }

    /**
     * 模拟发短信过程
     * @return
     */
    private SmsCode createSmsCode() {
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        return new SmsCode(code, 30L);
    }

}
