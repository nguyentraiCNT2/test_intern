package org.project4.test_intern.service.impl;

import org.modelmapper.ModelMapper;
import org.project4.test_intern.dto.TokenDTO;
import org.project4.test_intern.entity.TokenEntity;
import org.project4.test_intern.entity.UserEntity;
import org.project4.test_intern.repository.TokenRepository;
import org.project4.test_intern.repository.UserRepository;
import org.project4.test_intern.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class TokenServiceIMPL implements TokenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void saveToken(String token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setCreated_at(new Timestamp(System.currentTimeMillis()));
        long oneHourInMillis = 1000 * 60 * 60; // 1 giờ = 60 phút = 3600 giây = 3600000 mili giây
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + oneHourInMillis);
        tokenEntity.setExpires_at(expiresAt);
        tokenEntity.setToken(token);
        tokenRepository.save(tokenEntity);
    }

    @Override
    public void logoutToken(Long id) {
            TokenEntity tokenEntity = tokenRepository.findById(id).orElseThrow(() -> new RuntimeException("khoong tim thay token nay"));
            tokenEntity.setExpires_at(new Timestamp(System.currentTimeMillis()));
            tokenRepository.save(tokenEntity);
    }


    @Override
    public TokenDTO getByid(Long id) {
        return null;
    }

    @Override
    public Boolean validateToken(String token) {
        List<TokenEntity> checktoken = tokenRepository.findByToken(token);
        for (TokenEntity item : checktoken) {
            if (item.getExpires_at().after(new Timestamp(System.currentTimeMillis()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean getByToken(String token) {
         Boolean tokenEntities = tokenRepository.existsByToken(token);

        return tokenEntities;
    }
}
