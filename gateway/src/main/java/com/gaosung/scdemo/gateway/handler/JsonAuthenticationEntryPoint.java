package com.gaosung.scdemo.gateway.handler;

import com.gaosung.scdemo.gateway.utils.ServerHttpUtils;
import com.gaosung.scdemo.common.constants.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        log.error("请先登录", e);
        ServerHttpResponse httpResponse = exchange.getResponse();
        if (e instanceof AuthenticationCredentialsNotFoundException) {
            return ServerHttpUtils.writeErrorMessage(httpResponse, ResultEnum.TOKEN_NOT_FOUND);
        } else if (e instanceof InvalidBearerTokenException) {
            return ServerHttpUtils.writeErrorMessage(httpResponse, ResultEnum.INVALID_TOKEN);
        }
        return ServerHttpUtils.writeErrorMessage(httpResponse, ResultEnum.UNAUTHORIZED);
    }

//    private Mono<Void> writeErrorMessage(ServerHttpResponse response, ResultEnum errorEnum) {
//        Response baseResponse = new Response();
//        baseResponse.setCode(errorEnum.getCode());
//        baseResponse.setMsg(errorEnum.getMsg());
//        String result = JSONObject.toJSONString(baseResponse);
//        DataBuffer buffer = response.bufferFactory().wrap(result.getBytes(Charset.forName("UTF-8")));
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        return response.writeWith(Mono.just(buffer));
//    }

}
