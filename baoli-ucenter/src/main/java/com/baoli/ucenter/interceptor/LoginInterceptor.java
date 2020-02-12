package com.baoli.ucenter.interceptor;

import com.baoli.ucenter.utils.JwtTokenUtil;
import com.baoli.ums.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ys
 * @create 2020-01-21-11:19
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final ThreadLocal<Member> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
//        if (StringUtils.isBlank(token)) {
//            return false;
//        }
        Member member = jwtTokenUtil.getMemberFormToken(token);
        if (member != null) {
            THREAD_LOCAL.set(member);
        }
        return true;
//        if (member != null) {
//            THREAD_LOCAL.set(member);
//            return true;
//        } else {
//            return false;
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }

    public static Member getMember() {
        return THREAD_LOCAL.get();
    }
}
