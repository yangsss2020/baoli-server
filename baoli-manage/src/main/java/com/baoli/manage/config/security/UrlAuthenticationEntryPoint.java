package com.baoli.manage.config.security;

import com.alibaba.fastjson.JSON;
import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义未登录时：返回状态码401
 */
@SuppressWarnings("Duplicates")
@Component
public class UrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        R r = R.setResult(ResultCodeEnum.NOT_LOGIN_IN);
        httpServletResponse.setStatus(200);
        httpServletResponse.getWriter().write(JSON.toJSONString(r));
    }
}
