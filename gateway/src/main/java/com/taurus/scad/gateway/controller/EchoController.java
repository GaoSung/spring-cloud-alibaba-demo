package com.taurus.scad.gateway.controller;

import com.taurus.scad.gateway.feign.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

    @Autowired
    private EchoService echoService;

    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        return string+":"+userName+":"+age;
    }

    @GetMapping(value = "/echo-rest/{str}")
    public String rest(@PathVariable String str) {
        return restTemplate.getForObject("http://service-provider/echo/" + str, String.class);
    }
    @GetMapping(value = "/echo-feign/{str}")
    public String feign(@PathVariable String str) {
        return echoService.echo(str);
    }

}
