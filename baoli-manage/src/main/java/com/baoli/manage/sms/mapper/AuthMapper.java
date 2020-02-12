package com.baoli.manage.sms.mapper;

import com.baoli.sms.entity.Auth;
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
public interface AuthMapper extends BaseMapper<Auth> {

    List<Auth> findAuthListByAdminId(Integer id);
}
