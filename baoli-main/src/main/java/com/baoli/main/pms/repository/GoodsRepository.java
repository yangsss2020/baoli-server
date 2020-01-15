package com.baoli.main.pms.repository;

import com.baoli.main.vo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ys
 * @create 2020-01-13-11:11
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
