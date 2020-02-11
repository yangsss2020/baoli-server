package com.baoli.manage.sms.service.impl;

import com.baoli.manage.sms.mapper.AdminMapper;
import com.baoli.manage.sms.mapper.AuthMapper;
import com.baoli.manage.sms.mapper.RoleMapper;
import com.baoli.sms.entiry.SecurityAdmin;
import com.baoli.sms.entity.Admin;
import com.baoli.sms.entity.Role;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ys
 * @create 2020-02-11-11:14
 */
@Service
@Slf4j
public class SecurityDetailService implements UserDetailsService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Admin admin = this.adminMapper.selectOne(new LambdaQueryWrapper<>(new Admin().setUsername(username)));
        if (admin == null) {
            return null;
        }
        List<Role> roles = this.roleMapper.findRoleListByAdminId(admin.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();
//        List<Auth> auths = this.authMapper.findAuthListByAdminId(admin.getId());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        log.info("用户角色 {}" ,authorities);
        return new SecurityAdmin(admin, authorities);
    }
}
