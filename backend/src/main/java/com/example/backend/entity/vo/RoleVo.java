package com.example.backend.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleVo {
    private Integer id;

    private String name;

    private String description;

    private List<Integer> permissions;
}
