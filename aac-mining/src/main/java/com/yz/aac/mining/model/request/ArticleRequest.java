package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("文章发布基本信息")
public class ArticleRequest {

    @ApiModelProperty(value = "文章类型ID", position = 1, required = true)
    private Integer type;
    
    @ApiModelProperty(value = "文章标题", position = 2, required = true)
    private String title;
    
    @ApiModelProperty(value = "文章内容模块集合", position = 3, required = false)
    private List<ArticleTextRequest> articleTextRequestList;

}
