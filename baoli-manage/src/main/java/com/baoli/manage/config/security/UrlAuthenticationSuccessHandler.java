package com.baoli.manage.config.security;

import com.alibaba.fastjson.JSON;
import com.baoli.common.vo.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义登录成功处理器：返回状态码200
 */
@Component
public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//
//    }

//    @Resource
//    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json; charset=utf-8");
        R r = R.ok().message("登陆成功");
        httpServletResponse.setStatus(200);
        httpServletResponse.getWriter().write(JSON.toJSONString(r));
    }
}
