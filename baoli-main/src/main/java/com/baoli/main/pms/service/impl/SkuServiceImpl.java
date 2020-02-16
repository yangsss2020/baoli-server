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
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    /**
     * 根据spuid查询skuvolist
     * @param spuId
     * @return
     */
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

    /**
     * 根据skuid查询skuvolist
     * @param id
     * @return
     */
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
    public Boolean stockDecrement(Long id, Integer quantity, Long spuId) {
        int i = this.skuStockMapper.stockDecrement(id, quantity);
        if (i == 1) {
            //删除redis缓存
            RBucket<Object> bucket = this.redissonClient.getBucket(MainCacheConstant.GOODS_DETAIL + spuId);
            bucket.delete();
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void changeDB() {
        List<Sku> list = this.list(null);
        String pr = "http://img.yangsss.xyz/";
        list.forEach(item -> {

            String str = item.getIndexes();
            String replace = str.replace("_", ",");
            String newStr = "[" +replace+"]";
            item.setIndexes(newStr);
            //设置图片
            String images = item.getImages();
            String img = StringUtils.substringAfterLast(images, "/");
            String newImage = pr+img;
            item.setImages(newImage);
            //设置价格
            BigDecimal price = item.getPrice();
            BigDecimal decimal = BigDecimal.valueOf(100);
            BigDecimal decimal1 = BigDecimal.valueOf(200);
            BigDecimal decimal2 = BigDecimal.valueOf(500);
            BigDecimal newPrice = price.divide(decimal);
            BigDecimal memberPrice = newPrice.subtract(decimal1);
            BigDecimal originPrice = newPrice.add(decimal2);
            item.setPrice(newPrice);
            item.setMemberPrice(memberPrice);
            item.setOriginalPrice(originPrice);
        });
        this.updateBatchById(list);
    }
}
