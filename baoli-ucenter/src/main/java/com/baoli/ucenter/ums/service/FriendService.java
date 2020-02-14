package com.baoli.ucenter.ums.service;

import com.baoli.ums.entity.Friend;
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
public interface FriendService extends IService<Friend> {

    List<Friend> findFriends(String id);

    void addFriend(String userId, String phone);
}
