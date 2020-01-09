package com.baoli.ucenter.oms.service.impl;

import com.baoli.oms.entity.Cart;
import com.baoli.ucenter.oms.mapper.CartMapper;
import com.baoli.ucenter.oms.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
