package com.example.ms_campuslab_bff.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class RoleResolverTest {

    private final RoleResolver resolver = new RoleResolver("duocuc.cl, @profesor.duoc.cl");

    private static Jwt jwt(Map<String, Object> claims) {
        Jwt.Builder builder = Jwt.withTokenValue("token").header("alg", "none")
                .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(60));
        claims.forEach(builder::claim);
        return builder.build();
    }

    @Test
    void usaLosRolesDelTokenCuandoVienen() {
        Jwt token = jwt(Map.of("roles", List.of("Admin"), "preferred_username", "ana@duocuc.cl"));
        assertThat(resolver.rolesFor(token)).containsExactly("Admin");
    }

    @Test
    void asignaDocenteSiNoHayRolYElCorreoEsDelDominio() {
        assertThat(resolver.rolesFor(jwt(Map.of("preferred_username", "Ana.Perez@DuocUC.cl")))).containsExactly("Docente");
        assertThat(resolver.rolesFor(jwt(Map.of("preferred_username", "juan@profesor.duoc.cl")))).containsExactly("Docente");
    }

    @Test
    void noAsignaNadaAOtrosDominios() {
        assertThat(resolver.rolesFor(jwt(Map.of("preferred_username", "ana@gmail.com")))).isEmpty();
        // Un subdominio no cuenta como el dominio configurado
        assertThat(resolver.rolesFor(jwt(Map.of("preferred_username", "ana@alumnos.duocuc.cl")))).isEmpty();
    }

    @Test
    void usaElCorreoRealDeUnInvitadoYNoSuUpnExterno() {
        Jwt guest = jwt(Map.of(
                "upn", "ana_duocuc.cl#EXT#@appclientm.onmicrosoft.com",
                "email", "ana@duocuc.cl"));
        assertThat(resolver.rolesFor(guest)).containsExactly("Docente");
    }

    @Test
    void desactivadoSiNoHayDominiosConfigurados() {
        RoleResolver disabled = new RoleResolver("");
        assertThat(disabled.rolesFor(jwt(Map.of("preferred_username", "ana@duocuc.cl")))).isEmpty();
    }
}
