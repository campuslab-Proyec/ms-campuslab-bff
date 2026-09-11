package com.example.ms_campuslab_bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    public record BookingDto(String id, String labName, String status) {}

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookings() {
        List<BookingDto> list = List.of(
            new BookingDto("BKG-001", "Laboratorio Redes & Cloud", "SOLICITADA"),
            new BookingDto("BKG-002", "Laboratorio Electrónica", "APROBADA")
        );
        return ResponseEntity.ok(list);
    }
}