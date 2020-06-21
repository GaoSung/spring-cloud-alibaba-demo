package com.taurus.scad.pay.controller;

import com.taurus.scad.pay.feign.OrderEchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gaoxiang on 2020/6/18
 */
@RefreshScope
@RestController
public class EchoController {

    @Value("${user.name:zhangsan}")
    private String userName;

    @Value("${user.age:10}")
    private int age;

    @Autowired
    private OrderEchoService orderEchoService;

    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        return "调用pay =====> "+string+":"+userName+":"+age;
    }

    @GetMapping(value = "/echo-feign/{str}")
    public String feign(@PathVariable String str) {
        return orderEchoService.echo(str);
    }

}
