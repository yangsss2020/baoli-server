package com.baoli.manage.upload.service;

import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.exception.BaoliException;
import com.baoli.common.util.ExceptionUtil;
import com.baoli.manage.config.QiniuProperties;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author ys
 * @create 2020-01-09-19:43
 */
@Service
@Slf4j
@EnableConfigurationProperties(QiniuProperties.class)
public class UploadService {
    @Autowired
    private QiniuProperties qiniuProperties;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp", "image/gif");

    public String uploadImg(MultipartFile file) {
        String accessKey = this.qiniuProperties.getAccessKey();
        String secretKey = this.qiniuProperties.getSecretKey();
        String bucket = this.qiniuProperties.getBucket();
        String domain = this.qiniuProperties.getDomain();
        String name = file.getOriginalFilename();
        if (!CONTENT_TYPES.contains(file.getContentType())) {
            throw new BaoliException("文件类型不合法", ResultCodeEnum.FILE_UPLOAD_ERROR.getCode());
        }
        long size = file.getSize();
        if (size > 1024 * 1024 * 4) {
            throw new BaoliException("文件大小超出限制", ResultCodeEnum.FILE_UPLOAD_ERROR.getCode());
        }
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传

        String url = null;
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String ext = StringUtils.substringAfterLast(name, ".");
        String key = UUID.randomUUID().toString().replace("-", "_") + "." + ext;
        byte[] bytes = null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error(ExceptionUtil.getMessage(e));
        }
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            url = domain + putRet.key;
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            log.error(ExceptionUtil.getMessage(ex));
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return url;
    }

    public void deleteImg(String key) {
        String subKey = key.substring(7);
        String accessKey = this.qiniuProperties.getAccessKey();
        String secretKey = this.qiniuProperties.getSecretKey();
        String bucket = this.qiniuProperties.getBucket();
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, subKey);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }

    }
}
