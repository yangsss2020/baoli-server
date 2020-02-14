package com.baoli.main.pms.service;

import com.baoli.pms.entity.Sku;
import com.baoli.pms.entity.SkuStock;
import com.baoli.vo.SkuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface SkuService extends IService<Sku> {
    List<SkuVo> findSkuVoListBySpuId(Long spuId);

    SkuVo findSkuVoBySkuId(Long id);

    SkuStock findSkuStockBySkuId(Long id);

    Boolean stockDecrement(Long id, Integer quantity,Long spuId);

    void changeDB();
}
