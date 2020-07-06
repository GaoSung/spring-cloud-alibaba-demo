package com.gaosung.scdemo.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private static final String TOKEN = "token";

    // url匹配器
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int getOrder() {
        return -1000;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Filter Token...");
//        String accessToken = extractToken(exchange.getRequest());
//
//        if(pathMatcher.match("/**/v2/api-docs/**",exchange.getRequest().getPath().value())){
//            return chain.filter(exchange);
//        }
//
//        if (pathMatcher.match("/auth/**", exchange.getRequest().getPath().value())) {
//            return chain.filter(exchange);
//        }
//
//        if (accessToken == null) {
//            ServerHttpResponse httpResponse = exchange.getResponse();
//            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
//            httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
//            String body = JSONObject.toJSONString(Response.success("请先登录"));
//            DataBuffer buffer = httpResponse.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
//            return httpResponse.writeWith(Mono.just(buffer));
//        } else {
//            try {
//                Map<String, Object> params = (Map<String, Object>) redisTemplate.opsForValue().get("token:" + accessToken);
//                if (params.isEmpty()) {
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                }
//            } catch (Exception e) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//        }

        return chain.filter(exchange);
    }

    protected String extractToken(ServerHttpRequest request) {
        List<String> strings = request.getHeaders().get("Authorization");
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0).substring("Bearer".length()).trim();
        }

        if (StringUtils.isEmpty(authToken)) {
            strings = request.getQueryParams().get("access_token");
            if (strings != null) {
                authToken = strings.get(0);
            }
        }

        return authToken;
    }
}
