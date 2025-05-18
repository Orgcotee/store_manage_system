package com.example.backend.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Menu {
    @TableId("menu_id")
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
}