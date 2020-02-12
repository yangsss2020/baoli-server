package com.baoli.manage.sms.service.impl;

import com.baoli.manage.sms.mapper.RoleMapper;
import com.baoli.manage.sms.service.RoleService;
import com.baoli.sms.entity.Role;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-02-11
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRolesByAuthId(Integer id) {
        List<Role> roles = this.roleMapper.findRolesByAuthId(id);
        return roles;
    }
}
