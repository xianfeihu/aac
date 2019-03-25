package com.yz.aac.mining.service;

import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.ArticleCommentRequest;
import com.yz.aac.mining.model.request.ArticleRequest;
import com.yz.aac.mining.model.request.ArticleThumbUpRequest;
import com.yz.aac.mining.model.request.UploadRequest;
import com.yz.aac.mining.model.response.*;

import java.util.List;


/**
 *资讯文章基本服务
 *
 */
public interface ArticleService {

	/**
	 * 获取文章发布元素
	 * @param isGetDefault
	 * @return
	 * @throws Exception
	 */
	ArticlePublishElementsResponse getTypeList(Integer isGetDefault) throws Exception;

	/**
	 * 获取预览文章基本元素
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	ArticleBaseElementsResponse getBaseElements(Long userId) throws Exception;

	/**
	 * 发布文章
	 * @param articleRequest 文章基本信息
	 * @param attachments 文章附件集合
	 * @param types 文章附件类型集合（和attachments一一对应）
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	Boolean issuance(ArticleRequest articleRequest, List<UploadRequest> attachments, Integer[] types, Long userId) throws Exception;

	/**
	 * 文章点赞（包含评论互动点赞）
	 * @param articleThumbUpRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Integer thumbUp(ArticleThumbUpRequest articleThumbUpRequest, Long userId) throws Exception;

	/**
	 * 评论
	 * @param articleCommentRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<ArticleCommentResponse> comment(ArticleCommentRequest articleCommentRequest, Long userId) throws Exception;

	/**
	 * 文章个性化设置
	 * @param articleId 文章ID
	 * @param userId 用户
	 * @param policy 个性化类型
	 * @return
	 * @throws Exception
	 */
	Boolean personalizedSettings(Long articleId, Long userId, Integer policy) throws Exception;

	/**
	 * 文章详情
	 * @param articleId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	ArticleIssuanceResponse articleInfo(Long articleId, Long userId) throws Exception;

	/**
	 * 设置阅读奖励
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String setReadingAward(Long userId) throws Exception;

	/**
	 * 获取资讯文章规则文本
	 * @return
	 * @throws Exception
	 */
	String getAwardRules() throws Exception;

	/**
	 * 获取文章类型列表数据
	 * @param pageNo
	 * @param pageSize
	 * @param tagType
	 * @param userId
	 * @return
	 */
	PageResult<ArticleListResponse> getList(Integer pageNo, Integer pageSize, Integer tagType, Long userId, Integer notResult) throws Exception;

}
