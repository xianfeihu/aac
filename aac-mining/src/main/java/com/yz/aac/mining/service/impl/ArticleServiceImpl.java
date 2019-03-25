package com.yz.aac.mining.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.exception.SerializationException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.mining.Constants;
import com.yz.aac.mining.Constants.ParamConfigSubclassEnum;
import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.*;
import com.yz.aac.mining.model.response.*;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import com.yz.aac.mining.service.ArticleService;
import com.yz.aac.mining.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private  ArticleCategoryRepository articleCategoryRepository;

	@Autowired
	private ParamConfigRepository paramConfigRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleElementRepository articleElementRepository;

	@Autowired
	private FileStorageHandler fileStorageHandler;

	@Autowired
	private ArticleInteractionRepository articleInteractionRepository;

	@Autowired
	private ArticlePersonalRepository articlePersonalRepository;

	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;

	@Autowired
	private UserAssertRepository userAssertRepository;

	@Autowired
	private BriefTextRepository briefTextRepository;

	@Autowired
	private OperatorRepository operatorRepository;
	
	@Override
	public ArticlePublishElementsResponse getTypeList(Integer isGetDefault) throws Exception {
		List<ArticleCategory> categoryList = articleCategoryRepository.getArticleCategory();
		if (null != isGetDefault && isGetDefault == Constants.StateType.OK_STATE.code()) {
			List<ArticleCategory> addCategoryList = Arrays.stream(Constants.ArticleDefaultCategoryEnum.values()).map(e -> new ArticleCategory((long)e.code(), e.message(), null)).collect(Collectors.toList());
			if (null != categoryList && categoryList.size() > 0) {
				categoryList.addAll(0, addCategoryList);
			} else {
				categoryList = addCategoryList;
			}
		}

		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MIN_ARTICLE_LENGTH.maxCode(), ParamConfigSubclassEnum.MIN_ARTICLE_LENGTH.minCode(), ParamConfigSubclassEnum.MIN_ARTICLE_LENGTH.name());
		return new ArticlePublishElementsResponse(categoryList,(null != paramConfig) ? Integer.valueOf(paramConfig.getValue()) : 500);
	}

	@Override
	public ArticleBaseElementsResponse getBaseElements(Long userId) throws Exception {
		User user = userRepository.getUserById(userId);

		return new ArticleBaseElementsResponse(user.getName(), Constants.ArticleLabelEnum.ORIGINAL.des());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean issuance(ArticleRequest articleRequest, List<UploadRequest> attachments, Integer[] types, Long userId)
			throws Exception {
		try{
			//保存基本文章信息
			Article article = new Article(null, articleRequest.getType(), articleRequest.getTitle(), userId, System.currentTimeMillis(), Constants.ArticleAuthorTypeEnum.APP.code());
			articleRepository.saveArticle(article);

			//保存文章元素
			List<Integer> textOrderNumberList = new ArrayList<>();

				if (null != articleRequest.getArticleTextRequestList() && articleRequest.getArticleTextRequestList().size() > 0) {
					for (ArticleTextRequest atr : articleRequest.getArticleTextRequestList()) {
						articleElementRepository.saveArticleElement(new ArticleElement(null, article.getId(), Constants.ArticleElementType.TEXT.code(), atr.getContent(), atr.getOrderNumber()));
						textOrderNumberList.add(atr.getOrderNumber());
					}
				}

			if (null != attachments && attachments.size() > 0) {
				//计算附件排序号
				List<Integer> orderNumberList = new ArrayList<>();
				for (int i = 1; i <= (textOrderNumberList.size() + attachments.size()); i++) {
					orderNumberList.add(i);
				}

				if (textOrderNumberList.size() > 0) {
					orderNumberList.removeAll(textOrderNumberList);
				}

				for (int i = 0; i < attachments.size(); i++) {
					String imgPath = fileStorageHandler.uploadFile(attachments.get(i).getContent(), attachments.get(i).getExtName());
					articleElementRepository.saveArticleElement(new ArticleElement(null, article.getId(), types[i], imgPath, orderNumberList.get(i)));
				}
			}

			return true;
		} catch (UncategorizedSQLException e){//禁止输入特殊符号（Emoji表情）
			if (e.getSQLException().getErrorCode() == 1366) {
				throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "输入有非法字符！");
			}
			throw e;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer thumbUp(ArticleThumbUpRequest articleThumbUpRequest, Long userId)
			throws Exception {
		List<ArticleInteraction> interactionList;
		articleIsEmpty(articleThumbUpRequest.getArticleId());

		//是否有点赞
		ArticleInteraction articleInteraction = articleInteractionRepository.getArticleInteractionByUserId(articleThumbUpRequest.getArticleId(), Constants.ArticleActionType.LIKE.code(), userId, articleThumbUpRequest.getInteractionId());
		if (null != articleInteraction) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "已经赞过啦！");
		}

		articleInteractionRepository.saveArticleInteraction(
				new ArticleInteraction(null, articleThumbUpRequest.getArticleId(), articleThumbUpRequest.getInteractionId(),
						Constants.ArticleActionType.LIKE.code(), null, userId, System.currentTimeMillis()));
		if (null != articleThumbUpRequest.getInteractionId()) {
			interactionList = articleInteractionRepository.getArticleInteractionByParent(articleThumbUpRequest.getArticleId(), Constants.ArticleActionType.LIKE.code(), articleThumbUpRequest.getInteractionId());
		} else{
			interactionList = articleInteractionRepository.getArticleInteraction(articleThumbUpRequest.getArticleId(), Constants.ArticleActionType.LIKE.code());
		}

		return interactionList.size();
	};

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<ArticleCommentResponse> comment(ArticleCommentRequest articleCommentRequest, Long userId)
			throws Exception {
		articleIsEmpty(articleCommentRequest.getArticleId());

		articleInteractionRepository.saveArticleInteraction(
				new ArticleInteraction(null, articleCommentRequest.getArticleId(), null,
						Constants.ArticleActionType.COMMENT.code(), articleCommentRequest.getDescription(), userId, System.currentTimeMillis()));

		return getCommentList(articleCommentRequest.getArticleId(), userId);
	}

	/**
	 * 获取评论列表
	 * @param articleId
	 * @param userId
	 * @return
	 */
	private List<ArticleCommentResponse> getCommentList(Long articleId, Long userId){
		List<ArticleCommentResponse> commentList = articleInteractionRepository.getComment(articleId, Constants.ArticleActionType.COMMENT.code(), userId, Constants.ArticleActionType.LIKE.code());
		if (null != commentList && commentList.size() > 0) {
			for (ArticleCommentResponse acr : commentList) {
				acr.setActionTimeStr(TimeUtil.secondsFormatStr((System.currentTimeMillis() - acr.getActionTime())/1000));
			}
		}
		return commentList;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean personalizedSettings(Long articleId, Long userId, Integer policy)
			throws Exception {
		articleIsEmpty(articleId);

		articlePersonalRepository.saveArticlePersonal(
				new ArticlePersonal(null, userId, articleId, policy)
		);

		return true;
	}

	@Override
	public ArticleIssuanceResponse articleInfo(Long articleId, Long userId)throws Exception {

		String userName;

		articleIsEmpty(articleId);

		//文章基本信息
		Article article = articleRepository.getArticleById(articleId);

		if (Constants.ArticleAuthorTypeEnum.APP.code() == article.getAuthorType()) {
			User user = userRepository.getUserById(article.getAuthorId());
			userName = user.getName();
		} else {
			Operator operator = operatorRepository.getOperatorById(article.getAuthorId());
			userName = operator.getName();
		}

		ArticleIssuanceResponse articleIssuanceResponse = new ArticleIssuanceResponse(articleId, userName, Constants.ArticleLabelEnum.ORIGINAL.des(), article.getTitle());
		articleIssuanceResponse.setTimeStr(TimeUtil.secondsFormatStr((System.currentTimeMillis() - article.getCreateTime())/1000));

		//元素列表
		List<ArticleElement> elementList = articleElementRepository.getArticleElementByArticleId(articleId);
		if (null != elementList && elementList.size() > 0) {
			List<ArticleElementResponse>  aerList= elementList.stream().map(e ->{
						try {
							return new ArticleElementResponse(
									e.getElementType()
									, (e.getElementType() != Constants.ArticleElementType.TEXT.code()) ? fileStorageHandler.genDownloadUrl(e.getElementContent()) : e.getElementContent()
									, e.getOrderNumber());
						} catch (SerializationException ex) {
							log.error(ex.getMessage(), ex);
							throw new RuntimeException(ex);
						}
			}
			).collect(Collectors.toList());

			articleIssuanceResponse.setArticleElementList(aerList);
		}

		//赞赞
		List<ArticleInteraction> interactionList = articleInteractionRepository.getArticleInteraction(articleId, Constants.ArticleActionType.LIKE.code());
		articleIssuanceResponse.setLikes((null == interactionList || interactionList.size() == 0) ? 0 : interactionList.size());
		interactionList = interactionList.stream().filter(il -> il.getActionUserId() == userId).collect(Collectors.toList());
		articleIssuanceResponse.setIsLikes((null == interactionList || interactionList.size() == 0) ? Constants.StateType.NO_STATE.code() : Constants.StateType.OK_STATE.code());

		//个性化属性
		ArticlePersonal followBean = articlePersonalRepository.getArticlePersonal(userId, articleId, Constants.ArticlePersonalType.FOLLOW.code());
		ArticlePersonal dontSeeBean = articlePersonalRepository.getArticlePersonal(userId, articleId, Constants.ArticlePersonalType.DONT_SEE.code());

		articleIssuanceResponse.setIsFollow((null == followBean) ? Constants.StateType.NO_STATE.code() : Constants.StateType.OK_STATE.code());
		articleIssuanceResponse.setIsShield((null == dontSeeBean) ? Constants.StateType.NO_STATE.code() : Constants.StateType.OK_STATE.code());

		//随机同类型五篇文章
		List<Article> articleRandList = articleRepository.getArticleRand(article.getCategoryId(), 5);

		List<ArticleListResponse> randArticleList = setArticleResult(articleRandList, userId);
		articleIssuanceResponse.setRandArticleList(randArticleList);

		//评论列表
		articleIssuanceResponse.setCommentList(getCommentList(articleId, userId));

		//阅读时长
		ParamConfig readingTime = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.READING_TIME.maxCode(), ParamConfigSubclassEnum.READING_TIME.minCode(), ParamConfigSubclassEnum.READING_TIME.name());
		articleIssuanceResponse.setReadingTime((null != readingTime) ? Integer.valueOf(readingTime.getValue()) : null);

		//是否达到当日阅读最高奖励数
		List<UserMiningRecord> miningList = userMiningRecordRepository.getRecordToDay(userId, Constants.MiningActionEnum.READ.code());
		int max = (null == miningList || miningList.size() == 0) ? 0 : miningList.size();
		ParamConfig maxReadingPerDay = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_READING_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_READING_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_READING_PER_DAY.name());
		articleIssuanceResponse.setIsMaxRewardToday((max < Integer.valueOf(maxReadingPerDay.getValue())) ? Constants.StateType.NO_STATE.code() : Constants.StateType.OK_STATE.code());

		return articleIssuanceResponse;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String setReadingAward(Long userId)throws Exception {
		ParamConfig readingCurrency = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.READING_CURRENCY.maxCode(), ParamConfigSubclassEnum.READING_CURRENCY.minCode(), ParamConfigSubclassEnum.READING_CURRENCY.name());
		int upset = userAssertRepository.addUserAssertBalance(userId, com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value(), BigDecimal.valueOf(Integer.valueOf(readingCurrency.getValue())), BigDecimal.valueOf(Integer.valueOf(readingCurrency.getValue())));
		if (upset > 0) {
			//挖矿记录
			User user = userRepository.getUserById(userId);
			userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), Constants.MiningActionEnum.READ.code(), System.currentTimeMillis(), BigDecimal.valueOf(Integer.valueOf(readingCurrency.getValue())), Constants.MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));
			return "奖励AAB+" + Integer.valueOf(readingCurrency.getValue());
		}
		throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "阅读文章奖励错误！");
	}

	@Override
	public String getAwardRules()throws Exception {

		BriefText briefText = briefTextRepository.getBriefText(Constants.BriefTextKeyEnum.ARTICLE_AWARD.name());
		return (null != briefText) ? briefText.getContent() : null;
	}

	@Override
	public PageResult<ArticleListResponse> getList(Integer pageNo, Integer pageSize, Integer tagType,Long userId, Integer notResult) throws Exception {
		Page<PacketHistoryResponse> page = PageHelper.startPage(pageNo, pageSize, true);

		notResult = null == notResult ? Constants.StateType.OK_STATE.code() : notResult;//用来区别推荐列表数据(eg:前200推荐数据还是按照阅读量数据)
		tagType = null == tagType ? Constants.ArticleDefaultCategoryEnum.RECOMMENDATION.code() : tagType;
		List<Article> articleList;
		Map<String, String> paramMap = new HashMap<>();

		if (tagType == Constants.ArticleDefaultCategoryEnum.RECOMMENDATION.code()) {
			if (notResult == Constants.StateType.OK_STATE.code()) {
				articleList = articleRepository.getArticleList(Constants.ArticleActionType.READING.code(), Constants.ArticlePersonalType.DONT_SEE.code(),
						tagType, Constants.ArticleDefaultReadCountEnum.MAX_NUM.code(), Constants.ArticleDefaultCategoryEnum.RECOMMENDATION.code(),
						null, null, null, null);

				//如果第一次推荐没有数据则按照阅读量从大到小分页获取
				if ((null == articleList || articleList.size() == 0) && pageNo == 1) {
					page = PageHelper.startPage(pageNo, pageSize, true);//从新初始化分页数据

					articleList = articleRepository.getArticleList(Constants.ArticleActionType.READING.code(), Constants.ArticlePersonalType.DONT_SEE.code(),
							null,null,null,null,null,null,null);
					if (null != pageNo && page.getPages() > pageNo) {
						paramMap.put("notResult", String.valueOf(Constants.StateType.NO_STATE.code()));
					}
				}
			} else {
				articleList = articleRepository.getArticleList(Constants.ArticleActionType.READING.code(), Constants.ArticlePersonalType.DONT_SEE.code(),
						null,null,null,null,null,null,null);

			}

		} else if(tagType == Constants.ArticleDefaultCategoryEnum.FOLLOW.code()) {
			articleList = articleRepository.getArticleList(Constants.ArticleActionType.READING.code(), Constants.ArticlePersonalType.DONT_SEE.code(),
					tagType, null, null, Constants.ArticleDefaultCategoryEnum.FOLLOW.code(), userId, Constants.ArticlePersonalType.FOLLOW.code()
					, null);
		} else {
			articleList = articleRepository.getArticleList(Constants.ArticleActionType.READING.code(),Constants.ArticlePersonalType.DONT_SEE.code() ,
					null, null, null, null, userId, null, tagType);
		}
		List<ArticleListResponse> resultList = setArticleResult(articleList, userId);

		return new PageResult<ArticleListResponse>(pageNo, pageSize, page.getTotal(), page.getPages(), resultList, paramMap);

	}

	/**
	 * 构建文章列表属性
	 * @param articleList
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ArticleListResponse> setArticleResult(List<Article> articleList, Long userId) throws Exception{
		List<ArticleListResponse> randArticleList = new ArrayList<>();
		if (null != articleList && articleList.size() > 0) {
			for (Article articleBean : articleList) {
				ArticleListResponse articleListResponse = new ArticleListResponse();
				articleListResponse.setArticleId(articleBean.getId());
				articleListResponse.setTitle(articleBean.getTitle());

				try{
					if (Constants.ArticleAuthorTypeEnum.APP.code() == articleBean.getAuthorType()) {
						User author = userRepository.getUserById(articleBean.getAuthorId());
						articleListResponse.setAuthor(author.getName());
					} else {
						Operator operator = operatorRepository.getOperatorById(articleBean.getAuthorId());
						articleListResponse.setAuthor(operator.getName());
					}
				}catch (NullPointerException e) {
					throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "文章数据异常！");
				}


				articleListResponse.setTimeStr(TimeUtil.secondsFormatStr((System.currentTimeMillis() - articleBean.getCreateTime())/1000));
				List<Long> commentNumbers = articleInteractionRepository.getArticleInteractionUserId(articleBean.getId(), Constants.ArticleActionType.COMMENT.code());
				articleListResponse.setCommentNumber((null == commentNumbers || commentNumbers.size() == 0) ? 0 : commentNumbers.size());
				ArticleElement articleElement = articleElementRepository.getArticleElementFirst(articleBean.getId(),
						new Integer[]{Constants.ArticleElementType.IMG.code()});

				if (null != articleElement) {
					articleListResponse.setHeadDiagramUrl(fileStorageHandler.genDownloadUrl(articleElement.getElementContent()));
				}

				articleListResponse.setIsMyself(articleBean.getAuthorId() == userId ? Constants.StateType.OK_STATE.code() : Constants.StateType.NO_STATE.code());

				randArticleList.add(articleListResponse);
			}

		}
		return randArticleList;
	}

	/**
	 * 文章是否存在
	 * @param articleId
	 */
	private void articleIsEmpty(Long articleId) throws Exception {
		Article article = articleRepository.getArticleById(articleId);
		if (Objects.isNull(article)) {
			throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "文章不存在！");
		}
	}
}
