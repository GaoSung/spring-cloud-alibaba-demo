package com.gaosung.scdemo.gateway.controller;

import com.gaosung.scdemo.gateway.feign.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class EchoController {

    @Value("${user.name:zhangsan}")
    String username;

    @Value("${user.age:15}")
    Integer userage;

    @Autowired
    private EchoService echoService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/echo/{str}")
    public String echo(@PathVariable String str) {
        return str + ",i am " + username + "," + "i am " + userage;
    }

    @GetMapping("/echo-rest/{str}")
    public String restEcho(@PathVariable String str) {
        restTemplate.getForObject("http://gateway-service/gateway/echo/" + str, String.class);
        return str + ",i am " + username + "," + "i am " + userage;
    }

    @GetMapping("/echo-feign/{str}")
    public String feignEcho(@PathVariable String str) {
        return echoService.echo(str);
    }

}
