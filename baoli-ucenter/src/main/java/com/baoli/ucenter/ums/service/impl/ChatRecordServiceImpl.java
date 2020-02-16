package com.baoli.ucenter.ums.service.impl;

import com.baoli.ucenter.ums.mapper.ChatRecordMapper;
import com.baoli.ucenter.ums.service.ChatRecordService;
import com.baoli.ums.entity.ChatRecord;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {
    @Autowired
    private ChatRecordMapper chatRecordMapper;

    @Override
    public List<ChatRecord> findByUserAndFriendId(String userId, String friendId) {
        List<ChatRecord> list = this.chatRecordMapper.findByUserAndFriendId(userId, friendId);
        return list;
    }

    @Override
    @Transactional
    public void readMessage(String userId, String friendId) {
        LambdaUpdateWrapper<ChatRecord> updateWrapper = new UpdateWrapper<ChatRecord>().lambda();
        updateWrapper
                .eq(ChatRecord::getUserId,friendId)
                .eq(ChatRecord::getFriendId,userId)
                .eq(ChatRecord::getHasRead,false);
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setHasRead(true);
        this.update(chatRecord,updateWrapper);
    }
}
