package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.model.response.QueryArticleResponse;
import com.yz.aac.opadmin.repository.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleRepository {

    String ALL_FIELDS = "id, category_id, title, author_id, author_type, create_time";

    String QUERY_COUNT = "<script>SELECT COUNT(id) FROM article"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"categoryId != null\"> AND category_id = #{categoryId}</if>"
            + "</where>"
            + "</script>";

    String QUERY_ARTICLE = "<script>SELECT a.id, a.create_time, ac.name AS categoryName, a.title,"
            + " CASE a.author_type WHEN 1 THEN u.name WHEN 2 THEN o.name END AS authorName,"
            + " (SELECT COUNT(DISTINCT ai.action_user_id) FROM article_interaction ai WHERE ai.article_id = a.id AND ai.parent_id IS NULL AND ai.action = 3) AS readers,"
            + " (SELECT COUNT(DISTINCT ai.action_user_id) FROM article_interaction ai WHERE ai.article_id = a.id AND ai.parent_id IS NULL AND ai.action = 1) AS commentators"
            + " FROM article a"
            + " INNER JOIN article_category ac ON a.category_id = ac.id"
            + " LEFT JOIN user u ON a.author_id = u.id AND a.author_type = 1"
            + " LEFT JOIN operator o ON a.author_id = o.id AND a.author_type = 2"
            + "<where>"
            + "<if test=\"title != null\"><bind name=\"fixedTitle\" value=\"'%' + title + '%'\" /> AND a.title LIKE #{fixedTitle}</if>"
            + "</where>"
            + " ORDER BY a.create_time DESC"
            + "</script>";

    String QUERY_ARTICLE_ID = "<script>SELECT a.id FROM article a"
            + "<where>"
            + "<if test=\"categoryId != null\"> AND a.category_id = #{categoryId}</if>"
            + "</where>"
            + "</script>";

    String STORE_ARTICLE = "INSERT INTO article(" + ALL_FIELDS + ") VALUES(#{id}, #{categoryId}, #{title}, #{authorId}, #{authorType}, #{createTime})";

    String DELETE_ARTICLE = "<script>DELETE FROM article"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "</where>"
            + "</script>";

    @Select(QUERY_COUNT)
    Long queryCount(Article condition);

    @Select(QUERY_ARTICLE)
    List<QueryArticleResponse.Item> query(Article condition);

    @Select(QUERY_ARTICLE_ID)
    List<Long> queryId(Article condition);

    @Insert(STORE_ARTICLE)
    @Options(useGeneratedKeys = true)
    void store(Article article);

    @Delete(DELETE_ARTICLE)
    void delete(Article article);
}
