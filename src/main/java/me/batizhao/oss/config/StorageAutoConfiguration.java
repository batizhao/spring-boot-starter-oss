package me.batizhao.oss.config;

import lombok.RequiredArgsConstructor;
import me.batizhao.oss.api.DasService;
import me.batizhao.oss.api.MinioService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author batizhao
 * @date 2021/11/1
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(StorageProperties.class)
@Import(MinioAutoConfiguration.class)
public class StorageAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "pecado.storage.location", havingValue = "das")
    public DasService localStorageService(StorageProperties properties) {
        return new DasService(properties);
    }

    @Bean
    @ConditionalOnProperty(name = "pecado.storage.location", havingValue = "minio")
    public MinioService minioService(StorageProperties properties) {
        return new MinioService(properties);
    }

}
