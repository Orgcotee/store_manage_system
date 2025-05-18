package com.example.backend.entity.vo;

import lombok.Data;

@Data
public class UserRoleVo {
    private Integer id;
    private String username;
    private String avatar;
    private String phone;
    private String email;
    private Integer roleId;
    private String name;
}