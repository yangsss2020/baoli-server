package com.baoli.ucenter.ums.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baoli.common.constans.UCenterContant;
import com.baoli.common.exception.BaoliException;
import com.baoli.common.util.NumUtils;
import com.baoli.ucenter.properties.WxAppProperties;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Map;
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
@EnableConfigurationProperties(WxAppProperties.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private WxAppProperties wxAppProperties;

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
        bucket.delete();
        Member member = new Member();
        member.setPhone(memberQuery.getMobile());
        String salt = CodecUtils.generateSalt();
        String password = CodecUtils.md5Hex(memberQuery.getPassword(), salt);
        member.setPassword(password);
        member.setSalt(salt);
        String phone = memberQuery.getMobile();
        member.setUsername(phone);
        String end = phone.substring(7);
        String start = phone.substring(0, 3);
        member.setNickname(start+"****"+end);
        member.setAvatar("http://img.yangsss.xyz/avatar.png");
        this.memberMapper.insert(member);
        String token = jwtTokenUtil.generateToken(member);
        RBucket<String> rBucket = this.redissonClient.getBucket(UCenterContant.USER_TOEKN + member.getPhone());
        rBucket.set(token, 7, TimeUnit.HOURS);
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
        rBucket.set(token, 7, TimeUnit.HOURS);
        return token;
    }

    @Override
    public String smsLogin(Map<String, Object> map) {
        RBucket<String> bucket = redissonClient.getBucket(UCenterContant.USER_REGISTER_CODE + map.get("mobile").toString());
        if (bucket.isExists()) {
            String code = bucket.get();
            if (!StringUtils.equals(map.get("code").toString(), code)) {
                throw new BaoliException("验证码不正确", 200011);
            }
        } else {
            throw new BaoliException("验证码不正确", 200011);
        }
        bucket.delete();
        Object openId = map.get("user_wx_id");
        String phone = map.get("mobile").toString();
        Member member = new Member();
        if (openId != null) {
            //微信小程序登陆
            Member memberPhone = this.memberMapper.selectOne(new LambdaQueryWrapper<>(new Member().setPhone(phone)));
            if (memberPhone == null) {
                member.setPhone(phone);
                member.setOpenId(openId.toString());
                member.setAvatar("http://img.yangsss.xyz/avatar.png");
                member.setUsername(phone);
                String end = phone.substring(7);
                String start = phone.substring(0, 3);
                member.setNickname(start+"****"+end);
                this.memberMapper.insert(member);
            } else {
                memberPhone.setOpenId(openId.toString());
                this.memberMapper.updateById(memberPhone);
                member = memberPhone;
            }
        } else {
            //手机登陆
            member.setPhone(phone);
            Member memberOne = this.memberMapper.selectOne(new LambdaQueryWrapper<>(member));
            if (memberOne == null) {
                member.setAvatar("http://img.yangsss.xyz/avatar.png");
                member.setUsername(phone);
                String end = phone.substring(7);
                String start = phone.substring(0, 3);
                member.setNickname(start+"****"+end);
                this.memberMapper.insert(member);
            } else {
                member = memberOne;
            }
        }
        String token = jwtTokenUtil.generateToken(member);
        RBucket<String> rBucket = this.redissonClient.getBucket(UCenterContant.USER_TOEKN + member.getPhone());
        rBucket.set(token, 7, TimeUnit.HOURS);
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
        if (!StringUtils.equals(memberPassword.getNewPassword(), memberPassword.getRePassword())) {
            throw new BaoliException("两次密码不一致");
        }
        Member member = this.memberMapper.selectById(memberPassword.getMemberId());
        String md5Hex = CodecUtils.md5Hex(memberPassword.getPassword(), member.getSalt());
        if (!StringUtils.equals(md5Hex, member.getPassword())) {
            throw new BaoliException("密码错误");
        }
        String newPassword = CodecUtils.md5Hex(memberPassword.getNewPassword(), member.getSalt());
        member.setPassword(newPassword);
        this.memberMapper.updateById(member);
    }

    @Override
    public Map<String, Object> wxAppLogin(String code) {
        String url =
                "https://api.weixin.qq.com/sns/jscode2session?appid=" + this.wxAppProperties.getAppId() + "&secret=" + this.wxAppProperties.getSecret() +
                        "&js_code" +
                        "=" + code + "" +
                        "&grant_type" +
                        "=authorization_code";
        String resultStr = HttpUtil.get(url);
        Map<String, Object> result = (Map<String, Object>) JSON.parse(resultStr);
        Object errcode = result.get("errcode");
        if (errcode != null) {
            int i = NumberUtils.toInt(errcode.toString());
            if (i != 0) {
                return null;
            }
        }
        return result;
    }

    @Override
    public String getAccessToken() {
        RBucket<Object> bucket = this.redissonClient.getBucket(UCenterContant.WX_ACCESS_TOKEN);
        String token;
        if (bucket.get() == null) {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.wxAppProperties.getAppId() + "&secret" +
                    "=" + this.wxAppProperties.getSecret();
            String resultStr = HttpUtil.get(url);
            Map<String, Object> result = (Map<String, Object>) JSON.parse(resultStr);
            Object access_token = result.get("access_token");
            if (access_token == null) {
                return null;
            }
            String expiresIn = result.get("expires_in").toString();
            int expiresTime = NumberUtils.toInt(expiresIn);
            token = access_token.toString();
            bucket.set(token, expiresTime, TimeUnit.SECONDS);
        } else {
            token = bucket.get().toString();
        }

        return token;

    }

    @Override
    public String wxAppLogin2(String openId) {
        Member member = this.memberMapper.selectOne(new LambdaQueryWrapper<>(new Member().setOpenId(openId)));
        if (member == null) return null;
        String token = jwtTokenUtil.generateToken(member);
        RBucket<String> rBucket = this.redissonClient.getBucket(UCenterContant.USER_TOEKN + member.getPhone());
        rBucket.set(token, 7, TimeUnit.HOURS);
        return token;
    }


}
