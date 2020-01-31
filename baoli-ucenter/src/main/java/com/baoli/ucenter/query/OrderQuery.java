package com.baoli.ucenter.query;

import lombok.Data;

/**
 * @author ys
 * @create 2020-01-31-0:05
 */
@Data
public class OrderQuery {
    private String ids;
    private Long addressId;
    private String memberId;
    private String note;
}
