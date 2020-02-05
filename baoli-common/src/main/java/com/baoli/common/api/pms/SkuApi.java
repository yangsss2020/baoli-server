package com.baoli.common.api.pms;

import com.baoli.common.vo.R;
import com.baoli.pms.entity.SkuStock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ys
 * @create 2020-01-21-12:33
 */
@RequestMapping("/pms/sku")
public interface SkuApi {
    @GetMapping("{id}")
    R findSkuVoBySkuId(@PathVariable Long id);

    @GetMapping("stock/{id}")
    SkuStock findSkuStockBySkuId(@PathVariable Long id);

    @GetMapping("stock/decrement/{id}/{quantity}/{spuId}")
    R stockDecrement(@PathVariable Long id, @PathVariable Integer quantity,@PathVariable long spuId);
}
