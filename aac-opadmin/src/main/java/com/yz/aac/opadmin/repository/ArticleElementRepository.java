package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.ArticleElement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleElementRepository {

    String ALL_FIELDS = "id, article_id, element_type, element_content, order_number";

    String STORE = "INSERT INTO article_element(" + ALL_FIELDS + ") VALUES(#{id}, #{articleId}, #{elementType}, #{elementContent}, #{orderNumber})";

    String QUERY = "<script>SELECT " + ALL_FIELDS + " FROM article_element"
            + "<where>"
            + "<if test=\"articleId != null\"> AND article_id = #{articleId}</if>"
            + "</where>"
            + "</script>";

    String DELETE = "<script>DELETE FROM article_element"
            + "<where>"
            + "<if test=\"articleId != null\"> AND article_id = #{articleId}</if>"
            + "</where>"
            + "</script>";

    @Select(QUERY)
    List<ArticleElement> query(ArticleElement condition);

    @Insert(STORE)
    int store(ArticleElement element);

    @Delete(DELETE)
    void delete(ArticleElement element);
}
