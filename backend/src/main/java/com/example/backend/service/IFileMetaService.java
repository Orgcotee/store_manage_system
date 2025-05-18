package com.example.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.FileMeta;
import com.example.backend.entity.dto.FileMetaDto;
import com.example.backend.entity.vo.FileMetaVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileMetaService {
    Integer uploadFile(MultipartFile file, Integer accessPolicy, Integer preSignedExpiry);

    String getFileUrl(Integer ids);

    boolean deleteFile(Integer id);

    Page<FileMetaVo> list(FileMetaDto dto);

    void update(FileMeta fileMeta);

    List<String> getFileUrls(List<Integer> ids);
}
