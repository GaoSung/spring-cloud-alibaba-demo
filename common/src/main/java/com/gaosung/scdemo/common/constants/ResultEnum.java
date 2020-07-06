package com.gaosung.scdemo.common.constants;

import lombok.Getter;

public enum ResultEnum {

    SUCCESS(100, "操作成功."),
    FAILURE(201, "操作失败."),
    PARAM_ERROR(202, "参数错误."),
    UNKNOWN_ERROR(203, "未知错误."),
    DATABASE_ERROR(204, "数据库操作异常."),
    USER_NOT_EXISTS(205, "用户不存在"),
    USERNAME_PASSWORD_ERROR(206, "用户名或者密码错误"),
    USER_LOCKED(207, "用户已被锁定"),
    TOKEN_NOT_FOUND(208, "缺失凭证"),
    INVALID_TOKEN(209, "无效凭证"),
    UNAUTHORIZED(299, "未登录"),
    ILLEGAL(300, "非法操作."),
    SYSTEM_ERROR(9999, "系统错误");

    @Getter
    private int code;
    @Getter
    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
