package com.gaosung.scdemo.auth.common;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(value = "通用响应")
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {
    private static final long serialVersionUID = -397209355784390835L;

    private boolean isSuccess;

    private int responseCode;

    private String responseMessage;

    private T result;

    public static <T> CommonResponse<T> success(T result){
        return new CommonResponse<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), result);
    }

    public static CommonResponse success(ResultCode resultCode){
        return new CommonResponse(true, resultCode.getCode(), resultCode.getMsg(), resultCode.getMsg());
    }

    public static <T> CommonResponse<T> success(ResultCode resultCode, T result){
        return new CommonResponse<>(true, resultCode.getCode(), resultCode.getMsg(), result);
    }

    public static <T> CommonResponse<T> success(){
        return new CommonResponse<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }


    public static <T> CommonResponse<T> fail(int resultCode){
        return new CommonResponse<>(false, resultCode, ResultCode.getMsg(resultCode),null);
    }

    public static <T> CommonResponse<T> fail(ResultCode resultCode){
        return new CommonResponse<>(false, resultCode.getCode(), resultCode.getMsg(),null);
    }
    
    public static <T> CommonResponse<T> fail(String errorMessage) {
        return new CommonResponse<T>(false, 999, errorMessage, null);
    }

    public static <T> CommonResponse<T> fail(int resultCode, String errorMessage){
        return new CommonResponse<>(false, resultCode, errorMessage,null);
    }
}
