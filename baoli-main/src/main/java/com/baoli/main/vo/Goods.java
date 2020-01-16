package com.baoli.main.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-12-23:04
 */
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Field(type = FieldType.Keyword, index = false)
    private String title;
    @ApiModelProperty("可以被搜索的信息")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String keyword;
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Integer sort;
    @Field(type = FieldType.Keyword, index = false)
    private String images;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal memberPrice;
    private Integer sold;
    @ApiModelProperty(value = "排序")
    @Field(type = FieldType.Keyword, index = false)
    private String skus;
    private Map<String, Object> spec;
    private Date createTime;
    private Date updateTime;
}
