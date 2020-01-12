package com.baoli.manage.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ys
 * @create 2020-01-10-0:06
 */
@Data
public class SpuQuery implements Serializable {
    private Long cid3;
    private Boolean saleable;
    private String keywords;
    private Long page;
    private Long size;
    private List<String> createTime;
    private List<String> updateTime;

    public Long getPage() {
        if (page == null) {
            page = 1l;
        }
        return page;
    }

    public Long getSize() {
        if (size == null) {
            size = 15l;
        }
        return size;
    }
}
