package org.project4.test_intern.service;

import org.antlr.v4.runtime.Token;
import org.project4.test_intern.dto.TokenDTO;

import java.sql.Timestamp;
import java.util.List;

public interface TokenService {
    void  saveToken(String token, String username);
    void  logoutToken(Long id);
    void getByUserid(String Username);
    TokenDTO  getByid(Long id);
    Boolean validateToken(String token, String username);
    Boolean getByToken(String token);
}
