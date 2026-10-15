package com.example.ms_campuslab_bff.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Devuelve los datos y roles del usuario autenticado. Los App Roles (Admin, Docente, Estudiante)
 * están definidos en el registro de ms-campuslab-bff, por lo que solo vienen en el access token
 * que recibe el BFF y no en el ID token del frontend.
 */
@RestController
@RequestMapping("/api/bff/me")
public class MeController {

    @GetMapping
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return Map.of(
                "name", String.valueOf(jwt.getClaimAsString("name")),
                "username", String.valueOf(jwt.getClaimAsString("preferred_username")),
                "roles", roles != null ? roles : List.of()
        );
    }
}
