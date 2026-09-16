package com.example.ms_campuslab_bff.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/bff/audit")
@CrossOrigin(origins = "*")
public class AuditBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.audit-url:http://localhost:8085}")
    private String auditUrl;

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) String actorId,
            HttpServletRequest request) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(auditUrl + "/api/audit/events");
        if (bookingId != null) builder.queryParam("bookingId", bookingId);
        if (actorId != null) builder.queryParam("actorId", actorId);

        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Object.class);
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
