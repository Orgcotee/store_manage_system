package com.example.finance.openFeign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
//文件上传/下载
@FeignClient(value ="backend",path = "/file")
public interface BackendClient {
    @PostMapping(value ="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Integer uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("accessPolicy") Integer accessPolicy,
                       @RequestParam(value = "preSignedExpiry", required = false) Integer preSignedExpiry);

    @GetMapping("/url")
    JSONObject getFileUrl(@RequestParam ("id") Integer id);

    @GetMapping("/urls")
    List<String> getFileUrls(@RequestParam ("ids") List<Integer> ids);

}
