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
@RequestMapping("/api/bff/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.bookings-url:http://localhost:8081}")
    private String bookingsUrl;

    @GetMapping
    public ResponseEntity<Object> getBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            HttpServletRequest request) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(bookingsUrl + "/api/bookings");        if (status != null) builder.queryParam("status", status);
        if (from != null) builder.queryParam("from", from);
        if (to != null) builder.queryParam("to", to);

        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Object.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id, HttpServletRequest request) {
        String url = bookingsUrl + "/api/bookings/" + id;
        HttpEntity<Void> entity = new HttpEntity<>(forwardAuthHeader(request));
        return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody Object body, HttpServletRequest request) {
        String url = bookingsUrl + "/api/bookings";
        HttpEntity<Object> entity = new HttpEntity<>(body, forwardAuthHeader(request));
        return restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateStatus(@PathVariable Long id, @RequestBody Object body, HttpServletRequest request) {
        String url = bookingsUrl + "/api/bookings/" + id + "/status";
        HttpEntity<Object> entity = new HttpEntity<>(body, forwardAuthHeader(request));
        return restTemplate.exchange(url, HttpMethod.PUT, entity, Object.class);
    }

    /** Reenvía el header Authorization que llegó del frontend hacia el microservicio de dominio. */
    private HttpHeaders forwardAuthHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            headers.set("Authorization", authHeader);
        }
        return headers;
    }
}