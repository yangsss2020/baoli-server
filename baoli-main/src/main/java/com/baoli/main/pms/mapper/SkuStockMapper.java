package com.baoli.main.pms.mapper;

import com.baoli.pms.entity.SkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 库存表 Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    int stockDecrement(@Param("id") Long id,@Param("quantity") Integer quantity);
}
