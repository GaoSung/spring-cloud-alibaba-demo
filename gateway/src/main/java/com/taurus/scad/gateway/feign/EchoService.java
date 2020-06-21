package com.taurus.scad.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by gaoxiang on 2020/6/18
 */
@FeignClient(name = "service-provider")
public interface EchoService {

    @GetMapping(value = "/demo/echo/{str}")
    String echo(@PathVariable("str") String str);

}
