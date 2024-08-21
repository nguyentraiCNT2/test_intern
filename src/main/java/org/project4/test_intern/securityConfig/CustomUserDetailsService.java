// CustomUserDetailsService.java
package org.project4.test_intern.securityConfig;

import org.project4.test_intern.context.RequestContext;
import org.project4.test_intern.entity.RoleEntity;
import org.project4.test_intern.entity.UserEntity;
import org.project4.test_intern.repository.RoleRepository;
import org.project4.test_intern.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetails> users = new ArrayList<>();
        List<UserEntity> userEntities = userRepository.findByUserName(username);
        if (userEntities.isEmpty()) {
            throw new UsernameNotFoundException("Không có tài khoản nào có tên: " + username);
        }
        RequestContext context = RequestContext.get();
        context.setUserId(userEntities.get(0).getId());
        RoleEntity roleEntity = roleRepository.findById(userEntities.get(0).getRoleId().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Không có quyền hạn này"));

        users.add(User.withUsername(userEntities.get(0).getUserName())
                .password(userEntities.get(0).getPassWord())
                .roles(roleEntity.getRoleName())
                .build());

        return users.get(0);
    }
}