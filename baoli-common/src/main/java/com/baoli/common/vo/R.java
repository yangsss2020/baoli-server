package com.baoli.common.vo;

import com.baoli.common.constans.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ys
 * @create 2020-01-09-1:10
 */
@Data
@ApiModel("全局同意返回结果")
public class R {
    @ApiModelProperty("返回码")
    private Integer code;
    @ApiModelProperty("是否成功")
    private Boolean status;
    @ApiModelProperty("返回消息")
    private String msg;
    @ApiModelProperty("返回数据")
    private Object data;

    private R() {
    }

    public static R ok() {
        R r = new R();
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setStatus(ResultCodeEnum.SUCCESS.getSuccess());
        r.setMsg(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    public static R error() {
        R r = new R();
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setStatus(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
        r.setMsg(ResultCodeEnum.UNKNOWN_REASON.getMessage());
        return r;
    }

    public R data(Object data) {
        this.setData(data);
        return this;
    }

//    public R data(String key, Object value) {
//        this.data.put(key, value);
//        return this;
//    }

    public R message(String message) {
        this.setMsg(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R success(Boolean success) {
        this.setStatus(success);
        return this;
    }

    public static R setResult(ResultCodeEnum resultCodeEnum) {
        R r = new R();
        r.setCode(resultCodeEnum.getCode());
        r.setStatus(resultCodeEnum.getSuccess());
        r.setMsg(resultCodeEnum.getMessage());
        return r;
    }

    public static Map<String, Object> pageResult(Page page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("limit", page.getSize());
        map.put("total_page", page.getPages());
        map.put("page", page.getCurrent());
        map.put("list", page.getRecords());
        return map;
    }
}
