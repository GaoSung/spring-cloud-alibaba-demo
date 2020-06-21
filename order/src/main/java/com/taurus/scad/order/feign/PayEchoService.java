package com.taurus.scad.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by gaoxiang on 2020/6/18
 */
@FeignClient(name = "pay", path = "pay")
public interface PayEchoService {

    @GetMapping(value = "/echo/{str}")
    String echo(@PathVariable("str") String str);

}
