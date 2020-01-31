package com.baoli.ucenter.vo;

import com.baoli.oms.entity.Cart;
import com.baoli.pms.entity.SkuStock;
import lombok.Data;

/**
 * @author ys
 * @create 2020-01-30-12:37
 */
@Data
public class CartsVo extends Cart {
    private SkuStock products;
}
