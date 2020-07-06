package com.gaosung.scdemo.common.model;

import com.gaosung.scdemo.common.constants.ResultEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 2421672425273204377L;
    private Integer code;
    private String msg;
    private T data;

    public Response() {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
    }

    public Response(T data) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response<ResultEnum> success() {
        return new Response(ResultEnum.SUCCESS);
    }

    public static <T> Response<T> success(T data) {
        return new Response(data);
    }

    public static <T> Response<T> success(int code, String msg, T data) {
        return new Response(code, msg, data);
    }

    public static <T> Response<T> failure() {
        return new Response(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMsg());
    }

    public static <T> Response<T> failure(int code, String msg) {
        return failure(code, msg, null);
    }

    public static <T> Response<T> failure(int code, String msg, T data) {
        return new Response(code, msg, (Object)null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response<?> response = (Response<?>) o;
        return Objects.equals(getCode(), response.getCode()) &&
                Objects.equals(getMsg(), response.getMsg()) &&
                Objects.equals(getData(), response.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMsg(), getData());
    }
}
