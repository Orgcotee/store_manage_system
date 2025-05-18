package com.example.backend.entity.dto;


import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer pageSize;
    private Integer pageNum;
    private String roleId;
    //用户更新后的角色id
    private List<Integer> roleIds;
}
