package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.FileMeta;
import com.example.backend.entity.dto.FileMetaDto;
import com.example.backend.entity.vo.FileMetaVo;
import com.example.backend.mapper.FileMetaMapper;
import com.example.backend.service.IFileMetaService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileMetaService implements IFileMetaService {
    private final FileMetaMapper fileMetaMapper;
    private final MinioClient minioClient;

    // 定义文件类型到 bucket 的映射
    private static final Map<String, String> FILE_TYPE_BUCKET_MAP = new HashMap<>();
    static {
        // 图片类型映射到 image-bucket
        FILE_TYPE_BUCKET_MAP.put("image/jpeg", "image-bucket");
        FILE_TYPE_BUCKET_MAP.put("image/png", "image-bucket");
        FILE_TYPE_BUCKET_MAP.put("image/gif", "image-bucket");
        FILE_TYPE_BUCKET_MAP.put("image/bmp", "image-bucket");
        FILE_TYPE_BUCKET_MAP.put("image/tiff", "image-bucket");

        // 视频类型映射到 video-bucket
        FILE_TYPE_BUCKET_MAP.put("video/mp4", "video-bucket");
        FILE_TYPE_BUCKET_MAP.put("video/avi", "video-bucket");
        FILE_TYPE_BUCKET_MAP.put("video/mkv", "video-bucket");
        FILE_TYPE_BUCKET_MAP.put("video/quicktime", "video-bucket");
        FILE_TYPE_BUCKET_MAP.put("video/webm", "video-bucket");

        // 音频类型映射到 audio-bucket
        FILE_TYPE_BUCKET_MAP.put("audio/mpeg", "audio-bucket");
        FILE_TYPE_BUCKET_MAP.put("audio/wav", "audio-bucket");
        FILE_TYPE_BUCKET_MAP.put("audio/ogg", "audio-bucket");
        FILE_TYPE_BUCKET_MAP.put("audio/flac", "audio-bucket");
        FILE_TYPE_BUCKET_MAP.put("audio/aac", "audio-bucket");

        // 文档类型映射到 document-bucket
        FILE_TYPE_BUCKET_MAP.put("application/pdf", "document-bucket");
        FILE_TYPE_BUCKET_MAP.put("application/msword", "document-bucket");
        FILE_TYPE_BUCKET_MAP.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "document-bucket");
        FILE_TYPE_BUCKET_MAP.put("application/vnd.ms-excel", "document-bucket");
        FILE_TYPE_BUCKET_MAP.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "document-bucket");
        FILE_TYPE_BUCKET_MAP.put("application/vnd.ms-powerpoint", "document-bucket");
        FILE_TYPE_BUCKET_MAP.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "document-bucket");
    }
    @Override
    public Integer uploadFile(MultipartFile file, Integer accessPolicy, Integer preSignedExpiry) {
        if (Objects.isNull(file)) throw new ServiceException(ServiceCode.ERR_BAD_REQUEST, "文件不能为空");
        try {
            // 生成唯一的对象名，可根据实际需求修改命名规则
            String objectName = new Date().getTime() + "-" + file.getOriginalFilename();

            // 根据文件类型获取对应的 bucket 名
            String bucketName = FILE_TYPE_BUCKET_MAP.getOrDefault(file.getContentType(),"default-bucket");

            // 上传文件到 MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            // 保存文件元数据到数据库
            FileMeta fileMeta = new FileMeta();
            fileMeta.setBucketName(bucketName);
            fileMeta.setObjectName(objectName);
            fileMeta.setFileName(file.getOriginalFilename());
            fileMeta.setFileSize(file.getSize());
            fileMeta.setContentType(file.getContentType());
            fileMeta.setAccessPolicy(accessPolicy);
            fileMeta.setCreateTime(new Date());
            if (accessPolicy == 0 && preSignedExpiry != null) {
                fileMeta.setPreSignedExpiry(preSignedExpiry);
            }
            int id = fileMetaMapper.insert(fileMeta);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileUrl(Integer id) {
        if (Objects.isNull(id))
            return null;
        FileMeta fileMeta = fileMetaMapper.selectById(id);
        return generateUrl(fileMeta);
    }

    @Override
    public boolean deleteFile(Integer id) {
        if (Objects.isNull(id)) {
            return false;
        }
        try {
            // 从数据库中查询文件元数据
            FileMeta fileMeta = fileMetaMapper.selectById(id);
            if (fileMeta != null) {
                // 删除 MinIO 中的文件
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(fileMeta.getBucketName())
                        .object(fileMeta.getObjectName())
                        .build());
                // 删除数据库中的文件元数据
                fileMetaMapper.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Page<FileMetaVo> list(FileMetaDto dto) {
        // 解析分页参数
        int pageNum = dto.getPageNum() == null ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null ? 5 : dto.getPageSize();

        LambdaQueryWrapper<FileMeta> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(dto.getFileName() != null && !dto.getFileName().isEmpty(),FileMeta::getFileName, dto.getFileName());
        queryWrapper.eq(dto.getBucketName() != null && !dto.getBucketName().isEmpty(),FileMeta::getBucketName, dto.getBucketName());
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            queryWrapper.between(FileMeta::getCreateTime, dto.getStartTime(), dto.getEndTime());
        }
        List<FileMeta> metaList = fileMetaMapper.selectList(queryWrapper);
        if (metaList.isEmpty()){
            return null;
        }
        // 计算分页数据
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, metaList.size());
        List<FileMeta> pageRecords = (startIndex >= metaList.size()) ? Collections.emptyList() : metaList.subList(startIndex, endIndex);
        List<FileMetaVo> fileMetaVos = pageRecords.stream().map(fileMeta -> {
            FileMetaVo fileMetaVo = new FileMetaVo();
            BeanUtils.copyProperties(fileMeta, fileMetaVo);
            fileMetaVo.setUrl(generateUrl(fileMeta));
            return fileMetaVo;
        }).collect(Collectors.toList());

        // 手动创建 Page 对象
        Page<FileMetaVo> page = new Page<>(pageNum, pageSize);
        page.setRecords(fileMetaVos);
        page.setTotal(metaList.size());
        page.setPages((int) Math.ceil((double) metaList.size() / pageSize));
        page.addOrder(OrderItem.desc("create_time")); // 假设按创建时间降序排列

        return page;
    }

    @Override
    public void update(FileMeta fileMeta) {
        FileMeta file = fileMetaMapper.selectById(fileMeta.getId());
        if (file == null){
            throw new ServiceException(ServiceCode.ERR_BAD_REQUEST, "文件不存在");
        }
        fileMetaMapper.updateById(fileMeta);
    }

    @Override
    public List<String> getFileUrls(List<Integer> ids) {
        List<FileMeta> fileMetaList = fileMetaMapper.selectByIds(ids);
        List<String> fileUrls = new ArrayList<>();

        for (FileMeta fileMeta : fileMetaList) {
            String url = generateUrl(fileMeta);
            fileUrls.add(url);
        }
        return fileUrls;
    }

    private String generateUrl(FileMeta file){
        String url = null;
        String bucketName = file.getBucketName();
        String objectName = file.getObjectName();
        Integer accessPolicy = file.getAccessPolicy();
        Integer preSignedExpiry = file.getPreSignedExpiry();

        if (accessPolicy == 0) {
            // 生成预签名 URL
            try {
                url = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(objectName)
                                .expiry(preSignedExpiry==null ? 3600 : preSignedExpiry)
                                .build());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            // 构建公开访问链接
            url = "http://47.108.159.100:9000/" + bucketName + "/" + objectName;
        }
        return url;
    }
}
