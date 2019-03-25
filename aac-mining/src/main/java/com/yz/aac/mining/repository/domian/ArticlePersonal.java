package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章个性信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePersonal {

    /** ID */
    private Long id;

    /**  用户ID */
    private Long userId;

    /** 文章ID */
    private Long articleId;

    /** 个性化策略(1-关注 2-不想看) */
    private Integer policy;

}
