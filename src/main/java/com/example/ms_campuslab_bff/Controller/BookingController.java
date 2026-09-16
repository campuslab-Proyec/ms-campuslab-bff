package com.example.ms_campuslab_bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bff/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.bookings-url:http://ms-campuslab-bookings:8081}")
    private String bookingsUrl;

    @GetMapping
    public ResponseEntity<Object> getBookings() {
        String url = bookingsUrl + "/api/bookings";
        return restTemplate.getForEntity(url, Object.class);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody Object body) {
        String url = bookingsUrl + "/api/bookings";
        return restTemplate.postForEntity(url, body, Object.class);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String id, @RequestBody Object body) {
        String url = bookingsUrl + "/api/bookings/" + id + "/status";
        restTemplate.put(url, body);
        return ResponseEntity.ok().build();
    }
}