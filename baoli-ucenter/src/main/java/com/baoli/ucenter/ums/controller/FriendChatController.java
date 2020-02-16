package com.baoli.ucenter.ums.controller;


import com.baoli.common.constans.ResultCodeEnum;
import com.baoli.common.vo.R;
import com.baoli.ucenter.interceptor.LoginInterceptor;
import com.baoli.ucenter.ums.service.ChatRecordService;
import com.baoli.ucenter.ums.service.FriendReqService;
import com.baoli.ucenter.ums.service.FriendService;
import com.baoli.ums.entity.ChatRecord;
import com.baoli.ums.entity.Friend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
@Api(tags = "好友")
public class FriendChatController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendReqService friendReqService;
    @Autowired
    private ChatRecordService chatRecordService;
    @PostMapping("findFriendList")
    @ApiOperation("查询朋友列表")
    public R findFriendList(){
        String id = LoginInterceptor.getMember().getId();
        List<Friend> list= this.friendService.findFriends(id);
        if(CollectionUtils.isEmpty(list)){
            return R.setResult(ResultCodeEnum.NOT_FOUND);
        }
        return R.ok().data(list);
    }

    @PostMapping("findRecord")
    @ApiOperation("查询聊天记录")
    public R findChatRecordById(@RequestBody Map<String,String> map){
        String userId = LoginInterceptor.getMember().getId();
       List<ChatRecord> chatRecordServiceList= this.chatRecordService.findByUserAndFriendId(userId,map.get("friendId"));
       if(CollectionUtils.isEmpty(chatRecordServiceList)){
           return R.setResult(ResultCodeEnum.NOT_FOUND);
       }
       return R.ok().data(chatRecordServiceList);

    }

    @PostMapping("readMessage")
    @ApiOperation("接收读取消息")
    public R readMessage(@RequestBody Map<String,String> map){
        this.chatRecordService.readMessage(map.get("userId"),map.get("friendId"));
        return R.ok();
    }
    @PostMapping("addFriend")
    @ApiOperation("添加好友")
    public R addFriend(@RequestBody Map<String,String> map){
        String userId = LoginInterceptor.getMember().getId();
        this.friendService.addFriend(userId,map.get("phone"));
        return R.ok();
    }
}

