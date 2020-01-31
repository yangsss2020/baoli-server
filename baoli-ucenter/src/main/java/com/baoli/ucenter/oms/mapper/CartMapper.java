package com.baoli.ucenter.oms.mapper;

import com.baoli.oms.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 购物车列表 Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface CartMapper extends BaseMapper<Cart> {

    void setNum(@Param("id") long id,@Param("quantity") Integer quantity);
}
