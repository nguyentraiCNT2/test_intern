package org.project4.test_intern.api;

import jakarta.servlet.http.HttpServletRequest;
import org.project4.test_intern.context.RequestContext;
import org.project4.test_intern.dto.UserDTO;
import org.project4.test_intern.securityConfig.JwtTokenUtil;
import org.project4.test_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMe(HttpServletRequest request) {
        try {
            // Lấy token từ header "Authorization"
            String authHeader = request.getHeader("Authorization");
            String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token không hợp lệ"));
            }

            // Xử lý token và lấy thông tin người dùng
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            boolean isValidToken = jwtTokenUtil.validateToken(token, userDetails);

            if (!isValidToken) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Phiên đăng nhập của bạn đã hết hạn"));
            }

            UserDTO userDTO = userService.info(username);
            userDTO.setPassWord(null);

            // Cập nhật RequestContext với thông tin
            RequestContext context = RequestContext.get();
            // Chuẩn bị phản hồi
            Map<String, Object> response = new HashMap<>();
            response.put("userDTO", userDTO);
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }
}
