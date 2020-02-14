package com.baoli.ucenter.ums.service;

import com.baoli.ums.entity.ChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
public interface ChatRecordService extends IService<ChatRecord> {

    List<ChatRecord> findByUserAndFriendId(String userId, String friendId);
    void readMessage(String userId,String friendId);
}
