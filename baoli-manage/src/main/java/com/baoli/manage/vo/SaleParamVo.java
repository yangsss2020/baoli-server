package com.baoli.manage.vo;

import com.baoli.pms.entity.SaleParam;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ys
 * @create 2020-01-10-21:01
 */
@Data
public class SaleParamVo extends SaleParam {
   private Object size;
   private Set check = new HashSet();
}
