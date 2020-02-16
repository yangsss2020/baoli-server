package com.baoli.ucenter.ums.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.rms.entity.RecordTrading;
import com.baoli.ucenter.interceptor.LoginInterceptor;
import com.baoli.ucenter.query.MemberPasswordQuery;
import com.baoli.ucenter.query.MemberQuery;
import com.baoli.ucenter.query.PayQuery;
import com.baoli.ucenter.sms.service.PaymentService;
import com.baoli.ucenter.ums.service.AddressService;
import com.baoli.ucenter.ums.service.MemberService;
import com.baoli.ums.entity.Address;
import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
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
    @Autowired
    private AddressService addressService;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("sms")
    @ApiOperation("发送短信验证码")
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
    @PostMapping("smslogin")
    @ApiOperation("手机登录")
    public R smsLogin(@RequestBody Map<String,Object> map){
      String token=  this.memberService.smsLogin(map);
      if(StringUtils.isBlank(token)){
          return R.error().message("登陆失败");
      }
      return R.ok().data(token);
    }
    @PostMapping("wxapplogin1")
    @ApiOperation("微信小程序登陆")
    public R wxAppLogin(@RequestBody Map<String, String> map) {
        Map<String, Object> result = this.memberService.wxAppLogin(map.get("code"));
        if(result==null){
            return R.error().message("授权失败");
        }
        return R.ok().data(result);
    }
    @PostMapping("wxapplogin2")
    @ApiOperation("获取用户token")
    public R wxAppLogin2(@RequestBody Map<String,String> map){
        String openId = map.get("open_id");
       String token= this.memberService.wxAppLogin2(openId);
       if(StringUtils.isNotBlank(token)){
           return R.ok().data(token);
       }else {
           return R.error().data("登陆失败");
       }
    }

    @PostMapping("vuesaveusership")
    @ApiOperation("保存收货地址")
    public R save(@RequestBody Address address) {
        Member member = LoginInterceptor.getMember();
        address.setMemberId(member.getId());
        this.addressService.saveAddress(address);
        return R.ok().message("保存成功");
    }

    @PostMapping("getusership")
    @ApiOperation("获取收货地址列表")
    public R getAddresses() {
        Member member = LoginInterceptor.getMember();
        List<Address> list = this.addressService.list(new LambdaQueryWrapper<>(new Address().setMemberId(member.getId())));
        if (CollectionUtils.isEmpty(list)) {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        } else {
            return R.ok().data(list);
        }
    }

    @PostMapping("getshipdetail")
    @ApiOperation("根据id查询地址详情")
    public R getAddressById(@RequestBody Map<String, Long> map) {
        Address address = this.addressService.getById(map.get("id"));
        if (address != null) {
            return R.ok().data(address);
        } else {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
    }

    @PostMapping("removeship")
    @ApiOperation("根据id删除地址")
    public R delAddressById(@RequestBody Map<String, Long> map) {
        boolean flag = this.addressService.removeById(map.get("id"));
        if (flag) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @PostMapping("getuserdefaultship")
    @ApiOperation("获取默认收货地址")
    public R getDefaultAddress() {
        Member member = LoginInterceptor.getMember();
        Address address = new Address();
        address.setMemberId(member.getId());
        address.setDft(true);
        Address defaultAddress = this.addressService.getOne(new LambdaQueryWrapper<>(address));
        if (defaultAddress != null) {
            return R.ok().data(defaultAddress);
        } else {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
    }

    @PostMapping("pay")
    @ApiOperation("支付订单")
    public R pay(@RequestBody PayQuery payQuery) {
        if (StringUtils.isBlank(payQuery.getToken())) {
            return R.error().message("请勿重新支付,或支付信息过期1");
        }
        Member member = LoginInterceptor.getMember();
        payQuery.setMemberId(member.getId());
        RecordTrading record = this.paymentService.pay(payQuery);
        return R.ok().message("支付成功").data(record);
    }

    @PostMapping("editinfo")
    @ApiOperation("修改个人信息")
    public R editInfo(@Valid @RequestBody Member member) {
        Member member1 = LoginInterceptor.getMember();
        member.setId(member1.getId());
        this.memberService.updateById(member);
        return R.ok();
    }

    @PostMapping("editpwd")
    @ApiOperation("修改密码")
    public R editPwd(@Valid @RequestBody MemberPasswordQuery memberPassword) {
        Member member = LoginInterceptor.getMember();
        memberPassword.setMemberId(member.getId());
        this.memberService.changePassword(memberPassword);
        return R.ok();
    }
}

