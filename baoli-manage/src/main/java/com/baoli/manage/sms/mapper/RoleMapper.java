package com.baoli.manage.sms.mapper;

import com.baoli.sms.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-02-11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findRoleListByAdminId(Integer id);

    List<Role> findRolesByAuthId(Integer id);
}
