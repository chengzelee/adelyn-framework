package cn.adelyn.framework.oss.config;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.NotificationRecords;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author chengze
 * @date 2022/8/25
 * @desc 注入MinioTemplate
 */
@Component
public class MinioTemplate implements InitializingBean {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.access}")
    private String access;
    @Value("${minio.secret}")
    private String secret;
    @Value("${minio.presignedUrl.insideDomain}")
    private String insideDomain;
    @Value("${minio.presignedUrl.outsideDomain}")
    private String outsideDomain;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() {
        this.minioClient =  MinioClient.builder()
                .endpoint(url)
                .credentials(access, secret)
                .build();
    }

    public void add(InputStream is, String bucket, String filePath, String contentType) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .contentType(contentType)
                .stream(is, is.available(), -1)
                .build());
    }

    public InputStream get(String bucket, String filePath) throws Exception {
        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .build());
        return response;
    }

    public void delete(String bucket, String filePath) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(filePath)
                .build());
    }

    public void update(String bucket, String filePath) throws Exception {

    }

    public String genDownloadUrl(String bucket, String filePath, int duration, TimeUnit timeUnit) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(filePath)
                        .expiry(duration, timeUnit)
                        .method(Method.GET)
                        .build()
        ).replace(insideDomain, outsideDomain);
    }

    public String genUploadUrl(String bucket, String filePath, int duration, TimeUnit timeUnit) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(filePath)
                        .expiry(duration, timeUnit)
                        .method(Method.PUT)
                        .build()
        ).replace(insideDomain, outsideDomain);
    }

    public CloseableIterator<Result<NotificationRecords>> listenBucketNotification(ListenBucketNotificationArgs args) {
        try {
            return minioClient.listenBucketNotification(args);
        } catch (Exception e) {

        }
        return null;
    }
}
