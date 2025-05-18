package com.example.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.R;
import com.example.backend.common.ServiceCode;
import com.example.backend.common.ServiceException;
import com.example.backend.entity.FileMeta;
import com.example.backend.entity.dto.FileMetaDto;
import com.example.backend.entity.vo.FileMetaVo;
import com.example.backend.service.IFileMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileMetaController {
    private final IFileMetaService fileMetaService;

    @PostMapping("/upload")
    public R<Integer> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("accessPolicy") Integer accessPolicy,
                                 @RequestParam(value = "preSignedExpiry", required = false) Integer preSignedExpiry) {
        Integer fileId = fileMetaService.uploadFile(file, accessPolicy, preSignedExpiry);
        return R.ok(fileId);
    }

    @GetMapping("/urls")
    public R<List<String>> getFileUrls(@RequestParam ("ids") List<Integer> ids ) {
        List<String> urls = fileMetaService.getFileUrls(ids);
        return R.ok(urls);
    }
    @GetMapping("/url")
    public R<String> getFileUrl(@RequestParam ("id") Integer id) {
        String url = fileMetaService.getFileUrl(id);
        return R.ok(url);
    }
    @DeleteMapping("/delete")
    public R<Void> deleteFile(@RequestParam ("id") Integer id) {
        if (fileMetaService.deleteFile(id))
            return R.ok();
        else {
            return R.fail(new ServiceException(ServiceCode.ERR_DELETE_FAILED,"删除失败"));
        }
    }
    @PostMapping("/list")
    public R<Page<FileMetaVo>> list(@RequestBody FileMetaDto dto) {
        Page<FileMetaVo> list = fileMetaService.list(dto);
        return R.ok(list);
    }
    @PostMapping("/update")
    public R<String> update(@RequestBody FileMeta fileMeta) {
         fileMetaService.update(fileMeta);
        return R.ok("修改成功");
    }
}
