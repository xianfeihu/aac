package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.ArticlePersonal;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticlePersonalRepository {

    String SAVE_ARTICLE_PERSONAL = "INSERT INTO article_personal(user_id, article_id, policy) "
            + "VALUES(#{userId}, #{articleId}, #{policy})";

    String QUERY_ARTICLE_PERSONAL = "SELECT * FROM article_personal WHERE user_id = #{userId} AND article_id = #{articleId} AND policy = #{policy}";

    @Insert(SAVE_ARTICLE_PERSONAL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveArticlePersonal(ArticlePersonal articlePersonal);

    @Select(QUERY_ARTICLE_PERSONAL)
    ArticlePersonal getArticlePersonal(@Param("userId") Long userId, @Param("articleId") Long articleId, @Param("policy") Integer policy);

}
