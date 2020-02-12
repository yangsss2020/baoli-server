package com.baoli.ucenter.ums.controller;


import com.baoli.common.vo.R;
import com.baoli.ucenter.ums.service.ChatRecordService;
import com.baoli.ucenter.ums.service.FriendReqService;
import com.baoli.ucenter.ums.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
@RestController
@RequestMapping("/ums/friend")
public class FriendChatController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendReqService friendReqService;
    @Autowired
    private ChatRecordService chatRecordService;
    @PostMapping("findFriendList")
    public R findFriendList(){
        return null;
    }
}

