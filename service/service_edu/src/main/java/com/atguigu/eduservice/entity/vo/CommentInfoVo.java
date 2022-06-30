package com.atguigu.eduservice.entity.vo;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class CommentInfoVo {
    @ApiModelProperty(value = "评论ID")
    private String id;

    @ApiModelProperty(value = "所属上级id")
    private String pid;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "用户ID")
    private String memberId;

    @ApiModelProperty(value = "评论用户昵称")
    private String nickname;

    @ApiModelProperty(value = "评论用户头像")
    private String avatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "是否私信 1（true）私信， 0（false）公开")
    private Integer isPrivate;

    @ApiModelProperty(value = "状态 1已回复， 0未回复")
    private Integer status;
}
