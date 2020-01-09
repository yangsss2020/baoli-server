package com.baoli.manage.pms.service.impl;

import com.baoli.pms.entity.Comment;
import com.baoli.manage.pms.mapper.CommentMapper;
import com.baoli.manage.pms.service.CommentService;
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
