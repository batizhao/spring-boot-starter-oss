package me.batizhao.minio.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import me.batizhao.minio.api.LocalStorageService;
import me.batizhao.minio.api.MinioService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author batizhao
 * @date 2021/11/1
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "pecado.storage.location", havingValue = "local")
    public LocalStorageService localStorageService(StorageProperties properties) {
        return new LocalStorageService(properties);
    }

    @Bean
    @ConditionalOnProperty(name = "pecado.storage.location", havingValue = "minio")
    public MinioService minioService(StorageProperties properties) {
        return new MinioService(properties);
    }

}
