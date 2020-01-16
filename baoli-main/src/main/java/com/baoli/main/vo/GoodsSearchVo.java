package com.baoli.main.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-15-18:52
 */
@Data
public class GoodsSearchVo implements Serializable {
    private Map<String,Object> filter = new HashMap<>();
    private List<Map<String,Object>> spec =new ArrayList<>();
    private Integer page;
    private Integer limit;
    private Integer total_page;
    private String order;
    private Map<String,Object> where;
    private List<Goods> list;
    private String keyword;
}
