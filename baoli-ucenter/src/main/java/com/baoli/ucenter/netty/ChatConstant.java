package com.baoli.ucenter.netty;

/**
 * @author ys
 * @create 2020-02-13-11:14
 */
public class ChatConstant {
    public static final int MSG_TYPE_CONN = 0; // 连接
    public static final int MSG_TYPE_SEND = 1; // 发送消息
    public static final int MSG_TYPE_REC = 2; // 签收
    public static final int MSG_TYPE_KEEPALIVE = 3; // 客户端保持心跳
    public static final int MSG_TYPE_RELOADFRIEND = 4; // 重新拉取好友
}
