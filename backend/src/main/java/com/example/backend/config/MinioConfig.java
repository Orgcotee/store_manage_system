package com.example.backend.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://47.108.159.100:9000/")
                .credentials("minioadmin", "minioadmin123")
                .build();
    }
}
