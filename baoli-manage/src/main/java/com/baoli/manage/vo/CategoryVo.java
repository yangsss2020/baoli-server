package com.baoli.manage.vo;

import com.baoli.pms.entity.Category;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ys
 * @create 2020-01-09-12:11
 */
@Data
@ApiModel("分类")
public class CategoryVo extends Category {
    List<CategoryVo> children = new ArrayList<>();
}
