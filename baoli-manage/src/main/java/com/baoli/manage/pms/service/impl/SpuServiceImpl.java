package com.baoli.manage.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baoli.manage.pms.mapper.SaleParamMapper;
import com.baoli.manage.pms.mapper.SkuMapper;
import com.baoli.manage.pms.mapper.SpuDetailMapper;
import com.baoli.manage.vo.SaleParamVo;
import com.baoli.manage.vo.SpuVo;
import com.baoli.pms.entity.SaleParam;
import com.baoli.pms.entity.Sku;
import com.baoli.pms.entity.Spu;
import com.baoli.manage.pms.mapper.SpuMapper;
import com.baoli.manage.pms.service.SpuService;
import com.baoli.pms.entity.SpuDetail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品spu 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SaleParamMapper saleParamMapper;

    @Autowired
    public SkuMapper skuMapper;

    @Override
    public void saveSpu(SpuVo spuVo) {
        save(spuVo);
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spuVo.getId());
        this.spuDetailMapper.insert(spuDetail);
    }

    @Override
    public void updateSpu(SpuVo spuVo) {
        updateById(spuVo);
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spuVo.getId());
        this.spuDetailMapper.updateById(spuDetail);
    }

    @Override
    public void deleteSpu(Long id) {
        removeById(id);
        this.spuDetailMapper.deleteById(id);
    }

    /**
     * 根据spuId查找销售属性
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SaleParamVo> findSaleParamBySpuId(Long spuId) {
        SpuDetail spuDetail = this.spuDetailMapper.selectById(spuId);
        List<Sku> skuList = this.skuMapper.selectList(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, spuId));
        Spu spu = getById(spuId);
        List<SaleParam> list = this.saleParamMapper.selectList(new LambdaQueryWrapper<SaleParam>().eq(SaleParam::getCid3, spu.getCid3()));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<SaleParamVo> paramVos = new ArrayList<>();
        String saleParam = spuDetail.getSaleParam();
        if (StringUtils.isNotBlank(saleParam)) {
            Map<String, Object> parse = (Map<String, Object>) JSON.parse(saleParam);
            paramVos = list.stream().map(item -> {
                SaleParamVo saleParamVo = new SaleParamVo();
                BeanUtils.copyProperties(item, saleParamVo);
                Long aLong = saleParamVo.getId();
                String id = String.valueOf(aLong);
                Object size = parse.get(id);
                saleParamVo.setSize(size);
                skuList.forEach(value -> {
                    Map<String, String> ownParam = (Map<String, String>) JSON.parse(value.getOwnParam());
                    for (Map.Entry<String, String> entry : ownParam.entrySet()) {
                        Long key = Long.valueOf(entry.getKey());
                        if (Objects.equals(key, saleParamVo.getId())) {
                            saleParamVo.getCheck().add(entry.getValue());
                        }
                    }
                });
                return saleParamVo;
            }).collect(Collectors.toList());
        } else {
            paramVos = list.stream().map(item -> {
                SaleParamVo saleParamVo = new SaleParamVo();
                BeanUtils.copyProperties(item, saleParamVo);
                saleParamVo.setSize(new ArrayList<>());
                return saleParamVo;
            }).collect(Collectors.toList());
        }
        return paramVos;
    }
}
