package com.example.backend.entity.vo;

import lombok.Data;

import java.util.Date;
@Data
public class FileMetaVo {
    private Integer id;
    private String bucketName;
    private String objectName;
    private String fileName;
    //url
    private String url;
    private Long fileSize;
    private String contentType;
    private Date createTime;
    private String parentPath;
    private Boolean isDir;
    //策略 0 临时 1 直接访问
    private Integer accessPolicy;
    //生成临时链接过期事件
    private Integer preSignedExpiry;
}
