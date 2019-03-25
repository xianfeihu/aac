package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章元素
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleElement {

    /** 文章元素ID */
    private Long id;

    /**  文章ID */
    private Long articleId;

    /** 元素类型 （1-文字 2-视频 3-图片）*/
    private Integer elementType;

    /** 元素内容 */
    private String elementContent;

    /** 排序号 */
    private Integer orderNumber;

}
