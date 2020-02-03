package com.baoli.ucenter.query;

import lombok.Data;

/**
 * @author ys
 * @create 2020-02-02-13:47
 */
@Data
public class PayQuery {
    private String paymentCode;
    private Integer paymentType;
    private String token;
    private String ids;
    private String memberId;
}
