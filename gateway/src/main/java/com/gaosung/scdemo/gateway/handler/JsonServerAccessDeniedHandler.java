package com.gaosung.scdemo.gateway.handler;

import com.gaosung.scdemo.gateway.utils.ServerHttpUtils;
import com.gaosung.scdemo.common.constants.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JsonServerAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("无权限", denied);
        ServerHttpResponse httpResponse = exchange.getResponse();
        return ServerHttpUtils.writeErrorMessage(httpResponse, ResultEnum.UNAUTHORIZED);
    }
}
