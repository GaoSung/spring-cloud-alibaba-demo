package com.gaosung.scdemo.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import com.gaosung.scdemo.common.constants.ResultEnum;
import com.gaosung.scdemo.common.model.Response;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

public class ServerHttpUtils {

    public static Mono<Void> writeErrorMessage(ServerHttpResponse response, ResultEnum errorEnum) {
        Response baseResponse = new Response();
        baseResponse.setCode(errorEnum.getCode());
        baseResponse.setMsg(errorEnum.getMsg());
        String result = JSONObject.toJSONString(baseResponse);
        DataBuffer buffer = response.bufferFactory().wrap(result.getBytes(Charset.forName("UTF-8")));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(buffer));
    }

}
