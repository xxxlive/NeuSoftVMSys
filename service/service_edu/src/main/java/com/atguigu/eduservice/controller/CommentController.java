package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.CommentInfoVo;
import com.atguigu.eduservice.entity.vo.CommentQuery;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/eduservice/comment")
public class CommentController {
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
    //回复私信接口的方法
    @PostMapping("hfPrivateComment")
    public R hfPrivateComment(@RequestBody EduComment eduComment) {
        CommentInfoVo commentInfoVo=new CommentInfoVo();
        commentInfoVo.setId(eduComment.getPid());
        commentInfoVo.setStatus(1);
        boolean save = eduCommentService.save(eduComment);

        if(save) {
            int update=eduCommentService.updateCommentInfo(commentInfoVo);
            if(update!=0){
                return R.ok();
            }
            else{
                return R.error();
            }

        } else {
            return R.error();
        }
    }
    // 条件查询带分页的方法,查询所有私信
    @PostMapping("pageCommentCondition/{current}/{limit}")
    public R pageCommentCondition(@PathVariable long current,@PathVariable long limit,
                                  @RequestBody(required = false) CommentQuery commentQuery) {
        //创建page对象
        Page<EduComment> pageComment = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        //查询所有私信
        wrapper.eq("is_private",1);
        wrapper.eq("pid",0);
        // 多条件组合查询
        // mybatis学过 动态sql
        if(commentQuery!=null){
            //判断条件值是否为空，如果不为空拼接条件
            if(!StringUtils.isEmpty(commentQuery.getTeacherId())) {
                //构建条件
                wrapper.eq("teacher_id",commentQuery.getTeacherId());
            }
            if(!StringUtils.isEmpty(commentQuery.getCourseId())) {
                wrapper.eq("course_id",commentQuery.getCourseId());
            }
            if(!StringUtils.isEmpty(commentQuery.getStatus())) {
                wrapper.eq("status",commentQuery.getStatus());
            }
            if(!StringUtils.isEmpty(commentQuery.getMemberId())) {
                wrapper.eq("member_id",commentQuery.getMemberId());
            }
            if(!StringUtils.isEmpty(commentQuery.getBegin())) {
                wrapper.ge("gmt_create",commentQuery.getBegin());
            }
            if(!StringUtils.isEmpty(commentQuery.getEnd())) {
                wrapper.le("gmt_create",commentQuery.getEnd());
            }
        }


        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        eduCommentService.page(pageComment,wrapper);

        long total = pageComment.getTotal();//总记录数
        List<EduComment> records = pageComment.getRecords(); //数据list集合
        return R.ok().data("total",total).data("rows",records);
    }
    //查询所有私信，并包装成前端需要的格式返回
    @ApiOperation(value = "查询所有私信，并包装成前端需要的格式返回")
    @PostMapping("pageAllCommentCondition/{current}/{limit}")
    public R pageAllCommentCondition(@PathVariable long current,@PathVariable long limit,
                                     @RequestBody(required = false) CommentQuery commentQuery){
        //创建page对象
        Page<EduComment> pageComment = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        //查询所有私信
        wrapper.eq("is_private",1);
        // 多条件组合查询
        // mybatis学过 动态sql
        if(commentQuery!=null){
            //判断条件值是否为空，如果不为空拼接条件
            if(!StringUtils.isEmpty(commentQuery.getTeacherId())) {
                //构建条件
                wrapper.eq("teacher_id",commentQuery.getTeacherId());
            }
            if(!StringUtils.isEmpty(commentQuery.getCourseId())) {
                wrapper.eq("course_id",commentQuery.getCourseId());
            }
            if(!StringUtils.isEmpty(commentQuery.getStatus())) {
                wrapper.eq("status",commentQuery.getStatus());
            }
            if(!StringUtils.isEmpty(commentQuery.getBegin())) {
                wrapper.ge("gmt_create",commentQuery.getBegin());
            }
            if(!StringUtils.isEmpty(commentQuery.getEnd())) {
                wrapper.le("gmt_create",commentQuery.getEnd());
            }
        }


        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询分页
        eduCommentService.page(pageComment,wrapper);

        long total = pageComment.getTotal();//总记录数
        List<EduComment> records = pageComment.getRecords(); //数据list集合

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
        return R.ok().data("total",total).data("rows",eduComments);
    }
    //根据id进行查询
    @GetMapping("getComment/{id}")
    public R getComment(@PathVariable String id) {
        EduComment eduComment = eduCommentService.getById(id);
        return R.ok().data("comment",eduComment);
    }
    //根据id进行查询回复列表
    @GetMapping("getLookComment/{id}")
    public R getLookComment(@PathVariable String id) {
        //构建条件
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        List<EduComment> list = eduCommentService.list(wrapper);
        return R.ok().data("list",list);
    }
    //修改评论信息
    @PostMapping("updateCommentInfo")
    public R updateCommentInfo(@RequestBody CommentInfoVo commentInfoVo) {
        eduCommentService.updateCommentInfo(commentInfoVo);
        return R.ok();
    }
    // 逻辑删除评论的方法
    @ApiOperation(value = "逻辑删除评论")
    @DeleteMapping("{id}")
    public R removeComment(@ApiParam(name = "id", value = "讲师ID", required = true)
                           @PathVariable String id) {
        boolean flag = eduCommentService.removeById(id);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}
