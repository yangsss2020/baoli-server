package com.baoli.main.pms.service.impl;

import com.baoli.common.constans.MainCacheConstant;
import com.baoli.main.pms.mapper.SkuStockMapper;
import com.baoli.pms.entity.Sku;
import com.baoli.main.pms.mapper.SkuMapper;
import com.baoli.main.pms.service.SkuService;
import com.baoli.pms.entity.SkuStock;
import com.baoli.vo.SkuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private RedissonClient redissonClient;
    @Override
    public List<SkuVo> findSkuVoListBySpuId(Long spuId) {
        List<Sku> skuList = list(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, spuId).eq(Sku::getEnable, true).orderByAsc(Sku::getPrice));
        List<SkuVo> list = skuList.stream().map(sku -> {
            SkuVo skuVo = new SkuVo();
            BeanUtils.copyProperties(sku, skuVo);
            SkuStock skuStock = this.skuStockMapper.selectById(sku.getId());
            skuVo.setSkuStock(skuStock);
            return skuVo;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public SkuVo findSkuVoBySkuId(Long id) {
        SkuVo skuVo = new SkuVo();
        SkuStock skuStock = this.skuStockMapper.selectById(id);
        Sku sku = this.getById(id);
        BeanUtils.copyProperties(sku, skuVo);
        skuVo.setSkuStock(skuStock);
        return skuVo;
    }

    @Override
    public SkuStock findSkuStockBySkuId(Long id) {
        return this.skuStockMapper.selectById(id);
    }

    @Override
    @Transactional
    public Boolean stockDecrement(Long id, Integer quantity,Long spuId) {
        int i = this.skuStockMapper.stockDecrement(id, quantity);
        if(i==1){
            //删除redis缓存
            RBucket<Object> bucket = this.redissonClient.getBucket(MainCacheConstant.GOODS_DETAIL + spuId);
            bucket.delete();
            return true;
        }else {
            return false;
        }
    }
}
