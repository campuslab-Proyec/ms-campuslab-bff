package com.example.ms_campuslab_bff.Controller;

import com.example.ms_campuslab_bff.config.BearerTokenForwarder;
import com.example.ms_campuslab_bff.util.ResponseRelay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/catalog")
public class CatalogBffController {

    private final RestTemplate restTemplate = createRestTemplate();

    /** catalog valida el JWT: se le reenvía el token del usuario. */
    private static RestTemplate createRestTemplate() {
        RestTemplate rt = new RestTemplate();
        rt.getInterceptors().add(new BearerTokenForwarder());
        return rt;
    }

    @Value("${services.catalog-url:http://ms-campuslab-catalog:8082}")
    private String catalogUrl;

    @GetMapping("/resources")
    public ResponseEntity<Object> getResources() {
        String url = catalogUrl + "/api/catalog/resources";
        return ResponseRelay.clean(restTemplate.getForEntity(url, Object.class));
    }

    @PostMapping("/resources")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> createResource(@RequestBody Object body) {
        String url = catalogUrl + "/api/catalog/resources";
        return ResponseRelay.clean(restTemplate.postForEntity(url, body, Object.class));
    }

    @PutMapping("/resources/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> updateResource(@PathVariable Long id, @RequestBody Object body) {
        String url = catalogUrl + "/api/catalog/resources/" + id;
        return ResponseRelay.clean(restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body), Object.class));
    }

    @DeleteMapping("/resources/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> deleteResource(@PathVariable Long id) {
        String url = catalogUrl + "/api/catalog/resources/" + id;
        return ResponseRelay.clean(restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class));
    }

    @GetMapping("/labs")
    public ResponseEntity<Object> getLabs() {
        String url = catalogUrl + "/api/catalog/labs";
        return ResponseRelay.clean(restTemplate.getForEntity(url, Object.class));
    }

    @PostMapping("/labs")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> createLab(@RequestBody Object body) {
        String url = catalogUrl + "/api/catalog/labs";
        return ResponseRelay.clean(restTemplate.postForEntity(url, body, Object.class));
    }

    @PutMapping("/labs/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> updateLab(@PathVariable Long id, @RequestBody Object body) {
        String url = catalogUrl + "/api/catalog/labs/" + id;
        return ResponseRelay.clean(restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body), Object.class));
    }

    @DeleteMapping("/labs/{id}")
    @PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ResponseEntity<Object> deleteLab(@PathVariable Long id) {
        String url = catalogUrl + "/api/catalog/labs/" + id;
        return ResponseRelay.clean(restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class));
    }
}