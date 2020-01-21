package com.baoli.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.constans.UCenterContant;
import com.baoli.gateway.config.FilterProperties;
import com.baoli.gateway.utils.JwtTokenUtil;
import com.baoli.gateway.vo.R;
import com.baoli.ums.entity.Member;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author ys
 * @create 2020-01-19-23:34
 */
@Component
@EnableConfigurationProperties(FilterProperties.class)
public class LoginFilter extends ZuulFilter {
    @Autowired
    private FilterProperties filterProperties;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return LoginAFterFilter.isFilter(filterProperties);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json; charset=utf-8");
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            currentContext.setSendZuulResponse(false);
            R r = R.setResult(ResultCodeEnum.LOGIN_ERROR);
            currentContext.setResponseBody(JSON.toJSONString(r));
            response.setStatus(200);
        }
        Boolean tokenValid = jwtTokenUtil.isTokenValid(token);
        //判断token是否过期
        if (!tokenValid) {
            Member member = jwtTokenUtil.getMemberFromexpiredToken(token);
            if (member == null) {
                currentContext.setSendZuulResponse(false);
                R r = R.setResult(ResultCodeEnum.BAD_TOKEN);
                currentContext.setResponseBody(JSON.toJSONString(r));
                response.setStatus(200);
            } else {
                RBucket<String> bucket = this.redissonClient.getBucket(UCenterContant.USER_TOEKN + member.getPhone());
                if (bucket.isExists()) {
                    String newToken = this.jwtTokenUtil.generateToken(member);
                    bucket.set(newToken, 7, TimeUnit.HOURS);
                    currentContext.addZuulRequestHeader("token", newToken);
                    response.setHeader("newToken", newToken);
                } else {
                    currentContext.setSendZuulResponse(false);
                    R r = R.setResult(ResultCodeEnum.LOGIN_EXPIRATION);
                    currentContext.setResponseBody(JSON.toJSONString(r));
                    response.setStatus(200);
                }
            }
        }
        return null;
    }
}
