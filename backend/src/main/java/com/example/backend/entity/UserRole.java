package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 用户与角色多对多关联表
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Data
@TableName("user_role")
@ApiModel(value = "UserRole对象", description = "用户与角色多对多关联表")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("user_id")
    @ApiModelProperty("用户ID")
    private Integer userId;

    /**
     * 角色ID
     */
    private Integer roleId;
}
