package com.yz.aac.mining.model.response;

import com.yz.aac.mining.repository.domian.ArticleCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("获取文章发布元素请求响应对象")
public class ArticlePublishElementsResponse {

    @ApiModelProperty(value = "文章类型集合", position = 1)
    private List<ArticleCategory> ArticleTypeList;
    
    @ApiModelProperty(value = "文章最少字数", position = 2)
    private Integer articleMinLength;

}
