package com.baoli.ucenter.client;

import com.baoli.common.api.pms.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ys
 * @create 2020-01-21-12:29
 */
@FeignClient("main-service")
public interface SkuClient extends SkuApi {
}
