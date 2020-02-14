package com.baoli.main.sms.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.main.sms.service.NoticeService;
import com.baoli.sms.entity.Notice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-01-15
 */
@RestController
@RequestMapping("/sms/notice")
@Api(tags = "公告管理")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @PostMapping("noticeInfo")
    @ApiOperation("根据id获取公告")
    public R noticeInfo(@RequestBody Map<String,Long> map){
        Notice notice = this.noticeService.getById(map.get("id"));
        if(notice==null){
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(notice);
    }
    @PostMapping("noticeList")
    @ApiOperation("获取全部公告")
    public R noticeList(){
        List<Notice> list = this.noticeService.list(null);
        if(CollectionUtils.isEmpty(list)){
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(list);
    }
}

