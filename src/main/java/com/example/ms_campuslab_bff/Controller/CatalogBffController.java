package com.example.ms_campuslab_bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/catalog")
public class CatalogBffController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.catalog-url}")
    private String catalogUrl;

    @GetMapping("/resources")
    public ResponseEntity<Object> getResources() {
        String url = catalogUrl + "/api/catalog/resources";
        return restTemplate.getForEntity(url, Object.class);
    }

    @PostMapping("/resources")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> createResource(@RequestBody Object body) {
        String url = catalogUrl + "/api/catalog/resources";
        return restTemplate.postForEntity(url, body, Object.class);
    }
}