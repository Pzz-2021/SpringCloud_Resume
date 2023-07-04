package com.resume.parse.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.resume.parse.properties.QiNiuProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 *@filename: UploadService
 *@author: lyh
 *@date:2023/7/4 17:02
 *@version 1.0
 *@description TODO
 */
@Component
@Slf4j
public class UploadUtil {
    @Autowired
    private QiNiuProperties qiNiuProperties;

    public String uploadByBytes(byte[] bytes,String fileName){
        //构造一个带指定 Region 对象的配置类
        //华南
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = qiNiuProperties.getAccessKey();
        String secretKey = qiNiuProperties.getAccessSecret();
        String bucket = qiNiuProperties.getBucketName();
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return  qiNiuProperties.getHostName()+putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
                log.error(ex2.error());
            }
        }
        return null;
    }

}
