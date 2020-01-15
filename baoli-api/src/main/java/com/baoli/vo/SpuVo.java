package com.baoli.vo;

import com.baoli.pms.entity.Spu;
import com.baoli.pms.entity.SpuDetail;
import lombok.Data;

/**
 * @author ys
 * @create 2020-01-10-1:36
 */
@Data
public class SpuVo extends Spu {
    private SpuDetail spuDetail;
}
