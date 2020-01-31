package com.baoli.ucenter.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ys
 * @create 2020-01-30-13:43
 */
@Data
public class CartVo {
    private List<CartsVo> list;
    private BigDecimal amount;
}
