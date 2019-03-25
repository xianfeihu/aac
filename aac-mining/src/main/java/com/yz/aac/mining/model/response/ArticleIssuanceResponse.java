package com.yz.aac.mining.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发布文章请求响应对象")
public class ArticleIssuanceResponse {

    @ApiModelProperty(value = "文章ID", position = 1)
    private Long articleId;

    @ApiModelProperty(value = "作者", position = 2)
    private String author;
    
    @ApiModelProperty(value = "文章标签", position = 3)
    private String articleLabel;

    @ApiModelProperty(value = "发布时间字符串", position = 4)
    private String timeStr;

    @ApiModelProperty(value = "标题", position = 5)
    private String title;

    @ApiModelProperty(value = "文章元素集合", position = 6)
    private List<ArticleElementResponse> articleElementList;

    @ApiModelProperty(value = "点赞数", position = 7)
    private Integer likes;

    @ApiModelProperty(value = "是否点赞（针对当前用户 1-是 2-否）", position = 8)
    private Integer isLikes;

    @ApiModelProperty(value = "是否关注（1-是 2-否）", position = 9)
    private Integer isFollow;

    @ApiModelProperty(value = "是否屏蔽（1-是 2-否）", position = 10)
    private Integer isShield;

    @ApiModelProperty(value = "随机文章列表", position = 11)
    private List<ArticleListResponse> randArticleList;

    @ApiModelProperty(value = "评论列表", position = 12)
    private List<ArticleCommentResponse> commentList;

    @ApiModelProperty(value = "阅读时长（秒）", position = 13)
    private Integer readingTime;

    @ApiModelProperty(value = "当日阅读奖励是否达到上限(1-是 2-否)", position = 14)
    private Integer isMaxRewardToday;

    public ArticleIssuanceResponse(Long articleId, String author, String articleLabel, String title) {
        this.articleId = articleId;
        this.author = author;
        this.articleLabel = articleLabel;
        this.title = title;
    }
}
