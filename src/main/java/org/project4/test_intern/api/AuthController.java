package org.project4.test_intern.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project4.test_intern.context.RequestContext;
import org.project4.test_intern.dto.RoleDTO;
import org.project4.test_intern.dto.UserDTO;
import org.project4.test_intern.securityConfig.CustomUserDetailsService;
import org.project4.test_intern.securityConfig.JwtTokenUtil;
import org.project4.test_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        try {
            // Gán vai trò mặc định cho người dùng
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(2L); // Giả sử 2L là ID của vai trò mặc định
            user.setRoleId(roleDTO);

            // Đăng ký người dùng
            userService.register(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>>  login(@RequestParam String username,
                                                      @RequestParam String password) {
        try {
            // Load thông tin người dùng từ database
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Kiểm tra mật khẩu
            boolean isMatch = BCrypt.checkpw(password, userDetails.getPassword());
            if (!isMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Mật khẩu không chính xác " ));
            }
            // Cập nhật RequestContext với thông tin
            RequestContext context = RequestContext.get();
            // Sinh token JWT
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
            // Xóa thông tin người dùng khỏi SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                // Vô hiệu hóa JWT Token bằng cách thêm vào blacklist
                String token = request.getHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    jwtTokenUtil.invalidateToken(token);  // Hàm này bạn sẽ phải tự triển khai để thêm token vào blacklist
                }

                // Xóa dữ liệu người dùng khỏi SecurityContext
                SecurityContextHolder.getContext().setAuthentication(null);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Logout failed: bạn chưa đăng nhập!");
            }

            // Xóa cookie liên quan đến JWT (nếu có)
            Cookie cookie = new Cookie("token", null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);  // Làm cho cookie hết hạn ngay lập tức
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Logout failed: " + e.getMessage());
        }
    }


}
