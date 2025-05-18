package com.example.backend.entity.dto;

import lombok.Data;

@Data
public class TaskTemplateDto {
    private Integer id;
    private String templateName;
    private String taskAttributes;
    private Integer pageSize;
    private Integer pageNum;
}
