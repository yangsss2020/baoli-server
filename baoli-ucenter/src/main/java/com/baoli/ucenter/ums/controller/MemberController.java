package com.baoli.ucenter.ums.controller;


import com.baoli.common.vo.R;
import com.baoli.ucenter.query.MemberQuery;
import com.baoli.ucenter.ums.service.MemberService;
import com.baoli.ums.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/ums/user")
@Api(tags = "用户中心")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("sms")
    public R sms(@RequestBody Map<String, Object> map) {
        String phone = map.get("mobile").toString();
        if (StringUtils.isBlank(phone)) {
            return R.error().message("请输入正确的手机号");
        }
        memberService.sendVerifyCode(phone);
        return R.ok();
    }

    @PostMapping("register")
    @ApiOperation("注册")
    public R register(@Valid @RequestBody MemberQuery memberQuery) {
        String token = this.memberService.register(memberQuery);
        return R.ok().data(token);
    }

    @PostMapping("login")
    @ApiOperation("密码登陆")
    public R login(@RequestBody MemberQuery memberQuery) {
        String token = this.memberService.login(memberQuery);
        return R.ok().data(token);
    }

    @PostMapping("info")
    @ApiOperation("获取用户信息")
    public R getMember(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return R.error().message("参数错误");
        }
        Member member = this.memberService.getMember(token);
        if (member == null) {
            return R.error();
        }
        return R.ok().data(member);
    }
}

