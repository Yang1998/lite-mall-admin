package com.yyt.axios.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;


@Component
@Slf4j
@PropertySource(value = "classpath:OSSConfig.yml")
public class OSSUtil {
    @Value("${com.yyt.oss.endpoint}")
    private String endpoint;
    @Value("${com.yyt.oss.accessKey}")
    private String accessKey;
    @Value("${com.yyt.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${com.yyt.oss.bucketName}")
    private String bucketName;
    @Value("${com.yyt.oss.picLocation}")
    private String picLocation;
    @Value("${com.yyt.oss.fileBaseURL}")
    private String fileBaseURL;
    private OSS ossClient;

    public OSSUtil() {}

    @PostConstruct
    public void init() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
        log.info("ossClient 创建完成");
    }

    @PreDestroy
    public void destory() {
        if(this.ossClient != null) {
            ossClient.shutdown();
            log.info("ossClient 已关闭");
        }
    }

    public String uploadFile(InputStream in, String fileName) {
        ossClient.putObject(bucketName, picLocation + fileName, in);
        return fileBaseURL + picLocation + fileName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPicLocation() {
        return picLocation;
    }

    public void setPicLocation(String picLocation) {
        this.picLocation = picLocation;
    }
}
