package com.example.backend.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FileMetaDto {
    private Integer id;
    private String bucketName;
    private String fileName;
    private Date startTime;
    private Date endTime;
    private Integer pageNum;
    private Integer pageSize;
}
