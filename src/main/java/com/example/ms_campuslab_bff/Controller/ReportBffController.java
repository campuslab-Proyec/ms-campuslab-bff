package com.example.ms_campuslab_bff.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/report")
@CrossOrigin(origins = "*")
public class ReportBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.report-url:http://localhost:8084}")
    private String reportUrl;

    @GetMapping("/kpis")
    public ResponseEntity<Object> getKpis(
            @RequestParam(defaultValue = "last24h") String range,
            HttpServletRequest request) {
        String url = reportUrl + "/api/report/kpis?range=" + range;
        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }

    @GetMapping("/top-resources")
    public ResponseEntity<Object> getTopResources(
            @RequestParam(defaultValue = "last7d") String range,
            HttpServletRequest request) {
        String url = reportUrl + "/api/report/top-resources?range=" + range;
        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }

    private HttpHeaders forwardAuthHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            headers.set("Authorization", authHeader);
        }
        return headers;
    }
}
