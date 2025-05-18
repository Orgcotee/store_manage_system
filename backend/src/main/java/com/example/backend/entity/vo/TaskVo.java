package com.example.backend.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TaskVo {
    private Integer id;
    private String taskName;
    private Date startTime;
    private Date endTime;
    private Boolean finished;
    private Integer importance;
    private Date createTime;
    private Integer userId;
    private String username;
    private Integer templateId;
    private String content;
}
