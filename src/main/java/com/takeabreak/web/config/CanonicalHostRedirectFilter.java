package com.takeabreak.web.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CanonicalHostRedirectFilter extends OncePerRequestFilter {

    @Value("${app.canonical-host:takeabreak.pt}")
    private String canonicalHost;

    @Value("${app.force-canonical-host:true}")
    private boolean forceCanonicalHost;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!forceCanonicalHost) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestHost = request.getServerName();
        if (requestHost == null || requestHost.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String normalizedRequestHost = requestHost.trim().toLowerCase();
        String normalizedCanonicalHost = canonicalHost.trim().toLowerCase();

        if (normalizedCanonicalHost.equals(normalizedRequestHost)) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("localhost".equals(normalizedRequestHost)
                || "127.0.0.1".equals(normalizedRequestHost)
                || "0:0:0:0:0:0:0:1".equals(normalizedRequestHost)
                || "::1".equals(normalizedRequestHost)) {
            String requestUri = request.getRequestURI() == null ? "/" : request.getRequestURI();
            String query = request.getQueryString();
            String target = "https://" + canonicalHost + requestUri + (query == null ? "" : "?" + query);
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", target);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
