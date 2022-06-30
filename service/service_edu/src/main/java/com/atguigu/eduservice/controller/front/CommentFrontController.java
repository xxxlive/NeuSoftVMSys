package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/eduservice/commentfront")
public class CommentFrontController {
    //把service注入
    @Autowired
    private EduCommentService eduCommentService;

    //1 查询评论表所有数据
    //rest风格
    @ApiOperation(value = "所有评论列表")
    @GetMapping("findAll")
    public R findAllComment() {
        //调用service的方法实现查询所有的操作
        List<EduComment> list = eduCommentService.list(null);
        return R.ok().data("items",list);
    }
    //根据课程id查询该课程的所有评论，并包装成前端需要的格式返回
    @ApiOperation(value = "根据课程id查询该课程的所有评论")
    @GetMapping("findAllCommentByCourseId/{courseId}")
    public R findAllCommentByCourseId(@ApiParam(name = "courseId", value = "课程id", required = true)
                                          @PathVariable String courseId){
        //1根据课程id查询该课程所有评论
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        //只显示公共评论，私信不显示
        wrapper.eq("is_private",0);
        //排序
        wrapper.orderByDesc("gmt_create");
        List<EduComment> list = eduCommentService.list(wrapper);

        //使用map遍历所有的节点
        List<EduComment> eduComments=new ArrayList<EduComment>();

        Map<String, EduComment> eduCommentMap = new HashMap<String, EduComment>();
        for (EduComment e : list) {
            eduCommentMap.put(e.getId(), e);
        }
        for ( EduComment e : list ) {
            EduComment child = e;
            if (Objects.equals(child.getPid(), "0")) {
                eduComments.add(e);
            } else {
                EduComment parent = eduCommentMap.get(child.getPid());
                parent.getChildren().add(child);
                parent.setCommentNum(parent.getCommentNum()+1);
            }
        }
        return R.ok().data("items",eduComments);
    }

    //添加评论接口的方法
    @PostMapping("addComment")
    public R addComment(@RequestBody EduComment eduComment) {
        boolean save = eduCommentService.save(eduComment);
        if(save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}
