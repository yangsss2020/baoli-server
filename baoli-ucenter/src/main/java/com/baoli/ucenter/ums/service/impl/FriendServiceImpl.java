package com.baoli.ucenter.ums.service.impl;

import com.baoli.common.exception.BaoliException;
import com.baoli.ucenter.ums.mapper.ChatRecordMapper;
import com.baoli.ucenter.ums.mapper.MemberMapper;
import com.baoli.ums.entity.ChatRecord;
import com.baoli.ums.entity.Friend;
import com.baoli.ucenter.ums.mapper.FriendMapper;
import com.baoli.ucenter.ums.service.FriendService;
import com.baoli.ums.entity.Member;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
    @Autowired
    private ChatRecordMapper chatRecordMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Override
    public List<Friend> findFriends(String id) {

        Friend friend = new Friend();
        friend.setUserId(id);
        List<Friend> list = this.list(new LambdaQueryWrapper<>(friend));
        list.forEach(item->{
            ChatRecord chatRecord= this.chatRecordMapper.findLastByUserAndFriendId(id,item.getFriendId());
            if(chatRecord!=null){
                item.setLastMsg(chatRecord.getTextMsg());
                item.setHasRead(chatRecord.getHasRead());
                this.updateById(item);
            }
        });
        return list;
    }

    @Override
    @Transactional
    public void addFriend(String userId, String phone) {
        Member member = this.memberMapper.selectOne(new LambdaQueryWrapper<>(new Member().setPhone(phone)));

        if(member==null){
            throw new BaoliException("改用户不存在");
        }
        if(StringUtils.equals(userId,member.getId())){
            throw new BaoliException("您不能添加您自己为好友");
        }
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(member.getId());
        List<Friend> list = this.list(new LambdaQueryWrapper<>(friend));
        if(list.size()>0){
            throw new BaoliException("您已添加该好友");
        }
        friend.setRemarkName(member.getNickname());
        friend.setAvatar(member.getAvatar());
        this.save(friend);
        //将自己添加到对方好友
        Member memberMe = this.memberMapper.selectById(userId);
        Friend friendMe = new Friend();
        friendMe.setAvatar(memberMe.getAvatar());
        friendMe.setRemarkName(memberMe.getNickname());
        friendMe.setUserId(member.getId());
        friendMe.setFriendId(userId);
        this.save(friendMe);
    }
}
