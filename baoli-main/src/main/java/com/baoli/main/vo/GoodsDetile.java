package com.baoli.main.vo;

import com.baoli.pms.entity.Spu;
import com.baoli.pms.entity.SpuDetail;
import com.baoli.vo.SkuVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-16-18:41
 */
@Data
public class GoodsDetile extends Spu {
    private List<String> bannerImage;
    private Map<String,Object> spec;
    private SpuDetail spuDetail;
    private List<SkuVo> skuList;
}
