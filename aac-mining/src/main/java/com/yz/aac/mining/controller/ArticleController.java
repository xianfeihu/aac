package com.yz.aac.mining.controller;

import com.yz.aac.common.Constants;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.mining.aspect.targetCustom.PageControllerAspect;
import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.*;
import com.yz.aac.mining.model.response.*;
import com.yz.aac.mining.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(value = "/article")
@Slf4j
@Api(tags = "挖矿-资讯文章")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;
    
    @ApiOperation(value = "获取文章类型")
    @GetMapping("/getTypeList")
    public RootResponse<ArticlePublishElementsResponse> getTypeList(
            @ApiParam(name = "isGetDefault", value = "是否获取系统默认[推荐，关注]类型（1-是 2-否<默认>）", required = false) @RequestParam(value = "isGetDefault", required = false) Integer isGetDefault
    ) throws Exception {
        return buildSuccess(articleService.getTypeList(isGetDefault));
    }

    @ApiOperation(value = "文章预览基本元素")
    @GetMapping("/getBaseElements")
    public RootResponse<ArticleBaseElementsResponse> getBaseElements() throws Exception {

        LoginInfo userInfo = getLoginInfo();
        Long userId = (null == userInfo) ? null : userInfo.getId();

        return buildSuccess(articleService.getBaseElements(userId));
    }

    @ApiOperation(value = "发布文章")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE, value = "/issuance")
    public RootResponse<Boolean> issuance(
            ArticleRequest articleRequest,
            @ApiParam(name = "attachments", value = "附件", required = false) @RequestParam(value = "attachments", required = false) MultipartFile[] attachments,
            @ApiParam(name = "types[]", value = "元素类型（1-文字 2-视频 3-图片）", required = false) @RequestParam(value = "types[]", required = false) Integer[] types
    ) throws Exception {
        validateRequired("文章类型", articleRequest.getType());
        validateRequired("文章标题", articleRequest.getTitle());
        validateStringLength("文章标题", articleRequest.getTitle(), 20, 40);

        int textLength = (null == articleRequest.getArticleTextRequestList()) ? 0 : articleRequest.getArticleTextRequestList().size();
        int attachmentLength = (null == attachments) ? 0 : attachments.length;
        int typeLength = (null == types) ? 0 : types.length;
        List<Integer> orderNumberList = articleRequest.getArticleTextRequestList().stream().map(x -> x.getOrderNumber()).collect(Collectors.toList());
        if (textLength == 0 || !orderNumberList.contains(1)) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "默认文章文本必填！");
        }

        if (textLength == 0) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "文章文本，视频，图片必须填其一！");
        }

        if ((textLength + attachmentLength) > 10) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "文章内容模块最多十个");
        }

        if (null != articleRequest.getArticleTextRequestList() && articleRequest.getArticleTextRequestList().size() > 0) {
            ArticlePublishElementsResponse element =  articleService.getTypeList(com.yz.aac.mining.Constants.StateType.NO_STATE.code());
            for (ArticleTextRequest articleText : articleRequest.getArticleTextRequestList()) {

                validateRequired("文章文本", articleText.getContent());
                validateRequired("文章文本模块排序号", articleText.getOrderNumber());
                validateStringLength("文章文本", articleText.getContent(), element.getArticleMinLength(), 500);

            }
        }

        if (typeLength != attachmentLength) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "附件元素类型错误！");
        }

        List<UploadRequest> uploadRequestList = new ArrayList<UploadRequest>();
        if (attachmentLength > 0) {
            for (MultipartFile img : attachments) {
                uploadRequestList.add(UploadRequest.createOrNull(img));
            }
        }

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.issuance(articleRequest, uploadRequestList, types, userId));
    }

    @ApiOperation(value = "文章点赞（包含评论互动点赞）")
    @PostMapping("/thumbUp")
    public RootResponse<Integer> thumbUp(@RequestBody ArticleThumbUpRequest articleThumbUpRequest) throws Exception {
        validateRequired("红包ID", articleThumbUpRequest.getArticleId());

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.thumbUp(articleThumbUpRequest, userId));
    }

    @ApiOperation(value = "评论")
    @PostMapping("/comment")
    public RootResponse<List<ArticleCommentResponse>> comment(@RequestBody ArticleCommentRequest articleCommentRequest) throws Exception {
        validateRequired("文章ID", articleCommentRequest.getArticleId());
        validateRequired("评论内容", articleCommentRequest.getDescription());

        validateStringLength("评论内容", articleCommentRequest.getDescription(), 1, 125);

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.comment(articleCommentRequest, userId));
    }

    @ApiOperation(value = "文章个性化设置")
    @PostMapping("/personalizedSettings")
    public RootResponse<Boolean> personalizedSettings(@RequestBody ArticlePersonalizationParamRequest articlePersonalizationParamRequest) throws Exception {
        validateRequired("文章ID", articlePersonalizationParamRequest.getArticleId());
        validateRequired("个性化类型", articlePersonalizationParamRequest.getPolicy());

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.personalizedSettings(articlePersonalizationParamRequest.getArticleId(), userId, articlePersonalizationParamRequest.getPolicy()));
    }

    @ApiOperation(value = "文章详情")
    @GetMapping("/articleInfo")
    public RootResponse<ArticleIssuanceResponse> articleInfo(
            @ApiParam(name = "articleId", value = "文章ID", required = true) @RequestParam(value = "articleId", required = true) Long articleId
    ) throws Exception {
        this.validateRequired("文章ID", articleId);

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.articleInfo(articleId, userId));
    }

    @ApiOperation(value = "阅读奖励")
    @PostMapping("/readingAward")
    public RootResponse<String> readingAward() throws Exception {

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.setReadingAward(userId));
    }

    @ApiOperation(value = "获得奖励规则文本")
    @GetMapping("/getAwardRules")
    public RootResponse<String> getAwardRules() throws Exception {

        return buildSuccess(articleService.getAwardRules());
    }

    @ApiOperation(value = "资讯文章列表")
    @GetMapping("/getList")
    @PageControllerAspect
    public RootResponse<PageResult<ArticleListResponse>> getList(
            @ApiParam(name = "pageNo", value = "当前页码", required = true) @RequestParam(value = "pageNo" ) Integer pageNo,
            @ApiParam(name = "pageSize", value = "每页条数", required = true) @RequestParam(value = "pageSize") Integer pageSize,
            @ApiParam(name = "tagType", value = "资讯类型(默认推荐类型)") @RequestParam(value = "tagType", required = false) Integer tagType,
            @ApiParam(name = "notResult", value = "分页自动填充数据") @RequestParam(value = "notResult", required = false) Integer notResult
    ) throws Exception {
        this.validateRequired("当前页码", pageNo);
        this.validateRequired("每页条数", pageSize);

        LoginInfo userInfo = getLoginInfo();
        Long userId = userInfo.getId();

        return buildSuccess(articleService.getList(pageNo, pageSize, tagType, userId, notResult));
    }

}