package com.example.backend.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Data
public class RoleDto{

    private Integer id;

    private String name;

    private String description;

    private String keyWord;

    private Integer pageSize;

    private Integer pageNum;

    private List<Integer> permissions;

}
