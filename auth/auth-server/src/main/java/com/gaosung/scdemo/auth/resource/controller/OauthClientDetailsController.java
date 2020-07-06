package com.gaosung.scdemo.auth.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/client")
@RestController
public class OauthClientDetailsController {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @PostMapping("/add")
    public void client(@RequestBody ClientDetails clientDetails) {
        ((JdbcClientDetailsService)clientDetailsService).addClientDetails(clientDetails);
    }

    @DeleteMapping("/{clientId}")
    public void client(@PathVariable String clientId) {
        ((JdbcClientDetailsService)clientDetailsService).removeClientDetails(clientId);
    }

    @PostMapping("/update")
    public void update(@RequestBody ClientDetails clientDetails) {
        ((JdbcClientDetailsService)clientDetailsService).updateClientDetails(clientDetails);
    }

    @PostMapping("/list")
    public List<ClientDetails> queryAll() {
        return ((JdbcClientDetailsService)clientDetailsService).listClientDetails();
    }

    @GetMapping("/{clientId}")
    public ClientDetails queryAll(@PathVariable String clientId) {
        return ((JdbcClientDetailsService)clientDetailsService).loadClientByClientId(clientId);
    }

}
