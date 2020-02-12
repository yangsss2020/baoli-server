package com.baoli.manage.sms.service;

import com.baoli.sms.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ys
 * @since 2020-02-11
 */
public interface RoleService extends IService<Role> {

    List<Role> findRolesByAuthId(Integer id);
}
