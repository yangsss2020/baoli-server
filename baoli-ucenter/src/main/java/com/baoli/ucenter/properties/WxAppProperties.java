package com.baoli.ucenter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ys
 * @create 2020-02-11-17:34
 */
@ConfigurationProperties(prefix = "baoli.wxapp")
@Data
public class WxAppProperties {
    private String appId;
    private String secret;
}
