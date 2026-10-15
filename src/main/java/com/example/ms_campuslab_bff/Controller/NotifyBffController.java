package com.example.ms_campuslab_bff.Controller;

import com.example.ms_campuslab_bff.util.ResponseRelay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/notify")
public class NotifyBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.notify-url:http://ms-campuslab-notify:8083}")
    private String notifyUrl;

    @PostMapping
    public ResponseEntity<Object> sendNotification(@RequestBody Object body) {
        String url = notifyUrl + "/api/notify/test";
        return ResponseRelay.clean(restTemplate.postForEntity(url, body, Object.class));
    }
}