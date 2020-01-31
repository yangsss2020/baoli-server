package com.baoli.ucenter.oms.service;

import com.baoli.oms.entity.Cart;
import com.baoli.ucenter.query.CartQuery;
import com.baoli.ucenter.vo.CartVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 购物车列表 服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface CartService extends IService<Cart> {

    Integer getNumber(String id);

    void add(Cart cart,String memberId);

    CartVo getList(String id, CartQuery cartQuery);

    CartVo setNum(long id, Integer quantity,String memberId);
}
