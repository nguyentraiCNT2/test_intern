package org.project4.test_intern.service.impl;

import org.modelmapper.ModelMapper;
import org.project4.test_intern.dto.UserDTO;
import org.project4.test_intern.entity.ProfileEntity;
import org.project4.test_intern.entity.UserEntity;
import org.project4.test_intern.repository.ProfileRepository;
import org.project4.test_intern.repository.RoleRepository;
import org.project4.test_intern.repository.UserRepository;
import org.project4.test_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceIMPL implements UserService {
    @Autowired
    private UserRepository userRepository;
@Autowired
private ModelMapper modelMapper;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void register(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO,UserEntity.class);
      Boolean checkemail = userRepository.existsByEmail(userDTO.getEmail());
        if (checkemail)
            throw new RuntimeException("Email này đã tồn tại.");
        if (userEntity.getPassWord().length() < 6)
            throw new RuntimeException("mật khẩu tối thiểu 6 ký tự");
        if (userEntity.getPassWord().length() > 25)
            throw new RuntimeException("mật khẩu tối đa 25 ký tự");
        ProfileEntity profile = new ProfileEntity();
        profile.setEmail(userEntity.getEmail());
        profile.setUserName(userEntity.getEmail());
        ProfileEntity profileEntity = profileRepository.save(profile);
        userEntity.setProfileid(profileEntity);
        String hashPassword =  BCrypt.hashpw(userEntity.getPassWord(), BCrypt.gensalt());
        userEntity.setPassWord(hashPassword);
        UserEntity user_save= userRepository.save(userEntity);
    }

    @Override
    public UserDTO info(String username) {
        List<UserEntity>userEntities = userRepository.findByUserName(username);
        UserDTO dto = modelMapper.map(userEntities.get(0), UserDTO.class);
        return dto;
    }

    public UserDTO login(String username, String password) {
       List<UserEntity>  userEntity = userRepository.findByUserName(username);
        if (userEntity != null && userEntity.get(0).getPassWord().equals(password)) {
            UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity>  userEntity = userRepository.findByUserName(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(userEntity.get(0).getUserName())
                .password(userEntity.get(0).getPassWord())
                .roles(userEntity.get(0).getRoleId().getRoleName())
                .build();
    }
}
