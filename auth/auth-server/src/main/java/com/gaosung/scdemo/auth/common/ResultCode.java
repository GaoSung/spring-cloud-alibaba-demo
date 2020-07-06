package com.gaosung.scdemo.auth.common;

public enum ResultCode {

    SUCCESS(200, "成功"),
    SUCCESS_SAVE(101, "保存成功"),
    SUCCESS_DELETE(102, "删除成功"),
    SUCCESS_UPLOAD(103, "上传成功"),
    PARAM_MISSING(201, "参数缺失"),
    PARAM_ILLEGAL(202, "参数不合法"),
    UNAUTHORIZED(401, "用户未登录"),
    AUTHORIZATION_CONFIRM_ACCESS(402, "访问授权"),
    FORBIDDEN(403, "用户无权限"),
    LOGOUT_SUCCESS(405, "用户退出成功"),
    INVALID_TOKEN(406, "无效的凭证"),
    UNKNOW_ERROR(999, "未知异常");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(int code) {
        for (ResultCode resCode: ResultCode.values()) {
            if (resCode.code == code) {
                return resCode.msg;
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
