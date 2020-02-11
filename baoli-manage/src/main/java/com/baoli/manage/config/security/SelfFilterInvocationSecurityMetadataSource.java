package com.baoli.manage.config.security;

import com.baoli.manage.sms.service.AuthService;
import com.baoli.manage.sms.service.RoleService;
import com.baoli.sms.entity.Auth;
import com.baoli.sms.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Slf4j
@Component
public class SelfFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        Set<ConfigAttribute> set = new HashSet<>();
        // 获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        log.info("requestUrl >> {}", requestUrl);
        System.out.println("请求的路径--------------");
        System.out.println("请求的路径--------------");
        System.out.println("请求的路径--------------");
        System.out.println(requestUrl);
        List<Auth> auths = this.authService.findAllAuth();
        for (Auth auth : auths) {
            if (requestUrl.startsWith(auth.getUrl())) {
                List<Role> roles = this.roleService.findRolesByAuthId(auth.getId());
                roles.forEach(role -> {
                    SecurityConfig securityConfig = new SecurityConfig("ROLE_"+role.getName());
                    set.add(securityConfig);
                });
            }
        }
//        for (String url : menuUrl) {
////            if (antPathMatcher.match(url, requestUrl)) {
//            if (requestUrl.startsWith(url)) {
//                System.out.println("拦截的路径-------------------");
//                System.out.println("拦截的路径-------------------");
//                System.out.println("拦截的路径-------------------");
//                System.out.println(url);
////                List<String> roleNames = userService.findRoleNameByMenuUrl(url); //当前请求需要的权限
//                List<String> roleNames = Arrays.asList("ROLE_admin");
//                roleNames.forEach(roleName -> {
//                    SecurityConfig securityConfig = new SecurityConfig(roleName);
//                    set.add(securityConfig);
//                });
//            }
//        }
        if (ObjectUtils.isEmpty(set)) {
            return SecurityConfig.createList("ROLE_LOGIN");
        }
        return set;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
