package com.example.backend.entity.vo;

import lombok.Data;

@Data
public class PermissionVo {
    private Integer id;

    /**
     * 权限代码（后端如user:add）
     */
    private String code;

    /**
     * 权限名称（如查看用户）
     */
    private String name;

    /**
     * ("菜单界面id")
     */
    private Integer menuId;
}
