package com.baoli.manage.pms.service;

import com.baoli.manage.vo.SaleParamVo;
import com.baoli.vo.SpuVo;
import com.baoli.pms.entity.Spu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品spu 服务类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface SpuService extends IService<Spu> {

    void saveSpu(SpuVo spuVo);

    void updateSpu(SpuVo spuVo);

    void deleteSpu(Long id);

    List<SaleParamVo> findSaleParamBySpuId(Long spuId);
}
