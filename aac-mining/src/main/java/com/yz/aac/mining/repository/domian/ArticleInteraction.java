package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章互动
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleInteraction {

    /** ID */
    private Long id;

    /**  文章ID */
    private Long articleId;

    /** 父级互动ID */
    private Long parentId;

    /** 互动类型 （1-评论 2-点赞 3-阅读）*/
    private Integer action;

    /** 互动描述信息（评论内容） */
    private String actionDescription;

    /** 互动用户ID */
    private Long actionUserId;

    /** 互动时间 */
    private Long actionTime;

}
