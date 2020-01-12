package com.baoli.manage.pms.service.impl;

import com.baoli.manage.pms.mapper.SkuStockMapper;
import com.baoli.manage.pms.mapper.SpuDetailMapper;
import com.baoli.manage.query.SkuQuery;
import com.baoli.manage.vo.SkuVo;
import com.baoli.pms.entity.Sku;
import com.baoli.manage.pms.mapper.SkuMapper;
import com.baoli.manage.pms.service.SkuService;
import com.baoli.pms.entity.SkuStock;
import com.baoli.pms.entity.SpuDetail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public void saveSku(Long spuId, List<SkuVo> skuVos) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        remove(new LambdaQueryWrapper<>(sku));
        skuVos.forEach(skuVo -> {
            skuVo.setSpuId(spuId);
            save(skuVo);
            skuVo.getSkuStock().setSkuId(skuVo.getId());
            this.skuStockMapper.insert(skuVo.getSkuStock());
        });
    }

    @Override
    public void saveSkuAndParam(SkuQuery skuQuery) {
        this.saveSku(skuQuery.getSpuId(), skuQuery.getSkuList());
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(skuQuery.getSpuId());
        spuDetail.setSaleParam(skuQuery.getSaleParam());
        this.spuDetailMapper.updateById(spuDetail);
    }
}
