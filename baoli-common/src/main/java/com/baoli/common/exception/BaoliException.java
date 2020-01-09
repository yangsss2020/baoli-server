package com.baoli.common.exception;

import com.baoli.common.constans.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ys
 * @create 2020-01-09-1:27
 */
@Data
@ApiModel("全局异常类")
public class BaoliException extends RuntimeException {
    @ApiModelProperty("状态码")
    private Integer code;

    public BaoliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BaoliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "BaoliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
