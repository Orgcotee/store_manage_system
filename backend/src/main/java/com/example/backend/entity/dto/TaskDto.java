package com.example.backend.entity.dto;

import lombok.Data;

import java.util.Date;
@Data
public class TaskDto {
    private Integer id;
    private String taskName;
    private Date startTime;
    private Date endTime;
    private Boolean finished;
    private Integer importance;
    private Integer userId;
    private Integer templateId;
    private Date createTime;
    private Integer pageSize;
    private Integer pageNum;
    private String content;
}
