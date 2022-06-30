package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.vo.CommentInfoVo;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-24
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {
    @Override
    public List<EduComment> getChildCommentList(List<EduComment> list, String pid) {

        List<EduComment> childList=new ArrayList<>();
        for (EduComment eduComment : list) {
            if (eduComment.getPid() != null) {
                //遍历出父id等于参数的id，add进子节点集合
                if (eduComment.getPid() == pid) {
                    //递归遍历下一级
                    getChildCommentList(list, eduComment.getId());
                    childList.add(eduComment);
                }
            }
        }
        return childList;
    }

    @Override
    public int updateCommentInfo(CommentInfoVo commentInfoVo) {
        EduComment eduComment= new EduComment();
        BeanUtils.copyProperties(commentInfoVo,eduComment);
        int update = baseMapper.updateById(eduComment);
        if(update == 0) {
            throw new GuliException(20001,"修改评论信息失败");
        }
        return update;
    }
}
