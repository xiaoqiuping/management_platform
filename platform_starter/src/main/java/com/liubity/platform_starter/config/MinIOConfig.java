package com.liubity.platform_starter.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Liubity
 * @Date 2020-11-28
 */
@Data
@Configuration
public class MinIOConfig {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Bean
    public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
        return new MinioClient(endpoint, accessKey, secretKey);
    }
}
