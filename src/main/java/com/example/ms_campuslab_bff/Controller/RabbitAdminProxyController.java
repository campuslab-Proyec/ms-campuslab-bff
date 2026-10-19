package com.example.ms_campuslab_bff.Controller;

import java.net.URI;

import com.example.ms_campuslab_bff.config.BearerTokenForwarder;
import com.example.ms_campuslab_bff.util.ResponseRelay;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Reenvía /api/bff/admin/** a la API de administración de RabbitMQ de ms-campuslab-notify
 * (/api/admin/**): colas, exchanges, bindings y mensajes de prueba. Solo para el App Role Admin.
 * Los errores 4xx de notify (validación, 404, 409) llegan al frontend tal cual vía DownstreamErrorHandler.
 */
@RestController
@PreAuthorize("hasAuthority('APPROLE_Admin')")
@RequestMapping("/api/bff/admin")
public class RabbitAdminProxyController {

    private static final String BFF_PREFIX = "/api/bff";

    // Cliente HTTP del JDK: a diferencia del cliente por defecto, envía body también en DELETE (bindings)
    private final RestTemplate restTemplate = createRestTemplate();

    /** notify valida el JWT: se le reenvía el token del usuario. */
    private static RestTemplate createRestTemplate() {
        RestTemplate rt = new RestTemplate(new JdkClientHttpRequestFactory());
        rt.getInterceptors().add(new BearerTokenForwarder());
        return rt;
    }

    @Value("${services.notify-url:http://ms-campuslab-notify:8083}")
    private String notifyUrl;

    @RequestMapping("/**")
    public ResponseEntity<Object> proxy(HttpServletRequest request, @RequestBody(required = false) String body) {
        // /api/bff/admin/queues/x?ifEmpty=true -> {notify}/api/admin/queues/x?ifEmpty=true (path ya codificado)
        String path = request.getRequestURI().substring(BFF_PREFIX.length());
        String query = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        URI target = URI.create(notifyUrl + "/api" + path + query);

        HttpHeaders headers = new HttpHeaders();
        if (body != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        return ResponseRelay.clean(restTemplate.exchange(target, method, new HttpEntity<>(body, headers), Object.class));
    }
}
