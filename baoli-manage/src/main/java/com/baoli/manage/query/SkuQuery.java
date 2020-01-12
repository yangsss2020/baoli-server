package com.baoli.manage.query;

import com.baoli.manage.vo.SkuVo;
import lombok.Data;

import java.util.List;

/**
 * @author ys
 * @create 2020-01-12-0:53
 */
@Data
public class SkuQuery {
    private Long spuId;
    private String saleParam;
    private List<SkuVo> skuList;
}
