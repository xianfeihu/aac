package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.ArticleCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleCategoryRepository {

    String ALL_FIELDS = "id, name, create_time, readonly";
    String QUERY_CATEGORIES = "<script>SELECT " + ALL_FIELDS + " FROM article_category"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"accurateName != null and accurateName != ''\"> AND name = #{accurateName}</if>"
            + "</where>"
            + "ORDER BY create_time ASC"
            + "</script>";
    String STORE_CATEGORY = "INSERT INTO article_category(" + ALL_FIELDS + ") VALUES(#{id}, #{name}, #{createTime}, #{readonly})";
    String DELETE_CATEGORY = "DELETE FROM article_category WHERE id = #{id}";

    @Select(QUERY_CATEGORIES)
    List<ArticleCategory> query(ArticleCategory condition);

    @Insert(STORE_CATEGORY)
    void store(ArticleCategory category);

    @Delete(DELETE_CATEGORY)
    void delete(Long id);

}
