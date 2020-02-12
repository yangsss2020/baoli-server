package com.baoli.manage.sms.service.impl;

import com.baoli.manage.sms.mapper.AuthMapper;
import com.baoli.manage.sms.service.AuthService;
import com.baoli.sms.entity.Auth;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-02-11
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private AuthMapper authMapper;
    @Override
    public List<Auth> findAllAuth() {
//        QueryWrapper<Auth> wrapper = new QueryWrapper<>();
//        wrapper.ne("")
        List<Auth> auths = this.authMapper.selectList(new LambdaQueryWrapper<Auth>().ne(Auth::getParentId, 0));
        return auths;
    }
}
