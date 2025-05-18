package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 角色与权限多对多关联表
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Data
@TableName("role_permission")
@ApiModel(value = "RolePermission对象", description = "角色与权限多对多关联表")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 权限ID
     */
    private Integer permissionId;
}
