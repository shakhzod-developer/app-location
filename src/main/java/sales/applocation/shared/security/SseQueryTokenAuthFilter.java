package sales.applocation.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SseQueryTokenAuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        boolean isSseAdminStream =
                servletPath != null &&
                servletPath.startsWith("/api/v1/admin/stream/");
        if (isSseAdminStream &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = request.getParameter("access_token");
            if (token == null || token.isBlank()) {
                token = request.getParameter("token");
            }
            if (token != null && !token.isBlank()) {
                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    String typ = jwt.getClaimAsString("typ");
                    if ("stream".equals(typ)) {

                        Authentication authentication =
                                jwtAuthenticationConverter.convert(jwt);
                        if (authentication instanceof AbstractAuthenticationToken authToken) {
                            authToken.setDetails(
                                    new WebAuthenticationDetailsSource()
                                            .buildDetails(request)
                            );
                        }

                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }

                } catch (JwtException ex) {
                    logger.warn("Invalid SSE stream token");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}