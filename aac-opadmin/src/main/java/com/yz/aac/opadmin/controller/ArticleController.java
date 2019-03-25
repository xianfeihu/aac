package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.ImageUtil;
import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryArticleParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryArticleResponse;
import com.yz.aac.opadmin.model.response.QueryConfigResponse;
import com.yz.aac.opadmin.repository.domain.ArticleCategory;
import com.yz.aac.opadmin.service.ArticleService;
import com.yz.aac.opadmin.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INVALID_IMAGE;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.ArticleElement.*;
import static com.yz.aac.opadmin.Constants.Config.MIN_ARTICLE_LENGTH;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/articles")
@Slf4j
@Api(tags = "文章（app内容管理-阅读文章）")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ConfigService configService;

    @ApiOperation("查询文章分类")
    @GetMapping("/categories")
    @ResponseBody
    public RootResponse<List<ArticleCategory>> queryCategories() throws Exception {
        return buildSuccess(articleService.queryCategories());
    }

    @ApiOperation("创建文章分类")
    @PutMapping("/categories")
    @ResponseBody
    public RootResponse<?> createCategory(@RequestBody CreateArticleCategoryRequest request) throws Exception {
        validateRequired(NAME.value(), request.getName());
        validateStringLength(NAME.value(), request.getName(), 1, 5);
        validateSpecialChar(NAME.value(), request.getName());
        articleService.createCategory(request);
        return buildSuccess(null);
    }

    @ApiOperation("删除文章分类")
    @DeleteMapping("/categories/{id}")
    @ResponseBody
    public RootResponse<?> deleteCategory(
            @ApiParam(name = "id", value = "文章分类ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        articleService.deleteCategory(id);
        return buildSuccess(null);
    }

    @ApiOperation(value = "创建文章")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RootResponse<?> createArticle(
            @ApiParam(name = "categoryId", value = "文章分类ID", required = true) @RequestParam(value = "categoryId", required = false) Long categoryId,
            @ApiParam(name = "title", value = "文章标题", required = true) @RequestParam(value = "title", required = false) String title,
            @ApiParam(name = "texts", value = "文字数组") @RequestParam(value = "texts", required = false) String[] texts,
            @ApiParam(name = "files", value = "附件数组") @RequestParam(value = "files", required = false) MultipartFile[] files,
            @ApiParam(name = "indexes", value = "索引数组（1-文字；2-视频；3-图片）", required = true) @RequestParam(value = "indexes", required = false) Integer[] indexes
    ) throws Exception {
        //validate required
        validateRequired(CATEGORY_ID.value(), categoryId);
        validateRequired(TITLE.value(), title);
        if (null == indexes || indexes.length == 0) {
            validateRequired(INDEXES.value(), null);
        }
        //validate value scope
        Set<Integer> scope = new HashSet<>(Arrays.asList(TEXT.value(), VIDEO.value(), IMAGE.value()));
        for (int i = 0; i < indexes.length; i++) {
            validateValueScope(INDEXES.value() + "." + i, indexes[i], scope);
        }
        //validate size & length
        int maxElements = 10;
        if (indexes.length > maxElements) {
            throw new BusinessException(
                    MSG_CUSTOMIZED_EXCEPTION.code(),
                    ARTICLE_ELEMENTS_LIMIT.value().replaceFirst(PLACEHOLDER.value(), String.valueOf(maxElements))
            );
        }
        long textCount = Arrays.stream(indexes).filter(x -> x == TEXT.value()).count();
        long fileCount = Arrays.stream(indexes).filter(x -> x != TEXT.value()).count();
        //第一个元素必须是文字
        if (textCount == 0 || indexes[0] != TEXT.value()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), CONTENT_REQUIRED.value());
        }
        if (textCount > 0 && (null == texts || textCount != texts.length)) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_TEXTS_COUNT.value());
        }
        if (fileCount > 0 && (null == files || fileCount != files.length)) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_FILES_COUNT.value());
        }
        QueryConfigResponse config = configService.queryConfigs(new QueryConfigRequest(1, 1, MIN_ARTICLE_LENGTH.key())).iterator().next();
        int minTextLength = Integer.parseInt(config.getValue());
        for (int i = 0; i < texts.length; i++) {
            String fieldName = TEXTS.value() + "." + i;
            validateStringLength(fieldName, texts[i], minTextLength, i == 0 ? 1000 : 500);
        }
        validateStringLength(TITLE.value(), title, 20, 40);
        //validate format
        int fileIndex = -1;
        for (int index : indexes) {
            if (index == VIDEO.value() || index == IMAGE.value()) {
                fileIndex++;
            }
            if (index == IMAGE.value() && !ImageUtil.isImage(files[fileIndex].getInputStream())) {
                throw new BusinessException(MSG_INVALID_IMAGE.code(), MSG_INVALID_IMAGE.message().replaceFirst(PLACEHOLDER.value(), FILES.value() + "." + fileIndex));
            }
        }
        //submit
        UploadRequest[] uploadRequests = new UploadRequest[files.length];
        for (int i = 0; i < files.length; i++) {
            uploadRequests[i] = UploadRequest.createOrNull(files[i]);
        }
        try {
            articleService.createArticle(new CreateArticleRequest(getLoginInfo().getId(), categoryId, title, texts, uploadRequests, indexes));
        } catch (Exception e) {
            if (e instanceof UncategorizedSQLException) {
                UncategorizedSQLException use = (UncategorizedSQLException) e;
                if (use.getSQLException().getErrorCode() == Constants.SQLErrorCode.INVALID_CHAR.value()) {
                    throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_CHAR.value());
                }
            }
            throw e;
        }
        return buildSuccess(null);
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RootResponse<?> deleteArticle(
            @ApiParam(name = "id", value = "文章ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        articleService.deleteArticle(id);
        return buildSuccess(null);
    }

    @ApiOperation("查询文章")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryArticleResponse> queryArticles(
            @ApiParam(name = "title", value = "标题") @RequestParam(value = "title", required = false) String title,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        if (StringUtils.isNotBlank(title)) {
            validateSpecialChar(TITLE.value(), title);
            validateStringLength(TITLE.value(), title, 1, 40);
        }
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        return buildSuccess(articleService.queryArticles(
                new QueryArticleRequest(title, new PagingRequest(pageNumber, pageSize))
        ));
    }

    @ApiOperation("查询参与用户")
    @GetMapping("/participators")
    @ResponseBody
    public RootResponse<QueryArticleParticipatorResponse> queryParticipators(
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        if (StringUtils.isNotBlank(name)) {
            validateSpecialChar(NAME.value(), name);
            validateStringLength(NAME.value(), name, 1, 32);
        }
        if (StringUtils.isNotBlank(code)) {
            validateSpecialChar(CODE.value(), code);
            validateStringLength(CODE.value(), code, 1, 16);
        }
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        return buildSuccess(articleService.queryParticipators(
                new QueryArticleParticipatorRequest(name, code, null, new PagingRequest(pageNumber, pageSize))
        ));
    }

}