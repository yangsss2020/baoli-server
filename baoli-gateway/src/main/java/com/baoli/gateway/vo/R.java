package com.baoli.gateway.vo;

import com.baoli.common.constans.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ys
 * @create 2020-01-09-1:10
 */
@Data
public class R implements Serializable {
    private Integer code;
    private Boolean status;
    private String msg;
    private Object data;
    private String token;
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


}
