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

package me.batizhao.minio.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import me.batizhao.minio.exception.MinioException;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@Import(StorageAutoConfiguration.class)
public class MinioConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioConfiguration.class);

    @Bean
    @ConditionalOnClass(MinioClient.class)
    public MinioClient minioClient(StorageProperties storageProperties) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, ErrorResponseException, InvalidResponseException, MinioException, XmlParserException, ServerException {

        MinioClient minioClient;
        if (!configuredProxy()) {
            minioClient = MinioClient.builder()
                    .endpoint(storageProperties.getUrl())
                    .credentials(storageProperties.getAccessKey(), storageProperties.getSecretKey())
                    .build();
        } else {
            minioClient = MinioClient.builder()
                    .endpoint(storageProperties.getUrl())
                    .credentials(storageProperties.getAccessKey(), storageProperties.getSecretKey())
                    .httpClient(client())
                    .build();
        }
        minioClient.setTimeout(
                storageProperties.getConnectTimeout().toMillis(),
                storageProperties.getWriteTimeout().toMillis(),
                storageProperties.getReadTimeout().toMillis()
        );

        if (storageProperties.isCheckBucket()) {
            try {
                LOGGER.debug("Checking if bucket {} exists", storageProperties.getBucket());
                BucketExistsArgs existsArgs = BucketExistsArgs.builder()
                        .bucket(storageProperties.getBucket())
                        .build();
                boolean b = minioClient.bucketExists(existsArgs);
                if (!b) {
                    if (storageProperties.isCreateBucket()) {
                        try {
                            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                                    .bucket(storageProperties.getBucket())
                                    .build();
                            minioClient.makeBucket(makeBucketArgs);
                        } catch (Exception e) {
                            throw new MinioException("Cannot create bucket", e);
                        }
                    } else {
                        throw new IllegalStateException("Bucket does not exist: " + storageProperties.getBucket());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error while checking bucket", e);
                throw e;
            }
        }

        return minioClient;
    }

    private boolean configuredProxy() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");
        return httpHost != null && httpPort != null;
    }

    private OkHttpClient client() {
        String httpHost = System.getProperty("http.proxyHost");
        String httpPort = System.getProperty("http.proxyPort");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (httpHost != null)
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpHost, Integer.parseInt(httpPort))));
        return builder
                .build();
    }

}
