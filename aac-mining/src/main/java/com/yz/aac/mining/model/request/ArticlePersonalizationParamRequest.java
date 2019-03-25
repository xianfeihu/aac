package com.yz.aac.mining.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章个性化参数")
public class ArticlePersonalizationParamRequest {

    @ApiModelProperty(value = "文章ID", position = 1, required = true)
    private Long articleId;
    
    @ApiModelProperty(value = "个性化类型（1-关注 2-不想再看）",  position = 2, required = true)
    private Integer policy;

}
