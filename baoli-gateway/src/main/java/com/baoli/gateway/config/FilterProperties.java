package com.baoli.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author ys
 * @create 2020-01-19-23:41
 */
@ConfigurationProperties(prefix = "baoli.filter")
@Data
public class FilterProperties {
    private List<String> allowPaths;
}
