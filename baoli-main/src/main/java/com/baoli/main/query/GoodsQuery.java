package com.baoli.main.query;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-15-17:04
 */
@Data
public class GoodsQuery {
    private String keyword;
    private Integer page;
    private Integer limit;
    private String order;
    private Map<String, Object> where;
    private List<Map<String,Object>> specSearch;

    public Integer getPage() {
        if (page == null) {
            page = 1;
        }
        return page;
    }

    public Integer getLimit() {
        if (limit == null) {
            limit = 10;
        }
        return limit;
    }
}
