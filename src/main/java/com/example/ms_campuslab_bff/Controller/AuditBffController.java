package com.example.ms_campuslab_bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/audit")
@CrossOrigin(origins = "*")
public class AuditBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.audit-url:http://ms-campuslab-audit:8085}")
    private String auditUrl;

    @GetMapping
    public ResponseEntity<Object> getAuditLogs() {
        String url = auditUrl + "/api/audit";
        return restTemplate.getForEntity(url, Object.class);
    }
}