package com.baoli.ucenter.vo;

import com.baoli.ums.entity.ChatRecord;
import lombok.Data;

/**
 * @author ys
 * @create 2020-02-13-10:45
 */
@Data
public class Message {
    private Integer type;
    private ChatRecord chatRecord;
    private Object ext; //扩展字段
}
