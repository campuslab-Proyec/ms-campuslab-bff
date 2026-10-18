package com.example.ms_campuslab_bff.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Roles efectivos del usuario. Usa los App Roles del token (claim "roles") y, si no trae ninguno,
 * asigna Docente a los correos de los dominios configurados en campuslab.security.docente-domains.
 * Así no hace falta asignar el rol en Entra ID uno por uno (ni licencia P1 para usar grupos).
 *
 * Lo usan SecurityConfig (permisos de @PreAuthorize) y MeController (lo que muestra el frontend),
 * para que ambos vean siempre los mismos roles.
 */
@Component
public class RoleResolver {

    static final String DEFAULT_ROLE = "Docente";

    private final List<String> docenteDomains;

    public RoleResolver(@Value("${campuslab.security.docente-domains:}") String docenteDomains) {
        this.docenteDomains = Arrays.stream(docenteDomains.split(","))
                .map(d -> d.trim().toLowerCase(Locale.ROOT))
                .map(d -> d.startsWith("@") ? d.substring(1) : d)
                .filter(d -> !d.isEmpty())
                .toList();
    }

    public List<String> rolesFor(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null && !roles.isEmpty()) {
            return roles;
        }
        String domain = emailDomain(jwt);
        return domain != null && docenteDomains.contains(domain) ? List.of(DEFAULT_ROLE) : List.of();
    }

    /**
     * Dominio del correo del usuario. En Entra ID, preferred_username trae el correo real también
     * para invitados (el upn de un invitado tiene la forma usuario_dominio#EXT#@tenant).
     */
    private static String emailDomain(Jwt jwt) {
        return Stream.of("preferred_username", "email", "upn")
                .map(jwt::getClaimAsString)
                .filter(v -> v != null && v.contains("@") && !v.contains("#EXT#"))
                .findFirst()
                .map(v -> v.substring(v.lastIndexOf('@') + 1).toLowerCase(Locale.ROOT))
                .orElse(null);
    }
}
