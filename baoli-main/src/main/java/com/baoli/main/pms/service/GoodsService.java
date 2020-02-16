package com.baoli.main.pms.service;

import com.alibaba.fastjson.JSON;
import com.baoli.common.constans.MainCacheConstant;
import com.baoli.main.pms.repository.GoodsRepository;
import com.baoli.main.query.GoodsQuery;
import com.baoli.main.vo.Goods;
import com.baoli.main.vo.GoodsDetile;
import com.baoli.main.vo.GoodsSearchVo;
import com.baoli.pms.entity.BaseParam;
import com.baoli.pms.entity.Category;
import com.baoli.pms.entity.SaleParam;
import com.baoli.vo.SkuVo;
import com.baoli.vo.SpuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ys
 * @create 2020-01-13-11:08
 */
@Service
@Slf4j
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

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ExecutorService pool;

    /**
     * 创建elasticsearch商品索引库
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
        goods.setSort(spuVo.getSort());
        goods.setCreateTime(spuVo.getCreateTime());
        goods.setUpdateTime(spuVo.getUpdateTime());
        AtomicInteger sold = new AtomicInteger();
        Collection<Category> categories = this.categoryService.listByIds(Arrays.asList(spuVo.getCid1(), spuVo.getCid2(), spuVo.getCid3()));
        StringBuilder keyword = new StringBuilder(spuVo.getTitle());
        categories.forEach(item -> {
            keyword.append(" " + item.getName());
        });
        goods.setKeyword(keyword.toString());
        List<SkuVo> skuList = this.skuService.findSkuVoListBySpuId(spuVo.getId());
        if (CollectionUtils.isEmpty(skuList)) return null;
        skuList.forEach(sku -> {
            Integer num = sku.getSkuStock().getSold();
            sold.addAndGet(num);
        });
        goods.setSold(sold.get());
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
                value =  parse.get(baseParam.getId().toString()).toString();
            }
            specs.put(baseParam.getName(), value);

        }
        goods.setSpec(specs);
        return goods;
    }

    /**
     * 搜索商品
     *
     * @param goodsQuery
     * @return
     */
    public GoodsSearchVo search(GoodsQuery goodsQuery) {
        GoodsSearchVo goodsSearchVo = new GoodsSearchVo();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //关键词搜索
        BoolQueryBuilder boolQueryBuilder = null;
//        if (StringUtils.isNotBlank(goodsQuery.getKeyword())) {
        boolQueryBuilder = buildBoolQueryBuilder(goodsQuery);
        queryBuilder.withQuery(boolQueryBuilder);
//        }

        if (StringUtils.isNotBlank(goodsQuery.getOrder())) {
            //排序
            String order = goodsQuery.getOrder();
            String[] orderArr = order.split(",");
            for (String orderItem : orderArr) {
                String[] arr = orderItem.split("-");
                if (StringUtils.equals(arr[1], "asc")) {
                    queryBuilder.withSort(SortBuilders.fieldSort(arr[0]).order(SortOrder.ASC));
                } else {
                    queryBuilder.withSort(SortBuilders.fieldSort(arr[0]).order(SortOrder.DESC));
                }
            }
        }

        String categoryAggName = "goods_cat";
        Map<String, Object> where = goodsQuery.getWhere();
        //判断是否分类聚合
        boolean aggFlag = false;
        if (!CollectionUtils.isEmpty(where)) {
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                if (StringUtils.equals(entry.getKey(), "cat_id")) {
                    String value =entry.getValue().toString();
                    if (StringUtils.isNotBlank(value)) {
                        aggFlag = true;
                    }
                }
            }
        }
        if (aggFlag || StringUtils.isNotBlank(goodsQuery.getKeyword())) {
            //聚合查询
            queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        }

        //分页
        queryBuilder.withPageable(PageRequest.of(goodsQuery.getPage() - 1, goodsQuery.getLimit()));
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
        goodsSearchVo.setTotal_page(goodsPage.getTotalPages());
        goodsSearchVo.setLimit(goodsPage.getSize());
        goodsSearchVo.setPage(goodsPage.getNumber());
        goodsSearchVo.setList(goodsPage.getContent());
        if (aggFlag || StringUtils.isNotBlank(goodsQuery.getKeyword())) {
            goodsSearchVo.setKeyword(goodsQuery.getKeyword());
            //聚合
            Aggregation aggregation = goodsPage.getAggregation(categoryAggName);
            List<Map<String, Object>> categoryMap = categoryAggResult(aggregation);
            Map<String, Object> filter = new HashMap<>();
            filter.put("goods_cat", categoryMap);
            goodsSearchVo.setFilter(filter);
            //规格聚合查询
            if (categoryMap.size() == 1) {
                List<Map<String, Object>> spec = getSpecResult((Long) categoryMap.get(0).get("goods_cat_id"), boolQueryBuilder);
                goodsSearchVo.setSpec(spec);
            }
        }
        return goodsSearchVo;
    }

    /**
     * 构建布尔查询
     *
     * @param goodsQuery
     * @return
     */
    private BoolQueryBuilder buildBoolQueryBuilder(GoodsQuery goodsQuery) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(goodsQuery.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("keyword", goodsQuery.getKeyword()));
        }
        //属性过滤
        if (!CollectionUtils.isEmpty(goodsQuery.getSpecSearch())) {
            List<Map<String, Object>> specSearch = goodsQuery.getSpecSearch();
            specSearch.forEach(spec -> {
                for (Map.Entry<String, Object> entry : spec.entrySet()) {
                    String key = "spec." + entry.getKey() + ".keyword";
                    boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
                }

            });

        }
        //价格过滤
        if (!CollectionUtils.isEmpty(goodsQuery.getWhere())) {
            Map<String, Object> where = goodsQuery.getWhere();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                if (StringUtils.equals(entry.getKey(), "price_f")) {
                    String value =  entry.getValue().toString();
                    double priceF = NumberUtils.toDouble(value);
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(priceF));
                }
                if (StringUtils.equals(entry.getKey(), "price_t")) {
                    String value = entry.getValue().toString();
                    double priceT = NumberUtils.toDouble(value);
                    if (priceT > 0) {
                        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(priceT));
                    }

                }
                if (StringUtils.equals(entry.getKey(), "cat_id")) {
                    String value = entry.getValue().toString();
                    if (StringUtils.isNotBlank(value)) {
                        Long cid3 = NumberUtils.toLong(value);
                        boolQueryBuilder.filter(QueryBuilders.termQuery("cid3", cid3));
                    }
                }
            }
        }
        return boolQueryBuilder;
    }

    /**
     * 聚合所有搜索规格
     *
     * @param catId
     * @param keywordBuilder
     * @return
     */
    private List<Map<String, Object>> getSpecResult(Long catId, BoolQueryBuilder keywordBuilder) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        List<Map<String, Object>> result = new ArrayList<>();
        queryBuilder.withQuery(keywordBuilder);
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        List<BaseParam> baseParamList = this.baseParamService.list(new LambdaQueryWrapper<BaseParam>().eq(BaseParam::getCid, catId).eq(BaseParam::getSearching, true));
        List<SaleParam> saleParamList = this.saleParamService.list(new LambdaQueryWrapper<SaleParam>().eq(SaleParam::getCid3, catId).eq(SaleParam::getSearching, true));
        baseParamList.forEach(baseParam -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(baseParam.getName()).field("spec." + baseParam.getName() + ".keyword"));
        });
        saleParamList.forEach(saleParam -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(saleParam.getName()).field("spec." + saleParam.getName() + ".keyword"));
        });
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
        Aggregations aggregations = search.getAggregations();
        Map<String, Aggregation> stringAggregationMap = aggregations.asMap();
        for (Map.Entry<String, Aggregation> aggregationEntry : stringAggregationMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", aggregationEntry.getKey());
            StringTerms terms = (StringTerms) aggregationEntry.getValue();
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            List<Object> options = buckets.stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            map.put("options", options);
            result.add(map);
        }
        return result;
    }

    /**
     * 聚合分类
     *
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> categoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        List<Map<String, Object>> maps = buckets.stream().map(bucket -> {
            Map<String, Object> map = new HashMap<>();
            Category category = this.categoryService.getById(bucket.getKeyAsNumber().longValue());
            map.put("name", category.getName());
            map.put("goods_cat_id", category.getId());
            return map;
        }).collect(Collectors.toList());
        return maps;
    }

    /**
     * 根据排序查询商品
     *
     * @param sortBy
     * @return
     */
    public List<Goods> findGoods(String sortBy) {
        RList<Goods> rList = this.redissonClient.getList(MainCacheConstant.GOODS_SORT + sortBy);
        List<Goods> goodsList;
        if (rList.isEmpty()) {
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withPageable(PageRequest.of(0, 6));
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(SortOrder.DESC));
            Page<Goods> page = this.goodsRepository.search(queryBuilder.build());
            goodsList = page.getContent();
            rList.addAll(goodsList);
            log.info("获取到了缓存数据..........");
        } else {
            goodsList = rList.readAll();
        }
        return goodsList;
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

    /**
     * 获取商品详情
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public GoodsDetile getDetail(Long id) throws ExecutionException, InterruptedException {
        RBucket<GoodsDetile> bucket = this.redissonClient.getBucket(MainCacheConstant.GOODS_DETAIL + id);
        GoodsDetile goodsDetile;
        if (bucket.isExists()) {
            goodsDetile = bucket.get();
        } else {
            SpuVo spuVo = this.spuService.findSpuVoById(id);
            if (spuVo == null) return null;
            //异步编排开始
            CompletableFuture<List<SkuVo>> skuVoFuture = CompletableFuture.supplyAsync(() -> this.skuService.findSkuVoListBySpuId(id), pool);
            CompletableFuture<List<SaleParam>> saleParamsFuture =
                    CompletableFuture.supplyAsync(() -> this.saleParamService.list(new LambdaQueryWrapper<>(new SaleParam().setCid3(spuVo.getCid3()))), pool);
            CompletableFuture<List<BaseParam>> baseParamListFuture =
                    CompletableFuture.supplyAsync(() -> this.baseParamService.list(new LambdaQueryWrapper<>(new BaseParam().setCid(spuVo.getCid3()))), pool);
            CompletableFuture<Void> allOf = CompletableFuture.allOf(skuVoFuture, saleParamsFuture, baseParamListFuture);
            allOf.join();
            List<SkuVo> skuVoList = skuVoFuture.get();
            List<SaleParam> saleParams = saleParamsFuture.get();
            List<BaseParam> baseParamList = baseParamListFuture.get();
            //异步编排结束
            goodsDetile = new GoodsDetile();
            BeanUtils.copyProperties(spuVo, goodsDetile);
//        List<SkuVo> skuVoList = this.skuService.findSkuVoListBySpuId(id);
            if (CollectionUtils.isEmpty(skuVoList)) return null;
//        List<SaleParam> saleParams = this.saleParamService.list(new LambdaQueryWrapper<>(new SaleParam().setCid3(spuVo.getCid3())));
            Map<Long, String> saleMap = new HashMap<>();
            String saleParamStr = spuVo.getSpuDetail().getSaleParam();
            Map<String,Object> sale = (Map<String, Object>) JSON.parse(saleParamStr);
            Map<String,Object> spec = new HashMap<>();
            saleParams.forEach(saleParam -> {
                saleMap.put(saleParam.getId(), saleParam.getName());
                for (Map.Entry<String, Object> entry : sale.entrySet()) {
                    if(StringUtils.equals(entry.getKey(),saleParam.getId().toString())){
                        spec.put(saleParam.getName(),entry.getValue());
                    }
                }
            });

            List<String> bannerImage = new ArrayList<>();
            skuVoList.forEach(skuVo -> {
                bannerImage.add(skuVo.getImages());
                String ownParamJson = skuVo.getOwnParam();
                Map<String, Object> ownParam = (Map<String, Object>) JSON.parse(ownParamJson);
                Map<String, Object> newOwnParam = new HashMap<>();
                for (Map.Entry<String, Object> entry : ownParam.entrySet()) {
//                    Map<String, Set<Object>> spec = goodsDetile.getSpec();
                    String ke = entry.getKey();
                    long keyLone = NumberUtils.toLong(ke);
                    String key = saleMap.get(keyLone);
                    newOwnParam.put(key, entry.getValue());
//                    if (spec.get(key) != null) {
//                        spec.get(key).add(entry.getValue());
//                    } else {
//                        Set<Object> set = new HashSet<>();
//                        set.add(entry.getValue());
//                        spec.put(key, set);
//                    }
                }
                skuVo.setOwnParam(JSON.toJSONString(newOwnParam));

            });
            //基本属性
//        List<BaseParam> baseParamList = this.baseParamService.list(new LambdaQueryWrapper<>(new BaseParam().setCid(spuVo.getCid3())));
            String baseParamJson = spuVo.getSpuDetail().getBaseParam();
            Map<String, Object> baseParamMap = (Map<String, Object>) JSON.parse(baseParamJson);
            List<Map<String, Object>> base = baseParamList.stream().map(baseParam -> {
                Map<String, Object> map = new HashMap<>();
                Object o = baseParamMap.get(baseParam.getId().toString());
                map.put("name", baseParam.getName());
                if (baseParam.getNumeric()) {
                    String s = o.toString();
                    map.put("value", s + baseParam.getUnit());
                } else {
                    map.put("value", o);
                }

                return map;
            }).collect(Collectors.toList());
            goodsDetile.setSpec(spec);
            goodsDetile.setBaseParam(base);
            goodsDetile.setSkuList(skuVoList);
            goodsDetile.setBannerImage(bannerImage);
//            bucket.set(goodsDetile, MathUtil.randomInt(1, 5), TimeUnit.MINUTES);
        }
        return goodsDetile;
    }
}
