package com.baoli.ucenter.ums.mapper;

import com.baoli.ums.entity.ChatRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ys
 * @since 2020-02-13
 */
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {

    List<ChatRecord> findByUserAndFriendId(@Param("userId") String userId,@Param("friendId") String friendId);

    ChatRecord findLastByUserAndFriendId(@Param("userId") String id,@Param("friendId") String friendId);
}
