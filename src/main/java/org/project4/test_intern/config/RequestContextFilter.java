package org.project4.test_intern.config;

import jakarta.servlet.*;
import org.project4.test_intern.context.RequestContext;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component("customRequestContextFilter") // Rename the bean here
public class RequestContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        RequestContext requestContext = new RequestContext();
        requestContext.setRequestId(UUID.randomUUID().toString());
        requestContext.setTimestamp(Instant.now());
        // Optionally set userId if authenticated
        // requestContext.setUserId(getAuthenticatedUserId());

        RequestContext.set(requestContext);

        try {
            chain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }

    @Override
    public void destroy() {
    }

    // Private method to get authenticated user ID (if available)
    // private Long getAuthenticatedUserId() {
    //     // Logic to retrieve user ID from the SecurityContext
    // }
}
