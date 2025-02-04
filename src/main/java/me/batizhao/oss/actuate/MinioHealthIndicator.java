/*
 * Copyright Jordan LEFEBURE © 2019.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.batizhao.oss.actuate;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import me.batizhao.oss.config.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Set the Minio health indicator on Actuator.
 *
 * @author Jordan LEFEBURE
 */
@ConditionalOnClass(ManagementContextAutoConfiguration.class)
@ConditionalOnProperty(name = "pecado.storage.location", havingValue = "minio")
@Component
public class MinioHealthIndicator implements HealthIndicator {

    private final MinioClient minioClient;
    private final StorageProperties storageProperties;

    @Autowired
    public MinioHealthIndicator(MinioClient minioClient, StorageProperties storageProperties) {
        this.minioClient = minioClient;
        this.storageProperties = storageProperties;
    }


    @Override
    public Health health() {
        if (minioClient == null) {
            return Health.down().build();
        }

        try {
            BucketExistsArgs args = BucketExistsArgs.builder()
                    .bucket(storageProperties.getBucket())
                    .build();
            if (minioClient.bucketExists(args)) {
                return Health.up()
                        .withDetail("bucketName", storageProperties.getBucket())
                        .build();
            } else {
                return Health.down()
                        .withDetail("bucketName", storageProperties.getBucket())
                        .build();
            }
        } catch (Exception e) {
            return Health.down(e)
                    .withDetail("bucketName", storageProperties.getBucket())
                    .build();
        }
    }
}
