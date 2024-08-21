package org.project4.test_intern.dto;

import org.project4.test_intern.entity.UserEntity;

import java.sql.Timestamp;

public class TokenDTO {
    private Long id;
    private String token;
    private Timestamp created_at;
    private Timestamp expires_at;
    private UserEntity userid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Timestamp expires_at) {
        this.expires_at = expires_at;
    }

    public UserEntity getUserid() {
        return userid;
    }

    public void setUser(UserEntity user) {
        this.userid = user;
    }
}
