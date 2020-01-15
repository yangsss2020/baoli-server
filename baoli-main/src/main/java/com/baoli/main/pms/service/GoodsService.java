package com.baoli.main.pms.service;

import com.alibaba.fastjson.JSON;
import com.baoli.main.pms.mapper.CategoryMapper;
import com.baoli.main.pms.repository.GoodsRepository;
import com.baoli.main.vo.Goods;
import com.baoli.pms.entity.BaseParam;
import com.baoli.pms.entity.Category;
import com.baoli.pms.entity.SaleParam;
import com.baoli.pms.entity.Sku;
import com.baoli.vo.SpuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author ys
 * @create 2020-01-13-11:08
 */
@Service

public class GoodsService {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuDetailService spuDetailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BaseParamService baseParamService;
    @Autowired
    private SaleParamService saleParamService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 出案件elasticsearch商品索引库
     *
     * @param spuVo
     * @return
     */
    @Transactional
    public Goods buildGoods(SpuVo spuVo) {
        Goods goods = new Goods();
        goods.setId(spuVo.getId());
        goods.setTitle(spuVo.getTitle());
        goods.setSubTitle(spuVo.getSubTitle());
        goods.setCid1(spuVo.getCid1());
        goods.setCid2(spuVo.getCid2());
        goods.setCid3(spuVo.getCid3());
        goods.setCreateTime(spuVo.getCreateTime());
        goods.setUpdateTime(spuVo.getUpdateTime());
        Collection<Category> categories = this.categoryService.listByIds(Arrays.asList(spuVo.getCid1(), spuVo.getCid2(), spuVo.getCid3()));
        StringBuilder keyword = new StringBuilder(spuVo.getTitle());
        categories.forEach(item -> {
            keyword.append(" " + item.getName());
        });
        goods.setKeyword(keyword.toString());
        List<Sku> skuList = this.skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, spuVo.getId()).orderByAsc(Sku::getPrice));
        if (CollectionUtils.isEmpty(skuList)) return null;
        String skus = JSON.toJSONString(skuList);
        goods.setImages(skuList.get(0).getImages());
        goods.setPrice(skuList.get(0).getPrice());
        goods.setOriginalPrice(skuList.get(0).getOriginalPrice());
        goods.setMemberPrice(skuList.get(0).getMemberPrice());
        goods.setSkus(skus);
        //规格开始
        Map<String, Object> specs = new HashMap<>();
        //销售属性
        SaleParam saleParam1 = new SaleParam();
        saleParam1.setCid3(spuVo.getCid3());
        saleParam1.setSearching(true);
        List<SaleParam> saleParamList = this.saleParamService.list(new LambdaQueryWrapper<>(saleParam1));
        saleParamList.forEach(saleParam -> {
            HashSet<Object> set = new HashSet<>();
            skuList.forEach(sku -> {
                String skuParam = sku.getOwnParam();
                Map<String, String> skuParamMap = (Map<String, String>) JSON.parse(skuParam);
                for (Map.Entry<String, String> entry : skuParamMap.entrySet()) {
                    if (saleParam.getId().toString().equals(entry.getKey())) {
                        set.add(entry.getValue());
                    }
                }
            });
            specs.put(saleParam.getName(), set);
        });
        //基本属性
        List<BaseParam> baseParamList = this.baseParamService.list(new LambdaQueryWrapper<BaseParam>().eq(BaseParam::getCid, spuVo.getCid3()).eq(BaseParam::getSearching, true));
        String baseParamStr = spuVo.getSpuDetail().getBaseParam();
        Map<String, Object> parse = (Map<String, Object>) JSON.parse(baseParamStr);
        for (BaseParam baseParam : baseParamList) {//            Map<String,String> map = new HashMap<>();
            String value = "";
            if (baseParam.getNumeric()) {

                Object s = parse.get(baseParam.getId().toString());
                double val = NumberUtils.toDouble(s.toString());
//                System.out.println(val);
                value = this.generationParam(baseParam, val);
            } else {
                value = (String) parse.get(baseParam.getId().toString());
            }
            specs.put(baseParam.getName(), value);

        }
        goods.setSpec(specs);
        return goods;
    }

    private String generationParam(BaseParam baseParam, double val) {
        String segment = baseParam.getSegments();
        String[] segs = segment.split(",");
//        double val = NumberUtils.toDouble(value);
        String result = "";
        for (String seg : segs) {
            String[] arr = seg.split("-");
            double begin = NumberUtils.toDouble(arr[0]);
            double end = Double.MAX_VALUE;
            if (arr.length == 2) {
                end = NumberUtils.toDouble(arr[1]);
            }
            if (val >= begin && val <= end) {
                if (arr.length == 1) {
                    result = arr[0] + baseParam.getUnit() + "以上";
                } else if (begin == 0) {
                    result = arr[1] + baseParam.getUnit() + "一下";
                } else {
                    result = seg + baseParam.getUnit();
                }
            }
        }
        return result;
    }
}
