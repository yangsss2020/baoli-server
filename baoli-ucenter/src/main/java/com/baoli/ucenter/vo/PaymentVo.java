package com.baoli.ucenter.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-02-02-12:55
 */
@Data
public class PaymentVo {
    private BigDecimal money;
    private List<Map<String,Object>> rel;
    private String token;
}
