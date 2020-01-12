package com.baoli.manage.vo;

import com.baoli.pms.entity.Sku;
import com.baoli.pms.entity.SkuStock;
import lombok.Data;

/**
 * @author ys
 * @create 2020-01-10-1:47
 */
@Data
public class SkuVo extends Sku {
    private SkuStock skuStock;

}
