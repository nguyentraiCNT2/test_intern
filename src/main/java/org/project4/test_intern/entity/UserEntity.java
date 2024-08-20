package org.project4.test_intern.entity;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(255)")
    private String userName;
    @Column(columnDefinition = "varchar(255)")
    private String passWord;
    @Column(columnDefinition = "varchar(255)")
    private String email;
    @ManyToOne
    @JoinColumn(name = "roleid")
    private RoleEntity roleId;
    @ManyToOne
    @JoinColumn(name = "profileid")
    private ProfileEntity profileid;

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

    public RoleEntity getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleEntity roleId) {
        this.roleId = roleId;
    }

    public ProfileEntity getProfileid() {
        return profileid;
    }

    public void setProfileid(ProfileEntity profileid) {
        this.profileid = profileid;
    }
}
