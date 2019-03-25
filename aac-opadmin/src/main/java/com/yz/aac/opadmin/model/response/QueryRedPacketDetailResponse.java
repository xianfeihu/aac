package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@ApiModel("查询红包详情响应")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QueryRedPacketDetailResponse {

    @ApiModelProperty(value = "编号", position = 1, required = true)
    private Long id;

    @ApiModelProperty(value = "发布者", position = 2, required = true)
    private String name;

    @ApiModelProperty(value = "发布时间", position = 3, required = true)
    private Long issuanceTime;

    @ApiModelProperty(value = "发布地点", position = 4, required = true)
    private String location;

    @ApiModelProperty(value = "范围（单位：公里）", position = 5, required = true)
    private Integer radius;

    @ApiModelProperty(value = "红包个数", position = 6, required = true)
    private Integer dividingNumber;

    @ApiModelProperty(value = "总金额", position = 7, required = true)
    private BigDecimal currencyAmount;

    @ApiModelProperty(value = "发送群体（1-男；2-女；null-不限）", position = 8, required = true)
    private Integer grabbingLimit;

    @ApiModelProperty(value = "领取人数", position = 9, required = true)
    private Integer grabberCount;

    @ApiModelProperty(value = "点赞数", position = 10, required = true)
    private Long likeCount;

    @ApiModelProperty(value = "评论数", position = 11, required = true)
    private Long commentCount;

    @ApiModelProperty(value = "链接点击人数", position = 12, required = true)
    private Long clickLinkCount;

    @ApiModelProperty(value = "首图URL", position = 13, required = true)
    private String primaryImageUrl;

    @ApiModelProperty(value = "描述信息", position = 14)
    private String description;

    @ApiModelProperty(value = "链接标题", position = 15)
    private String linkTitle;

    @ApiModelProperty(value = "链接URL", position = 16)
    private String linkUrl;
}
