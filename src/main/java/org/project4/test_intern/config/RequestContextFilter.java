package org.project4.test_intern.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project4.test_intern.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component("customRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        RequestContext requestContext = new RequestContext();
        requestContext.setRequestId(UUID.randomUUID().toString());
        requestContext.setTimestamp(Instant.now());

        // Lấy thông tin người dùng từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userIdStr = authentication.getName(); // Lấy user ID dưới dạng String
            try {
                Long userId = Long.parseLong(userIdStr); // Chuyển đổi thành Long
                requestContext.setUserId(userId);
            } catch (NumberFormatException e) {
                // Xử lý lỗi khi không thể chuyển đổi user ID thành Long
                System.err.println("Failed to parse user ID: " + userIdStr);
            }
        }

        RequestContext.set(requestContext);

        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }
}
