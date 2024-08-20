package org.project4.test_intern.api;

import org.project4.test_intern.dto.RoleDTO;
import org.project4.test_intern.dto.UserDTO;
import org.project4.test_intern.entity.RoleEntity;
import org.project4.test_intern.entity.UserEntity;
import org.project4.test_intern.repository.UserRepository;
import org.project4.test_intern.securityConfig.CustomUserDetailsService;
import org.project4.test_intern.securityConfig.JwtTokenUtil;
import org.project4.test_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        user.setRoleId(roleDTO);
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login( String username,
                                        String password) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            Boolean isMatch = BCrypt.checkpw(password, userDetails.getPassword());
            if (!isMatch)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: Mật khẩu không chính xác");
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }
}
