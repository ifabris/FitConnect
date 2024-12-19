package hr.algebra.FitConnect.authentication.jwt;

import hr.algebra.FitConnect.authentication.configuration.SecurityConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (!isEndpointAllowingUnauthenticatedAccess(request)) {
            String jwtToken = extractJwtToken(request);

            log.trace("doFilter for endpoint: {} resolved jwt: {}", request.getRequestURI(), jwtToken);

            if (jwtToken != null
                    && !jwtToken.isEmpty()) { // only validate token if it exists in the request
                boolean authenticate = jwtService.authenticate(jwtToken);

                if (!authenticate) {
                    unauthorized(response);
                }
            } else { // no token, user is unauthorized
                unauthorized(response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            return bearerToken.substring(AUTHORIZATION_TOKEN_PREFIX.length());
        }
        return null;
    }

    private boolean isEndpointAllowingUnauthenticatedAccess(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return SecurityConfiguration.UNAUTHENTICATED_ENDPOINTS.stream()
                .anyMatch(endpoint -> uri.contains(endpointWithoutWildcard(endpoint)));
    }

    private String endpointWithoutWildcard(String endpoint) {
        return endpoint.replace("**", "");
    }
}
