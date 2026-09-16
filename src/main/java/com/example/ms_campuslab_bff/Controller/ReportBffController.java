package com.example.ms_campuslab_bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/reports")
@CrossOrigin(origins = "*")
public class ReportBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.report-url:http://ms-campuslab-report:8084}")
    private String reportUrl;

    @GetMapping
    public ResponseEntity<Object> getReports() {
        String url = reportUrl + "/api/reports";
        return restTemplate.getForEntity(url, Object.class);
    }

    @PostMapping
    public ResponseEntity<Object> generateReport(@RequestBody Object body) {
        String url = reportUrl + "/api/reports";
        return restTemplate.postForEntity(url, body, Object.class);
    }
}