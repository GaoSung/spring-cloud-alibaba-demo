package com.taurus.scad.pay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by gaoxiang on 2020/6/21
 */
@FeignClient(name = "order", path = "order")
public interface OrderEchoService {

    @GetMapping(value = "/echo/{str}")
    String echo(@PathVariable("str") String str);

}
