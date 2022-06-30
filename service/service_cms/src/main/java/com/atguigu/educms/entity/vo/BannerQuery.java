package com.atguigu.educms.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BannerQuery {
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "链接地址")
    private String linkUrl;

    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
}
