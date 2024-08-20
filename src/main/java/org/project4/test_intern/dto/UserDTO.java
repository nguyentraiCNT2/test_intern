package org.project4.test_intern.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String userName;
    private String passWord;
    private String email;
    private RoleDTO roleId;
    private ProfileDTO profileid;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleDTO getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleDTO roleId) {
        this.roleId = roleId;
    }

    public ProfileDTO getProfileid() {
        return profileid;
    }

    public void setProfileid(ProfileDTO profileid) {
        this.profileid = profileid;
    }
}
