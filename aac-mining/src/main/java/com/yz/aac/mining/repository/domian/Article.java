package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    /** 文章ID */
    private Long id;

    /**  文章类型ID */
    private Integer categoryId;

    /** 文章标题 */
    private String title;

    /** 发布者用户ID */
    private Long authorId;

    /** 发布时间 */
    private Long createTime;

    /** 发布者分类（1-APP用户 2-运营人员） */
    private Integer authorType;

}
