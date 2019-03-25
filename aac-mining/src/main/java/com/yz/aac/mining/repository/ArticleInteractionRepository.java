package com.yz.aac.mining.repository;

import com.yz.aac.mining.model.response.ArticleCommentResponse;
import com.yz.aac.mining.repository.domian.ArticleInteraction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleInteractionRepository {

    String SAVE_ARTICLE_INTERACTION = "INSERT INTO article_interaction(article_id, parent_id, action, action_description, action_user_id, action_time) "
            + "VALUES(#{articleId}, #{parentId}, #{action}, #{actionDescription}, #{actionUserId}, #{actionTime})";

    String QUERY_ARTICLE_INTERACTION_BY_USERID = "<script>"
            + "SELECT * FROM article_interaction WHERE article_id = #{articleId} AND action = #{action} "
            + "<choose>"
            + "<when test = \"parentId != null\"> AND parent_id = #{parentId} </when>"
            + "<otherwise>  AND parent_id IS NULL </otherwise>"
            + "</choose>"
            + "AND action_user_id = #{userId} "
            + "</script>";

    String QUERY_ARTICLE_INTERACTION = "SELECT * FROM article_interaction WHERE article_id = #{articleId} AND action = #{action} AND parent_id IS NULL";

    String QUERY_ARTICLE_INTERACTION_BY_PARENT = "SELECT * FROM article_interaction WHERE article_id = #{articleId} AND action = #{action} AND parent_id = #{parentId}";

    String QUERY_COMMENT = "select ai.id as commentId,u.`name`,ai.action_description as description,ai.action_time as actionTime, tableB.likes,tableB.isLikes from ("
            + "select tableA.id, count(tableA.aiaId) as likes,min(tableA.isLikes) as isLikes from ("
            + "SELECT  ai.id, aia.id as aiaId,"
            + "case when aia.action_user_id = #{userId} then 1 else 2 end as isLikes "
            + "FROM article_interaction ai "
            + "left join article_interaction aia on (aia.parent_id = ai.id and aia.action = #{likes}) "
            + "where ai.action = #{action} and ai.article_id = #{articleId} and ai.parent_id is null "
            + ") as tableA "
            + "GROUP BY tableA.id "
            + ") as tableB "
            + "left join article_interaction ai on ai.id = tableB.id "
            + "left join `user` u on u.id = ai.action_user_id "
            + "ORDER BY tableB.likes desc";

    String QUERY_ARTICLE_INTERACTION_USERID = "SELECT action_user_id FROM article_interaction WHERE article_id = #{articleId} AND action = #{action} AND parent_id IS NULL GROUP BY action_user_id";

    @Insert(SAVE_ARTICLE_INTERACTION)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveArticleInteraction(ArticleInteraction articleInteraction);

    @Select(QUERY_ARTICLE_INTERACTION_BY_USERID)
    ArticleInteraction getArticleInteractionByUserId(@Param("articleId") Long articleId, @Param("action") Integer action,
                                                      @Param("userId") Long userId, @Param("parentId") Long parentId);

    /**
     * 获得最顶层互动
     * @param articleId
     * @param action
     * @return
     */
    @Select(QUERY_ARTICLE_INTERACTION)
    List<ArticleInteraction> getArticleInteraction(@Param("articleId") Long articleId, @Param("action") Integer action);

    /**
     * 获得非顶级互动
     * @param articleId
     * @param action
     * @param parentId
     * @return
     */
    @Select(QUERY_ARTICLE_INTERACTION_BY_PARENT)
    List<ArticleInteraction> getArticleInteractionByParent(@Param("articleId") Long articleId, @Param("action") Integer action,@Param("parentId") Long parentId);

    /***
     * 获取顶级评论（包含总点赞数，和当前用户是否点赞标记）
     * @param articleId
     * @param action
     * @param userId
     * @param likes
     * @return
     */
    @Select(QUERY_COMMENT)
    List<ArticleCommentResponse> getComment(@Param("articleId") Long articleId, @Param("action") Integer action,
                                            @Param("userId") Long userId, @Param("likes") Integer likes);

    /**
     * 获取文章品论数
     * @param articleId
     * @param action
     * @return
     */
    @Select(QUERY_ARTICLE_INTERACTION_USERID)
    List<Long> getArticleInteractionUserId(@Param("articleId") Long articleId, @Param("action") Integer action);

}
