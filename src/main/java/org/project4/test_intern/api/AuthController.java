package org.project4.test_intern.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project4.test_intern.context.RequestContext;
import org.project4.test_intern.dto.RoleDTO;
import org.project4.test_intern.dto.UserDTO;
import org.project4.test_intern.securityConfig.CustomUserDetailsService;
import org.project4.test_intern.securityConfig.JwtTokenUtil;
import org.project4.test_intern.service.TokenService;
import org.project4.test_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    @Lazy
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TokenService tokenService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        try {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(2L);
            user.setRoleId(roleDTO);
            userService.register(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>>  login(@RequestParam String username, @RequestParam String password) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            boolean isMatch = BCrypt.checkpw(password, userDetails.getPassword());
            if (!isMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Mật khẩu không chính xác " ));
            }
            RequestContext context = RequestContext.get();
            String token = jwtUtil.generateToken(userDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed: bạn chưa đăng nhập!");
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                String  jwtToken = requestTokenHeader.substring(7);
                tokenService.saveToken(jwtToken, authentication.getName());
            }
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Logout failed: " + e.getMessage());
        }
    }
}
