package com.example.backend.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVo {
    private Integer id;
    private String username;
    private String password;
    private String avatar;
    private String phone;
    private String email;
    private String token;
    private List<MenuVo> routes;
    private List<String> authorize;
    private List<String> roles;
}
