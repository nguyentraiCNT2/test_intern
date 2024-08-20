package org.project4.test_intern.service;

import org.project4.test_intern.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    void  register (UserDTO dto);
    UserDTO info(String username);
    UserDTO login (String username, String password);
   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
