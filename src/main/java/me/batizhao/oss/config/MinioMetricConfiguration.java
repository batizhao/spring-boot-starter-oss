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

package me.batizhao.oss.config;


import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.minio.MinioClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Aspect
@Configuration
@ConditionalOnClass({MinioClient.class, ManagementContextAutoConfiguration.class})
@ConditionalOnEnabledHealthIndicator("minio")
@ConditionalOnProperty(name = "pecado.storage.location", havingValue = "minio")
@AutoConfigureBefore(HealthContributorAutoConfiguration.class)
@AutoConfigureAfter(MinioAutoConfiguration.class)
public class MinioMetricConfiguration {

    private final MeterRegistry meterRegistry;
    private final StorageProperties storageProperties;

    private Timer listOkTimer;
    private Timer listKoTimer;
    private Timer getOkTimer;
    private Timer getKoTimer;
    private Timer putOkTimer;
    private Timer putKoTimer;
    private Timer removeOkTimer;
    private Timer removeKoTimer;
    private Timer listBucketOkTimer;
    private Timer listBucketKoTimer;

    @Autowired
    public MinioMetricConfiguration(MeterRegistry meterRegistry, StorageProperties storageProperties) {
        this.meterRegistry = meterRegistry;
        this.storageProperties = storageProperties;
    }

    @PostConstruct
    public void initTimers() {
        listOkTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "listObjects")
                .tag("status", "ok")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        listKoTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "listObjects")
                .tag("status", "ko")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        getOkTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "getObject")
                .tag("status", "ok")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        getKoTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "getObject")
                .tag("status", "ko")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        putOkTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "putObject")
                .tag("status", "ok")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        putKoTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "putObject")
                .tag("status", "ko")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        listBucketOkTimer = Timer
                .builder(storageProperties.getMetricName() + ".list.bucket")
                .tag("operation", "listBuckets")
                .tag("status", "ok")
                .register(meterRegistry);

        listBucketKoTimer = Timer
                .builder(storageProperties.getMetricName() + ".list.bucket")
                .tag("operation", "listBuckets")
                .tag("status", "ko")
                .register(meterRegistry);

        removeOkTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "removeObject")
                .tag("status", "ok")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);

        removeKoTimer = Timer
                .builder(storageProperties.getMetricName())
                .tag("operation", "removeObject")
                .tag("status", "ko")
                .tag("bucket", storageProperties.getBucket())
                .register(meterRegistry);
    }


    @ConditionalOnBean(MinioClient.class)
    @Around("execution(* io.minio.MinioClient.getObject(..))")
    public Object getMeter(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();

        try {
            Object proceed = pjp.proceed();
            getOkTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            return proceed;
        } catch (Exception e) {
            getKoTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            throw e;
        }
    }

    @ConditionalOnBean(MinioClient.class)
    @Around("execution(* io.minio.MinioClient.listObjects(..))")
    public Object listMeter(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();

        try {
            Object proceed = pjp.proceed();
            listOkTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            return proceed;
        } catch (Exception e) {
            listKoTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            throw e;
        }
    }

    @ConditionalOnBean(MinioClient.class)
    @Around("execution(* io.minio.MinioClient.putObject(..))")
    public Object putMeter(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();

        try {
            Object proceed = pjp.proceed();
            putOkTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            return proceed;
        } catch (Exception e) {
            putKoTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            throw e;
        }
    }

    @ConditionalOnBean(MinioClient.class)
    @Around("execution(* io.minio.MinioClient.listBuckets(..))")
    public Object listBucketMeter(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();

        try {
            Object proceed = pjp.proceed();
            listBucketOkTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            return proceed;
        } catch (Exception e) {
            listBucketKoTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            throw e;
        }
    }

    @ConditionalOnBean(MinioClient.class)
    @Around("execution(* io.minio.MinioClient.removeObject(..))")
    public Object removeMeter(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();

        try {
            Object proceed = pjp.proceed();
            removeOkTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            return proceed;
        } catch (Exception e) {
            removeKoTimer.record(System.currentTimeMillis() - l, TimeUnit.MILLISECONDS);
            throw e;
        }
    }
}
