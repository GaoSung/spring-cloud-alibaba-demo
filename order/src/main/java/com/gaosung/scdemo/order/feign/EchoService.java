package com.gaosung.scdemo.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway-service")
public interface EchoService {

    @GetMapping("/gateway/echo/{str}")
    String echo(@PathVariable String str);

}
