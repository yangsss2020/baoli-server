package com.baoli.ucenter.oms.service.impl;

import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.exception.BaoliException;
import com.baoli.common.vo.R;
import com.baoli.oms.entity.Cart;
import com.baoli.pms.entity.SkuStock;
import com.baoli.ucenter.client.SkuClient;
import com.baoli.ucenter.oms.mapper.CartMapper;
import com.baoli.ucenter.oms.service.CartService;
import com.baoli.ucenter.query.CartQuery;
import com.baoli.ucenter.vo.CartVo;
import com.baoli.ucenter.vo.CartsVo;
import com.baoli.vo.SkuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 购物车列表 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Integer getNumber(String id) {
        Cart cart = this.cartMapper.selectOne(new LambdaQueryWrapper<>(new Cart().setMemberId(id)));
        return cart.getQuantity();
    }

    @Override
    @Transactional
    public Long add(Cart cart, String memberId) {
        R r = this.skuClient.findSkuVoBySkuId(cart.getSkuId());
        Object data = r.getData();
        SkuVo skuVo = mapper.convertValue(data, SkuVo.class);
        Cart selectOne = this.cartMapper.selectOne(new LambdaQueryWrapper<>(new Cart().setSkuId(cart.getSkuId())));
//        //判断商品库存 Todo
        if (skuVo.getSkuStock().getStock() < cart.getQuantity()) {
            throw new BaoliException(ResultCodeEnum.UNDERSTOCK);
        }
        //判断购物车是否已经有该商品
        if(cart.getType()==1){
            if (selectOne != null) {
                selectOne.setQuantity(cart.getQuantity() + selectOne.getQuantity());
                this.cartMapper.updateById(selectOne);
                return selectOne.getId();
            } else {
                return saveCart(cart, memberId, skuVo);
            }
        }else if (cart.getType()==2){
            return saveCart(cart, memberId, skuVo);
        }else {
            return null;
        }
    }

    private Long saveCart(Cart cart, String memberId, SkuVo skuVo) {
        cart.setMemberId(memberId);
        cart.setPrice(skuVo.getPrice());
        cart.setSkuIamges(skuVo.getImages());
        cart.setSkuName(skuVo.getTitle());
        cart.setSkuParam(skuVo.getOwnParam());
        cart.setSpuId(skuVo.getSpuId());
        this.cartMapper.insert(cart);
        return cart.getId();
    }

    @Override
    public CartVo getList(String id, CartQuery cartQuery) {
        List<Cart> cartList = this.cartMapper.selectList(new LambdaQueryWrapper<>(new Cart().setMemberId(id)));
        CartVo cartVo = new CartVo();
        String ids = cartQuery.getIds();
        List<String> idList = new ArrayList<>();
        if(StringUtils.isNotBlank(ids)){
            String[] arr = ids.split(",");
            idList = Arrays.asList(arr);
        }

        BigDecimal amount = BigDecimal.ZERO;
        List<CartsVo> list = new ArrayList<>();
        for (Cart cart : cartList) {
            if (ids.contains(cart.getId().toString())) {
                BigDecimal num = new BigDecimal(cart.getQuantity());
                BigDecimal decimal = amount.add(cart.getPrice().multiply(num));
                amount = decimal;
                cart.setSelected(true);
            }
            CartsVo cartsVo = mapper.convertValue(cart, CartsVo.class);
            SkuStock stock = this.skuClient.findSkuStockBySkuId(cart.getSkuId());
            cartsVo.setProducts(stock);
            list.add(cartsVo);
        }
        cartVo.setList(list);
        cartVo.setAmount(amount);
        return cartVo;
    }

    @Override
    public CartVo setNum(long id, Integer quantity,String memberId) {
        this.cartMapper.setNum(id,quantity);
        List<Cart> cartList = this.cartMapper.selectList(new LambdaQueryWrapper<>(new Cart().setMemberId(memberId)));
        CartVo cartVo = new CartVo();
        BigDecimal amount = BigDecimal.ZERO;
        List<CartsVo> list = new ArrayList<>();
        for (Cart cart : cartList) {
            CartsVo cartsVo = mapper.convertValue(cart, CartsVo.class);
            SkuStock stock = this.skuClient.findSkuStockBySkuId(cart.getSkuId());
            cartsVo.setProducts(stock);
            list.add(cartsVo);
        }
        cartVo.setList(list);
        cartVo.setAmount(amount);
        return cartVo;
    }
}
