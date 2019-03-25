package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleRepository {

    String SAVE_ARTICLE = "INSERT INTO article(category_id, title, author_id, create_time, author_type) "
            + "VALUES(#{categoryId}, #{title}, #{authorId}, #{createTime}, #{authorType})";

    String QUERY_ARTICLE_BY_ID = "SELECT * FROM article WHERE id = #{articleId}";

    String QUERY_ARTICLE_RAND = "SELECT * FROM article WHERE category_id = #{categoryId} order by rand() LIMIT #{randNumber}";

    String QUERY_ARTICLE_LIST = "<script>"
            + "select acle.* from ("
            + "select ale.id,count(ai.id) totalNum from article ale "
            + "left join article_interaction ai on (ai.article_id = ale.id and ai .action = #{readAction}) "
            + "left join article_personal ap on ap.article_id = ale.id "
            + "where ale.id not in (select article_id from article_personal where user_id=#{userId} and policy = #{notSee})"
            + "GROUP BY ale.id "
            + ") tableA "
            + "left join article acle on acle.id = tableA.id "
            + "<if test=\"tagType != null and tagType == recommendationType and totalNum != null\">where tableA.totalNum > #{totalNum}</if>"

            + "<if test=\"tagType != null and tagType == followType\">where acle.id in (select article_id from article_personal where user_id = #{userId} AND policy = #{policy}) or acle.author_id = #{userId}</if>"

            + "<if test=\"categoryId != null\">where acle.category_id = #{categoryId}</if>"

            + "ORDER BY totalNum desc, acle.create_time "
            + "</script>";

    @Insert(SAVE_ARTICLE)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveArticle(Article article);

    @Select(QUERY_ARTICLE_BY_ID)
    Article getArticleById(@Param("articleId") Long articleId);

    @Select(QUERY_ARTICLE_RAND)
    List<Article> getArticleRand(@Param("categoryId") Integer categoryId, @Param("randNumber") Integer randNumber);

    /**
     *
     * @param readAction 用于统计文章阅读数
     * @param notSee 不感兴趣文章类型（所有列表都要排除该类型）
     * @param tagType 文章类型(用于判断推荐文章或者关注文章类型)
     * @param totalNum 推荐列表阅读量
     * @param recommendationType 推荐文章类型
     * @param followType 关注文章类型
     * @param userId 用户ID
     * @param policy
     * @param categoryId 文章类型
     *
     * @return
     */
    @Select(QUERY_ARTICLE_LIST)
    List<Article> getArticleList(@Param("readAction") Integer readAction, @Param("notSee") Integer notSee
            , @Param("tagType") Integer tagType, @Param("totalNum") Integer totalNum
            , @Param("recommendationType") Integer recommendationType, @Param("followType") Integer followType
            , @Param("userId") Long userId, @Param("policy") Integer policy
            , @Param("categoryId") Integer categoryId);

}
