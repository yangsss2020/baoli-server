package com.baoli.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.baoli.gateway.config.FilterProperties;
import com.baoli.gateway.utils.JwtTokenUtil;
import com.baoli.gateway.vo.R;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-19-23:34
 */
@Component
@EnableConfigurationProperties(FilterProperties.class)
public class LoginAFterFilter extends ZuulFilter {
    @Autowired
    private FilterProperties filterProperties;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 11;
    }

    @Override
    public boolean shouldFilter() {
        return isFilter(filterProperties);
    }

    static boolean isFilter(FilterProperties filterProperties) {
        List<String> allowPaths = filterProperties.getAllowPaths();
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String uri = request.getRequestURI();

        for (String path : allowPaths) {
            if (uri.startsWith(path)) {
                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        String newToken = response.getHeader("newToken");
        if (StringUtils.isNotBlank(newToken)) {
            Object zuulResponse = currentContext.get("zuulResponse");
            if (zuulResponse != null) {
                response.setContentType("application/json; charset=utf-8");
                RibbonHttpResponse resp = (RibbonHttpResponse) zuulResponse;
                String body = IOUtils.toString(resp.getBody(), "utf-8");
                Map<String, Object> parse = (Map<String, Object>) JSON.parse(body);
                R r = R.ok().code(NumberUtils.toInt(parse.get("code").toString()));
                r.setStatus(Boolean.parseBoolean(parse.get("status").toString()));
                r.setData(parse.get("data"));
                r.setMsg(parse.get("msg").toString());
                r.setToken(newToken);
                currentContext.setSendZuulResponse(false);
                currentContext.setResponseBody(JSON.toJSONString(r));

            }
        }
        return null;
    }
}
