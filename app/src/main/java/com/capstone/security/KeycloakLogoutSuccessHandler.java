package com.capstone.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Clears the Keycloak SSO session after local logout by redirecting the browser
 * to Keycloak's end-session endpoint with the OIDC ID token.
 */
@Component
public class KeycloakLogoutSuccessHandler implements LogoutSuccessHandler {

    private final String keycloakLogoutUri;

    public KeycloakLogoutSuccessHandler(
            @Value("${app.keycloak.logout-uri}") String keycloakLogoutUri) {
        this.keycloakLogoutUri = keycloakLogoutUri;
    }

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        String postLogoutRedirectUri = buildPostLogoutRedirectUri(request);

        if (authentication != null && authentication.getPrincipal() instanceof OidcUser oidcUser) {
            String logoutUrl = UriComponentsBuilder
                    .fromUriString(keycloakLogoutUri)
                    .queryParam("id_token_hint", oidcUser.getIdToken().getTokenValue())
                    .queryParam("post_logout_redirect_uri", postLogoutRedirectUri)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUriString();
            response.sendRedirect(logoutUrl);
            return;
        }

        response.sendRedirect(postLogoutRedirectUri);
    }

    private static String buildPostLogoutRedirectUri(HttpServletRequest request) {
        return UriComponentsBuilder
                .fromUriString(request.getRequestURL().toString())
                .replacePath(request.getContextPath() + "/login")
                .replaceQuery(null)
                .fragment(null)
                .build()
                .toUriString();
    }
}
