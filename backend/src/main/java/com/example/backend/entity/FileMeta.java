package com.example.backend.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FileMeta {
    private Integer id;
    private String bucketName;
    private String objectName;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private Date createTime;
    private String parentPath;
    private Boolean isDir;
    private Integer accessPolicy;
    private Integer preSignedExpiry;
}
