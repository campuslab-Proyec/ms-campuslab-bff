package com.example.ms_campuslab_bff.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * Utilidad para reenviar respuestas de los microservicios downstream sin propagar
 * cabeceras hop-by-hop. En particular, copiar "Transfer-Encoding: chunked" del downstream
 * provoca que Tomcat añada una segunda igual y nginx rechace la respuesta con 502
 * ("upstream sent duplicate header line: Transfer-Encoding: chunked").
 */
public final class ResponseRelay {

    private ResponseRelay() {
    }

    public static ResponseEntity<Object> clean(ResponseEntity<Object> upstream) {
        HttpHeaders headers = new HttpHeaders();
        upstream.getHeaders().forEach((name, values) -> {
            if (!name.equalsIgnoreCase(HttpHeaders.TRANSFER_ENCODING)
                    && !name.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)
                    && !name.equalsIgnoreCase(HttpHeaders.CONNECTION)) {
                headers.put(name, values);
            }
        });
        return new ResponseEntity<>(upstream.getBody(), headers, upstream.getStatusCode());
    }
}
