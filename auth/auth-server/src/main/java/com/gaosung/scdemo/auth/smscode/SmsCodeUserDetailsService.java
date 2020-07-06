package com.gaosung.scdemo.auth.smscode;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author: yugu
 * @CreateDate: 2019/9/6
 * @Description: 用户细节服务实现
 */
@Service
public class SmsCodeUserDetailsService {

    public UserDetails loadUserByMobileAndSmscode(String mobile, String smscode) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(smscode)) {
            throw new InvalidGrantException("您输入的手机号或短信验证码不正确");
        }
        // 判断成功后返回用户细节
        return new User("匿名者",mobile,true,true,true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user,root"));
    }

}
