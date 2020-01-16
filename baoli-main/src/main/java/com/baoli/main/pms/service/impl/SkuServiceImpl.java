package com.baoli.main.pms.service.impl;

import com.baoli.main.pms.mapper.SkuStockMapper;
import com.baoli.pms.entity.Sku;
import com.baoli.main.pms.mapper.SkuMapper;
import com.baoli.main.pms.service.SkuService;
import com.baoli.pms.entity.SkuStock;
import com.baoli.vo.SkuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    @Autowired
    private SkuStockMapper skuStockMapper;

    @Override
    public List<SkuVo> findSkuVoListBySpuId(Long spuId) {
        List<Sku> skuList =list(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, spuId).eq(Sku::getEnable,true).orderByAsc(Sku::getPrice));
        List<SkuVo> list = skuList.stream().map(sku -> {
            SkuVo skuVo = new SkuVo();
            BeanUtils.copyProperties(sku, skuVo);
            SkuStock skuStock = this.skuStockMapper.selectById(sku.getId());
            skuVo.setSkuStock(skuStock);
            return skuVo;
        }).collect(Collectors.toList());
        return list;
    }
}
