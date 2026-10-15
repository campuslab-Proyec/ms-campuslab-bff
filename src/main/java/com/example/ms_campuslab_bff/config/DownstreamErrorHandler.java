package com.example.ms_campuslab_bff.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

/**
 * RestTemplate lanza excepción ante respuestas 4xx/5xx de los microservicios. Sin este handler
 * el BFF respondería siempre 500; aquí se reenvía el status y el cuerpo originales al frontend.
 */
@RestControllerAdvice
public class DownstreamErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(DownstreamErrorHandler.class);

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<String> handleDownstreamError(HttpStatusCodeException ex) {
        HttpHeaders headers = new HttpHeaders();
        MediaType contentType = ex.getResponseHeaders() != null ? ex.getResponseHeaders().getContentType() : null;
        headers.setContentType(contentType != null ? contentType : MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(ex.getResponseBodyAsString(), headers, ex.getStatusCode());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnavailable(ResourceAccessException ex) {
        log.warn("Microservicio no disponible: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Servicio no disponible", "detail", String.valueOf(ex.getMessage())));
    }
}
