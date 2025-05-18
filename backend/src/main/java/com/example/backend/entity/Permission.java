package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 系统权限表
 * </p>
 *
 * @author zz
 * @since 2025-04-10
 */
@Data
@ApiModel(value = "Permission对象", description = "系统权限表")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("权限代码（后端如user:add）")
    private String code;

    @ApiModelProperty("权限名称（如查看用户）")
    private String name;

    @ApiModelProperty("菜单界面id")
    private Integer menuId;
}
