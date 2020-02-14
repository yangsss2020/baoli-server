package com.baoli.main;

import com.baoli.main.pms.repository.GoodsRepository;
import com.baoli.main.pms.service.GoodsService;
import com.baoli.main.pms.service.SkuService;
import com.baoli.main.pms.service.SpuService;
import com.baoli.main.vo.Goods;
import com.baoli.pms.entity.Sku;
import com.baoli.vo.SpuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ys
 * @create 2020-01-12-22:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ElasticSearchTest {
    @Autowired
    private SpuService spuService;
    @Autowired
    private GoodsRepository GoodsRepository;
    @Autowired
    private SkuService skuService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsService goodsService;

    @Test
    public void testChangeSpu() {
        this.skuService.changeDB();
    }

    @Test
    public void testInsert() {
//        elasticsearchTemplate.createIndex(Goods.class);
//        elasticsearchTemplate.putMapping(Goods.class);
//        List<Spu> list = this.spuService.list(null);
//        list.forEach(item -> {
//            Goods goodsT = new Goods();
//            BeanUtils.copyProperties(item, goodsT);
//            this.goodsTRepository.save(goodsT);
//        });
    }

    @Test
    public void testSor() {
        List<Sku> list = this.skuService.list(new LambdaQueryWrapper<Sku>().eq(Sku::getSpuId, 3l).orderByAsc(Sku::getPrice));
        System.out.println(list);
    }

    @Test
    public void testBuildOne() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
        SpuVo spu = this.spuService.findSpuVoById(6);
        Goods goods = this.goodsService.buildGoods(spu);
        GoodsRepository.save(goods);
    }

    @Test
    public void testBuildGoods() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
        List<SpuVo> list = this.spuService.findSpuVo();
        list.forEach(spuVo -> {
            Goods goods = this.goodsService.buildGoods(spuVo);
            GoodsRepository.save(goods);
        });

    }

    @Test
    public void testSearch() {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("keyword", "小米");
        Iterable<Goods> goods = this.GoodsRepository.search(queryBuilder);
        goods.forEach(System.out::println);
    }
}
