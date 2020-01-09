package com.baoli.main.pms.service.impl;

import com.baoli.pms.entity.Comment;
import com.baoli.main.pms.mapper.CommentMapper;
import com.baoli.main.pms.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论列表 服务实现类
 * </p>
 *
 * @author ys
 * @since 2020-01-08
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
