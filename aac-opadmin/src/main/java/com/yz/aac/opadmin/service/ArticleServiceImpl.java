package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryArticleParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryArticleResponse;
import com.yz.aac.opadmin.repository.ArticleCategoryRepository;
import com.yz.aac.opadmin.repository.ArticleElementRepository;
import com.yz.aac.opadmin.repository.ArticleInteractionRepository;
import com.yz.aac.opadmin.repository.ArticleRepository;
import com.yz.aac.opadmin.repository.domain.Article;
import com.yz.aac.opadmin.repository.domain.ArticleCategory;
import com.yz.aac.opadmin.repository.domain.ArticleElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.ArticleAuthorType.OPERATOR;
import static com.yz.aac.opadmin.Constants.ArticleElement.*;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleElementRepository articleElementRepository;

    @Autowired
    private ArticleInteractionRepository articleInteractionRepository;

    @Autowired
    private FileStorageHandler fileStorageHandler;

    @Override
    public List<ArticleCategory> queryCategories() {
        return articleCategoryRepository.query(new ArticleCategory());
    }

    @Override
    public void createCategory(CreateArticleCategoryRequest request) throws Exception {
        ArticleCategory category = new ArticleCategory(null, request.getName().trim(), request.getName().trim(), System.currentTimeMillis(), NO.value());
        if (!articleCategoryRepository.query(category).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        int limit = 20;
        if (queryCategories().size() >= limit) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ARTICLE_CATEGORY_LIMIT.value().replaceFirst(PLACEHOLDER.value(), String.valueOf(limit)));
        }
        articleCategoryRepository.store(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) throws Exception {
        ArticleCategory category = new ArticleCategory(id, null, null, null, null);
        List<ArticleCategory> categories = articleCategoryRepository.query(category);
        if (categories.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        category = categories.iterator().next();
        if (category.getReadonly() == YES.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), DELETE_DEFAULT_DENY.value());
        }
        List<Long> articleIds = articleRepository.queryId(new Article(null, id, null, null, null, null));
        for (Long articleId : articleIds) {
            deleteArticle(articleId);
        }
        articleCategoryRepository.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createArticle(CreateArticleRequest request) throws Exception {
        //validate
        if (articleCategoryRepository.query(new ArticleCategory(request.getCategoryId(), null, null, null, null)).isEmpty()) {
            throw new BusinessException(
                    MSG_CUSTOMIZED_EXCEPTION.code(),
                    INVALID_ARTICLE_CATEGORY_ID.value().replaceFirst(PLACEHOLDER.value(), String.valueOf(request.getCategoryId()))
            );
        }
        //store article
        Article article = new Article(
                null,
                request.getCategoryId(),
                request.getTitle().trim(),
                request.getLoginUserId(),
                OPERATOR.value(),
                System.currentTimeMillis()
        );
        articleRepository.store(article);
        //store elements
        int textIndex = -1, fileIndex = -1;
        for (int i = 0; i < request.getIndexes().length; i++) {
            String elementContent = null;
            int elementType = request.getIndexes()[i];
            if (elementType == TEXT.value()) {
                textIndex++;
                elementContent = request.getTexts()[textIndex].trim();
            } else if (elementType == VIDEO.value() || elementType == IMAGE.value()) {
                fileIndex++;
                UploadRequest uploadRequest = request.getFiles()[fileIndex];
                elementContent = fileStorageHandler.uploadFile(uploadRequest.getContent(), uploadRequest.getExtName());
            }
            articleElementRepository.store(new ArticleElement(null, article.getId(), elementType, elementContent, i + 1));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long id) throws Exception {
        //validate
        if (articleRepository.queryCount(new Article(id, null, null, null, null, null)) == 0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_ID.value());
        }
        //delete file
        List<ArticleElement> fileList = articleElementRepository.query(
                new ArticleElement(null, id, null, null, null)
        )
                .stream()
                .filter(x -> x.getElementType() != TEXT.value())
                .collect(Collectors.toList());
        for (ArticleElement x : fileList) {
            fileStorageHandler.deleteFile(x.getElementContent());
        }
        //delete elements
        articleElementRepository.delete(new ArticleElement(null, id, null, null, null));
        //delete article
        articleRepository.delete(new Article(id, null, null, null, null, null));
    }

    @Override
    public QueryArticleResponse queryArticles(QueryArticleRequest request) {
        Article param = new Article();
        param.setTitle(StringUtils.isBlank(request.getTitle()) ? null : request.getTitle().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryArticleResponse.Item> page = (Page<QueryArticleResponse.Item>) articleRepository.query(param);
        return new QueryArticleResponse(page.getTotal(), page);
    }

    @Override
    public QueryArticleParticipatorResponse queryParticipators(QueryArticleParticipatorRequest request) throws Exception {
        Long userId;
        try {
            userId = StringUtils.isBlank(request.getCode()) ? null : Long.parseLong(request.getCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        request.setId(userId);
        request.setName(StringUtils.isBlank(request.getName()) ? null : request.getName().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryArticleParticipatorResponse.Item> page = (Page<QueryArticleParticipatorResponse.Item>) articleInteractionRepository.queryParticipators(request);
        List<QueryArticleParticipatorResponse.Item> items = page.getResult().stream()
                .map(x -> new QueryArticleParticipatorResponse.Item(
                        USER_CODE_PREFIX.value() + x.getUserCode(),
                        x.getUserName(),
                        x.getPublishCount(),
                        x.getReadingCount(),
                        x.getCommentCount(),
                        x.getBonus()
                )).collect(Collectors.toList());
        return new QueryArticleParticipatorResponse(page.getTotal(), items);
    }
}
