package me.batizhao.minio;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.minio.api.StorageService;
import me.batizhao.minio.config.MinioConfiguration;
import me.batizhao.minio.config.StorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author batizhao
 * @date 2021/10/29
 */
@ExtendWith(SpringExtension.class)
@Import({MinioConfiguration.class})
@EnableConfigurationProperties(value = StorageProperties.class)
@TestPropertySource(properties = {"pecado.storage.location=local",
        "pecado.storage.url=http://172.31.21.208:9000",
        "pecado.storage.bucket=stalber",
        "pecado.storage.access-key=minio",
        "pecado.storage.secret-key=minio123"})
@Slf4j
public class MinioServiceTest {

    @Autowired
    StorageService storageService;

    @Test
    void name() {
        log.info("MinIO bucketList : {}", storageService.list());
    }
}
