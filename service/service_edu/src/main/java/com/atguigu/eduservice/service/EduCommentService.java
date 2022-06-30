package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.vo.CommentInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-24
 */
public interface EduCommentService extends IService<EduComment> {
    List<EduComment> getChildCommentList(List<EduComment> list, String pid);

    int updateCommentInfo(CommentInfoVo commentInfoVo);
}
