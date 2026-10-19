package com.example.ms_campuslab_bff.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Interceptor para RestTemplate/RestClient: copia el header Authorization de la petición que
 * recibió el BFF en la llamada al microservicio, que valida el mismo JWT de Entra ID.
 */
public class BearerTokenForwarder implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs
                && !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            String authorization = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, authorization);
            }
        }
        return execution.execute(request, body);
    }
}
