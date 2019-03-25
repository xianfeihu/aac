package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章发布文本对象")
public class ArticleTextRequest {

    @ApiModelProperty(value = "文章内容", position = 1, required = true)
    private String content;
    
    @ApiModelProperty(value = "文章内容排序号(排序号包含视频，图片一起计算的结果)", position = 2, required = true)
    private Integer orderNumber;

}
