package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.ArticleElement;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleElementRepository {

    String SAVE_ARTICLE_ELEMENT = "INSERT INTO article_element(article_id, element_type, element_content, order_number) "
            + "VALUES(#{articleId}, #{elementType}, #{elementContent}, #{orderNumber})";

    String QUERY_ARTICLE_ELEMENT_BY_ARTICLEID = "SELECT * FROM article_element WHERE article_id = #{articleId} ORDER BY order_number";

    String QUERY_ARTICLE_ELEMENT_FIRST = "<script>"
            + "SELECT * FROM article_element WHERE article_id = #{articleId} "
            + "<if test=\"elementTypes != null and elementTypes.length > 0\"> "
            + "AND element_type IN "
            + "<foreach collection='elementTypes' item='et' open='(' close=')' separator=','>"
            + "#{et}"
            + "</foreach>"
            + "</if>"
            + " ORDER BY order_number limit 1"
            + "</script>";


    @Insert(SAVE_ARTICLE_ELEMENT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveArticleElement(ArticleElement articleElement);

    @Select(QUERY_ARTICLE_ELEMENT_BY_ARTICLEID)
    List<ArticleElement> getArticleElementByArticleId(@Param("articleId") Long articleId);

    @Select(QUERY_ARTICLE_ELEMENT_FIRST)
    ArticleElement getArticleElementFirst(@Param("articleId") Long articleId, @Param("elementTypes") Integer[] elementTypes);

}
