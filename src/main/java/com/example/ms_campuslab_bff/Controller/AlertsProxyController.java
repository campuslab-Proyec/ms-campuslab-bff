package com.example.ms_campuslab_bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

/**
 * Pasa al frontend el estado de las colas/DLQ de ms-campuslab-notify. Requiere
 * JWT como el resto del BFF (anyRequest().authenticated()).
 */
@RestController
@RequestMapping("/api/bff/alerts")
public class AlertsProxyController {

    private final RestClient rest;

    public AlertsProxyController(@Value("${services.notify-url}") String notifyUrl) {
        this.rest = RestClient.builder().baseUrl(notifyUrl).build();
    }

    @GetMapping(value = "/dlq", produces = MediaType.APPLICATION_JSON_VALUE)
    public String queues() {
        return rest.get().uri("/api/notify/dlq").retrieve().body(String.class);
    }

    @PostMapping(value = "/demo/logs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendLog(@RequestBody String body) {
        return rest.post().uri("/api/notify/demo/logs")
                .contentType(MediaType.APPLICATION_JSON).body(body).retrieve().body(String.class);
    }

    @PostMapping(value = "/demo/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendOrder(@RequestBody String body) {
        return rest.post().uri("/api/notify/demo/orders")
                .contentType(MediaType.APPLICATION_JSON).body(body).retrieve().body(String.class);
    }
}
