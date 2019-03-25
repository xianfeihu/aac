package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文章集合")
public class ArticleListResponse {

    @ApiModelProperty(value = "文章ID", position = 1)
    private Long articleId;

    @ApiModelProperty(value = "标题", position = 2)
    private String title;
    
    @ApiModelProperty(value = "作者", position = 3)
    private String author;

    @ApiModelProperty(value = "评论数量", position = 4)
    private Integer commentNumber;

    @ApiModelProperty(value = "时间字符串", position = 5)
    private String timeStr;

    @ApiModelProperty(value = "头图/头视频url", position = 6)
    private String headDiagramUrl;

    @ApiModelProperty(value = "是否自己发布（1-是 2-否）", position = 7)
    private Integer isMyself;

}
