package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.CreateArticleCategoryRequest;
import com.yz.aac.opadmin.model.request.CreateArticleRequest;
import com.yz.aac.opadmin.model.request.QueryArticleParticipatorRequest;
import com.yz.aac.opadmin.model.request.QueryArticleRequest;
import com.yz.aac.opadmin.model.response.QueryArticleParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryArticleResponse;
import com.yz.aac.opadmin.repository.domain.ArticleCategory;

import java.util.List;

public interface ArticleService {

    /**
     * 查询文章分类
     */
    List<ArticleCategory> queryCategories() throws Exception;

    /**
     * 创建文章分类
     */
    void createCategory(CreateArticleCategoryRequest request) throws Exception;

    /**
     * 删除文章分类
     */
    void deleteCategory(Long id) throws Exception;

    /**
     * 创建文章
     */
    void createArticle(CreateArticleRequest request) throws Exception;

    /**
     * 删除文章
     */
    void deleteArticle(Long id) throws Exception;

    /**
     * 查询文章
     */
    QueryArticleResponse queryArticles(QueryArticleRequest request) throws Exception;

    /**
     * 查询参与用户
     */
    QueryArticleParticipatorResponse queryParticipators(QueryArticleParticipatorRequest request) throws Exception;

}
