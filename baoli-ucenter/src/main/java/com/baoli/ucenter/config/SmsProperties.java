package com.baoli.ucenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ys
 * @create 2019-12-12-0:25
 */
@ConfigurationProperties(prefix = "baoli.sms")
public class SmsProperties {
    private String accessKeyId;
    private String accessSecret;
    private String SignName;
    private String TemplateCode;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSignName() {
        return SignName;
    }

    public void setSignName(String signName) {
        SignName = signName;
    }

    public String getTemplateCode() {
        return TemplateCode;
    }

    public void setTemplateCode(String templateCode) {
        TemplateCode = templateCode;
    }
}
