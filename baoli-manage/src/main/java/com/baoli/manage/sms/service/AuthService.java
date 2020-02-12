package com.baoli.manage.sms.service;

import com.baoli.sms.entity.Auth;
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
public interface AuthService extends IService<Auth> {

    List<Auth> findAllAuth();
}
