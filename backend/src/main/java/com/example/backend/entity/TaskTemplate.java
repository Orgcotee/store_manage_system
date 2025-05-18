package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("task_template")
public class TaskTemplate {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String templateName;
    private String taskAttributes;
}    