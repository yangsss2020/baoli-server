package com.baoli.main.pms.controller;

import com.baoli.common.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-15-11:06
 */
@RestController
@RequestMapping("common")
@Api(tags = "公共路由")
public class CommonController {
    @GetMapping("baoliconf")
    @ApiOperation("公共配置")
    public R getBaoliConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("shop_logo", "http://demo.jihainet.com/static/uploads/images/68/f6/1b/5cde803088e85.png");
        map.put("shop_name", "票信网");
        map.put("shop_desc", "shop_desc");
        map.put("image_max", 5);
        map.put("store_switch", 1);
        map.put("cate_style", 3);
        map.put("cate_type", 1);
        map.put("tocash_money_low", 1);
        map.put("tocash_money_rate", 1);
        map.put("point_switch", 1);
        map.put("statistics", "");
        List<String> list = new ArrayList<>();
        list.add("圆珠笔");
        list.add("文件夹");
        list.add("文件袋");
        map.put("recommend_keys", list);
        map.put("invoice_switch", 1);
        map.put("goods_stocks_warn", 5);
        map.put("shop_default_image", "http://demo.jihainet.com/static/uploads/images/d6/60/71/5cde803675475.png");
        map.put("shop_mobile", "18530801653");
        map.put("open_distribution", false);
        map.put("show_inviter", "");
        map.put("share_title", "优质好店邀您共享");
        map.put("share_desc", "");
        map.put("share_image", "https://demo.jihainet.com/static/poster/1/1-1b867dc28e4bd37e55eaf327b6722fea.jpg");
        map.put("about_article_id", 3);
        map.put("ent_id", 10519);
        map.put("user_agreement_id", 6);
        map.put("privacy_policy_id", 7);
        return R.ok().data(map);
    }
}
