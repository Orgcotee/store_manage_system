package com.example.backend.entity.vo;


import com.example.backend.entity.Permission;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuVo {
    private Integer menuId;
    private String menuName;
    private String menuTitle;
    private Long pid;
    private Integer orderNum;
    private String url;
    private String icon;
    private String remark;
    private Boolean disabled;
    private Date createDate;
    private Date updateDate;
    private List<Permission> permissions;
    private List<MenuVo> children;
}