package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("task")
public class Task {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String taskName;
    private Date startTime;
    private Date endTime;
    private Boolean finished;
    private Integer importance;
    private Date createTime;
    private Integer userId;
    private Integer templateId;
    private String content;
}    