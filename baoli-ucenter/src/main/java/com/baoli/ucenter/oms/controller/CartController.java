package com.baoli.ucenter.oms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.oms.entity.Cart;
import com.baoli.ucenter.interceptor.LoginInterceptor;
import com.baoli.ucenter.oms.service.CartService;
import com.baoli.ucenter.query.CartQuery;
import com.baoli.ucenter.vo.CartVo;
import com.baoli.ums.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 购物车列表 前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/oms/cart")
@Api(tags = "购物车")
public class CartController {
    @Autowired
    private CartService cartService;


    @PostMapping("getnumber")
    @ApiOperation("获取购物车数量")
    public R getNumber() {
        Member member = LoginInterceptor.getMember();
        Integer number = this.cartService.getNumber(member.getId());
        if (number == null) {
            return R.error();
        }
        return R.ok().data(number);
    }

    @PostMapping("add")
    @ApiOperation("添加购物车")
    public R add(@RequestBody Cart cart) {
        Member member = LoginInterceptor.getMember();
        this.cartService.add(cart, member.getId());
        return R.ok();
    }

    @PostMapping("getlist")
    @ApiOperation("获取购物车列表")
    public R getList(@RequestBody CartQuery cartQuery) {
        Member member = LoginInterceptor.getMember();
        CartVo cartVo = this.cartService.getList(member.getId(), cartQuery);
        if (CollectionUtils.isEmpty(cartVo.getList())) {
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(cartVo);
    }

    @PostMapping("setnums")
    @ApiOperation("设置购物车数量")
    public R SetNums(@RequestBody Map<String, Object> map) {
        Member member = LoginInterceptor.getMember();
        CartVo cartVo = this.cartService.setNum(NumberUtils.toLong(map.get("id").toString()),
                NumberUtils.toInt(map.get("quantity").toString()), member.getId());
        return R.ok().data(cartVo);
    }

    @PostMapping("del")
    @ApiOperation("删除购物车")
    public R del(@RequestBody Map<String,Object> map){
        String ids = map.get("ids").toString();
        if(StringUtils.isBlank(ids)){
            return R.error();
        }
        String[] idArr = ids.split(",");
        this.cartService.removeByIds(Arrays.asList(idArr));
        return R.ok().message("移除购物车成功");
    }
}

