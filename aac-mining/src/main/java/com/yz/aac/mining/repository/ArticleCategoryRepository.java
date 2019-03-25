package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.ArticleCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleCategoryRepository {

    String QUERY_ARTICLE_CATRGORY = "SELECT id ,`name` FROM article_category";

    @Select(QUERY_ARTICLE_CATRGORY)
    List<ArticleCategory> getArticleCategory();

}
