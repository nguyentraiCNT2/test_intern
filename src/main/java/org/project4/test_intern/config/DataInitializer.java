package org.project4.test_intern.config;

import jakarta.annotation.PostConstruct;
import org.project4.test_intern.entity.RoleEntity;
import org.project4.test_intern.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;
    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);

            RoleEntity userRole = new RoleEntity();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
        }

    }
}
