package com.baoli.manage.test;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ys
 * @create 2020-01-09-19:16
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UploadTest {
    @Test
    public void uploadImageTest() {
//构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "mABUnc2fsa2wJVoX339siT7P042bgfAOh7vD71nY";
        String secretKey = "XZyGnFuKPx-2p_yFBBahfhxSWnx_lkg8rFPHOKQs";
        String bucket = "baoli-ys";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\11019\\Desktop\\img\\3.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    public void test2() {
//        String aa = "http//:123";
//        System.out.println(aa.substring(6));
//        System.out.println(aa);
        String saleParam = "{\"4\":[\"白色\",\"金色\",\"玫瑰金\"],\"12\":[\"3GB\"],\"13\":[\"16GB\"]}";
        Map<String, Object> parse = (Map<String, Object>) JSON.parse(saleParam);
        System.out.println(parse.get("12"));
//        Long aa = 19l;
//        String bb = String.valueOf(aa);
//        System.out.println(bb);
    }
    @Test
    public void test3(){
        Set set = new HashSet();
        set.add("黑色");
        set.add("黑色");
        System.out.println(set.size());
    }
    @Test
    public void test4(){
        Map<String,String> map = new HashMap<>();
        map.put("4","粉");
        map.put("12","4GB");
        map.put("13","32GB");
//        for (String s : map.keySet()) {
//            System.out.println(s);
//
//        }
//        for (String value : map.values()) {
//            System.out.println(value);
//        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    @Test
    public void test5(){
        Long a = null;
        Long b = null;
        boolean b1 = ObjectUtils.notEqual(a, b);
//        System.out.println(b1);
//        System.out.println(a==b);
        System.out.println(a.equals(b));
//        NumberUtils.
    }

}
