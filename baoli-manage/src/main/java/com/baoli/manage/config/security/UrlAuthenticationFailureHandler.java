package com.baoli.manage.config.security;

import com.alibaba.fastjson.JSON;
import com.baoli.common.vo.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义登录失败处理器：返回状态码402
 */
@SuppressWarnings("Duplicates")
@Component
public class UrlAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json; charset=utf-8");
        R r = R.error().message("登陆失败");
        httpServletResponse.setStatus(200);
        httpServletResponse.getWriter().write(JSON.toJSONString(r));
    }
}
