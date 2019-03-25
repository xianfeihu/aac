package com.yz.aac.opadmin.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel("查询文章响应")
public class QueryArticleResponse {

    @ApiModelProperty(value = "总数据量", position = 1, required = true)
    private Long totalSize;

    @ApiModelProperty(value = "分页数据明细", position = 2, required = true)
    private List<Item> items;

    @ApiModel("查询文章响应分页数据")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Item {
        @ApiModelProperty(value = "ID", position = 1, required = true)
        private Long id;

        @ApiModelProperty(value = "发布时间", position = 2, required = true)
        private Long createTime;

        @ApiModelProperty(value = "类型", position = 3, required = true)
        private String categoryName;

        @ApiModelProperty(value = "标题", position = 4, required = true)
        private String title;

        @ApiModelProperty(value = "发布者", position = 5, required = true)
        private String authorName;

        @ApiModelProperty(value = "阅读人数", position = 6, required = true)
        private Long readers;

        @ApiModelProperty(value = "评论人数", position = 7, required = true)
        private Long commentators;
    }

}
