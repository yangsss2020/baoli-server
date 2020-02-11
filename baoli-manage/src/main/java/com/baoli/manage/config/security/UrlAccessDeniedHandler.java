package com.baoli.manage.config.security;

import com.alibaba.fastjson.JSON;
import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义权限不足处理器：返回状态码403
 */
@Component
public class UrlAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        R r = R.setResult(ResultCodeEnum.AUTH_DENIED);
        httpServletResponse.setStatus(403);
        httpServletResponse.getWriter().write(JSON.toJSONString(r));
    }
}