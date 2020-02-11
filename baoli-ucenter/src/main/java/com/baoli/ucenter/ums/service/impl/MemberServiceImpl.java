package com.baoli.ucenter.ums.service.impl;

import com.baoli.common.constans.UCenterContant;
import com.baoli.common.exception.BaoliException;
import com.baoli.common.util.NumUtils;
import com.baoli.ucenter.query.MemberPasswordQuery;
import com.baoli.ucenter.query.MemberQuery;
import com.baoli.ucenter.ums.mapper.MemberMapper;
import com.baoli.ucenter.ums.service.MemberService;
import com.baoli.ucenter.utils.CodecUtils;
import com.baoli.ucenter.utils.JwtTokenUtil;
import com.baoli.ucenter.utils.SmsUtils;
import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 发送短信验证码
     *
     * @param phone
     */
    @Override
    public void sendVerifyCode(String phone) {
        String code = NumUtils.generateCode(6);
        RBucket<String> bucket = redissonClient.getBucket(UCenterContant.USER_REGISTER_CODE + phone);
        bucket.set(code, 5, TimeUnit.MINUTES);
        smsUtils.sendSms(phone, code);
    }

    @Override
    public String register(MemberQuery memberQuery) {
        Member one = this.memberMapper.selectOne(new LambdaQueryWrapper<>(new Member().setPhone(memberQuery.getMobile())));
        if (one != null) {
            throw new BaoliException("用户存在", 200011);
        }
        RBucket<String> bucket = redissonClient.getBucket(UCenterContant.USER_REGISTER_CODE + memberQuery.getMobile());
        if (bucket.isExists()) {
            String code = bucket.get();
            if (!StringUtils.equals(memberQuery.getCode(), code)) {
                throw new BaoliException("验证码不正确", 200011);
            }
        } else {
            throw new BaoliException("验证码不正确", 200011);
        }
        Member member = new Member();
        member.setPhone(memberQuery.getMobile());
        String salt = CodecUtils.generateSalt();
        String password = CodecUtils.md5Hex(memberQuery.getPassword(), salt);
        member.setPassword(password);
        member.setSalt(salt);
        member.setUsername(memberQuery.getMobile());
        this.memberMapper.insert(member);
        bucket.delete();
        String token = jwtTokenUtil.generateToken(member);
        RBucket<String> rBucket = this.redissonClient.getBucket(UCenterContant.USER_TOEKN + member.getPhone());
        rBucket.set(token,7,TimeUnit.HOURS);
        return token;
    }

    @Override
    public String login(MemberQuery memberQuery) {
        Member member = this.memberMapper.selectOne(new LambdaQueryWrapper<>(new Member().setPhone(memberQuery.getMobile())));
        if (member == null) {
            throw new BaoliException("该用户不存在", 200012);
        }
        if (!member.getStatus()) {
            throw new BaoliException("抱歉,该账号已被禁用", 200012);
        }
        String md5Hex = CodecUtils.md5Hex(memberQuery.getPassword(), member.getSalt());
        if (!StringUtils.equals(md5Hex, member.getPassword())) {
            throw new BaoliException("密码错误", 200012);
        }
        String token = jwtTokenUtil.generateToken(member);
        RBucket<String> rBucket = this.redissonClient.getBucket(UCenterContant.USER_TOEKN + member.getPhone());
        rBucket.set(token,7,TimeUnit.HOURS);
        return token;
    }

    @Override
    public Member getMember(String token) {
        Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        Member memberFormToken = jwtTokenUtil.getMemberFormToken(token);
        if (memberFormToken == null) {
            return null;
        }
        String id = memberFormToken.getId();
        Member member = this.memberMapper.selectById(id);
        return member;
    }

    @Override
    public void changePassword(MemberPasswordQuery memberPassword) {
        if (!StringUtils.equals(memberPassword.getNewPassword(),memberPassword.getRePassword())){
            throw new BaoliException("两次密码不一致");
        }
        Member member = this.memberMapper.selectById(memberPassword.getMemberId());
        String md5Hex = CodecUtils.md5Hex(memberPassword.getPassword(), member.getSalt());
        if(!StringUtils.equals(md5Hex,member.getPassword())){
            throw new BaoliException("密码错误");
        }
        String newPassword = CodecUtils.md5Hex(memberPassword.getNewPassword(), member.getSalt());
        member.setPassword(newPassword);
        this.memberMapper.updateById(member);
    }
}
