package com.baoli.manage.pms.service;

import com.baoli.manage.query.SkuQuery;
import com.baoli.vo.SkuVo;
import com.baoli.pms.entity.Sku;
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

    void saveSku(Long spuId, List<SkuVo> skuVos);

    void saveSkuAndParam(SkuQuery skuQuery);
}
