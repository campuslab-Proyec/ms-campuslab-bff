package com.example.ms_campuslab_bff.Controller;

import com.example.ms_campuslab_bff.util.ResponseRelay;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@PreAuthorize("hasAuthority('APPROLE_Admin')")
@RequestMapping("/api/bff/report")
public class ReportBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.report-url:http://ms-campuslab-report:8084}")
    private String reportUrl;

    @GetMapping("/kpis")
    public ResponseEntity<Object> getKpis(
            @RequestParam(defaultValue = "last24h") String range,
            HttpServletRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(reportUrl + "/api/report/kpis")
                .queryParam("range", range)
                .encode().build().toUri();
        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return ResponseRelay.clean(restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class));
    }

    @GetMapping("/top-resources")
    public ResponseEntity<Object> getTopResources(
            @RequestParam(defaultValue = "last7d") String range,
            HttpServletRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(reportUrl + "/api/report/top-resources")
                .queryParam("range", range)
                .encode().build().toUri();
        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return ResponseRelay.clean(restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class));
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