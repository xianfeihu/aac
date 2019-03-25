package com.yz.aac.mining.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.mining.Constants.*;
import com.yz.aac.mining.aspect.AccountAspect;
import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.*;
import com.yz.aac.mining.model.response.*;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import com.yz.aac.mining.service.PacketService;
import com.yz.aac.mining.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PacketServiceImpl implements PacketService{
	
	private final static Integer DEFAULT_PAGENO = 1;//默认开始数
	
	private final static Integer DEFAULT_PAGESIZE = 20;//每页显示数
	
	//领取红包记录下一页路径
	private final static String PACKET_RECEIVE_TECORD_NEXT_PAGEPATH = "/packet/getReceiveList?pageNo=%d&pageSize=%d";
	
	//发布红包记录下一页路径
	private final static String PACKET_ISSUE_TECORD_NEXT_PAGEPATH = "/packet/getHistoricalRecordList?pageNo=%d&pageSize=%d";
	
	@Autowired
	private RedPacketIssuanceRepository redPacketIssuanceRepository;
	
	@Autowired
	private RedPacketImageRepository redPacketImageRepository;
	
	@Autowired
	private FileStorageHandler fileStorageHandler;
	
	@Autowired
	private RedPacketDividingGrabbingRepository redPacketDividingGrabbingRepository;
	
	@Autowired
	private RedPacketDividingRepository redPacketDividingRepository;
	
	@Autowired
	private RedPacketInteractionRepository redPacketInteractionRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BriefTextRepository briefTextRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private UserAssertRepository userAssertRepository;
	
	@Autowired
	private AccountAspect accountAspect;

	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;
	
	@Override
	public EnergyInfoResponse getEnergyInfo(Long userId) throws Exception {
		//邀请-当天邀请好友数
		List<User> userList = userRepository.getInviterList(userId);
		
		//邀请-当天最多邀请数量
		ParamConfig maxInvitationPerDay = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.name());
		
		ParamConfig invitationCurrency = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.INVITATION_CURRENCY.maxCode(), ParamConfigSubclassEnum.INVITATION_CURRENCY.minCode(), ParamConfigSubclassEnum.INVITATION_CURRENCY.name());
		
		return new EnergyInfoResponse((null == userList || userList.size() == 0) ? 0 : userList.size(), 
				Integer.valueOf(maxInvitationPerDay.getValue()), Integer.valueOf(invitationCurrency.getValue()),
				Misc.PLATFORM_CURRENCY_SYMBOL.value());
	}
	
	@Override
	public PackeIndexResponse getPacketMapDate(BigDecimal lng, BigDecimal lat, Long userId) throws Exception {
		PackeIndexResponse pir = new PackeIndexResponse();
		User user = userRepository.getUserById(userId);
		//获取范围红包数据
		List<PacketMapDateResponse> mapList = redPacketIssuanceRepository.getPacketMapDte(lng, lat, Integer.valueOf(OrtherEnum.REDUCED_UNIT.code()), user.getGender());
		if (null != mapList && mapList.size() > 0) {
			for (PacketMapDateResponse rm : mapList) {
				//判断是否抢完
				List<RedPacketDividing> rpdList = redPacketDividingRepository.getPacketDividingSurplus(rm.getPacketId());
				if (null != rpdList && rpdList.size() > 0 ) {
					rm.setIsSnatch(StateType.OK_STATE.code());
					//判断当前用户是否领取
					RedPacketDividingGrabbing rpdg = redPacketDividingGrabbingRepository.getPacketGrabbing(rm.getPacketId(), userId);
					rm.setIsReceive((null == rpdg) ? StateType.NO_STATE.code() : StateType.OK_STATE.code());
				} else {
					rm.setIsSnatch(StateType.NO_STATE.code());
					rm.setIsReceive(StateType.OK_STATE.code());
				}
			}
			pir.setMapDateList(mapList);
		}
		
		//当日领取红包个数限制
		ParamConfig config = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.GRAB_RED_PACKET_PER_DAY.maxCode(), ParamConfigSubclassEnum.GRAB_RED_PACKET_PER_DAY.minCode(), ParamConfigSubclassEnum.GRAB_RED_PACKET_PER_DAY.name());
		Integer receiveNum = redPacketDividingGrabbingRepository.getTodayReceiveCount(userId);
		pir.setResidueDegree(Integer.valueOf(config.getValue()) - receiveNum);
		
		pir.setPlatformCurrency(Misc.PLATFORM_CURRENCY_SYMBOL.value());
		
		return pir;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public PacketInfoResponse savePacket(PacketRequest packetRequest, List<UploadRequest> imgs, Long userId)
			throws Exception {
		
		RedPacketIssuance rpi = new RedPacketIssuance(null, userId, packetRequest.getDescription(), packetRequest.getDividingNumber(),
				packetRequest.getDividingAmount(), packetRequest.getLng(), packetRequest.getLat(), packetRequest.getLocation(),
				packetRequest.getRadius(), packetRequest.getGrabbingLimit(), packetRequest.getLinkTitle(), packetRequest.getLinkUrl(),
				System.currentTimeMillis());
		
		redPacketIssuanceRepository.saveRedPacketIssuance(rpi);
		
		//上传图片
		List<String> imgStrList = new ArrayList<String>();
		for (int i = 0; i<imgs.size(); i++) {
			String imgPath = fileStorageHandler.uploadFile(imgs.get(i).getContent(), imgs.get(i).getExtName());
			redPacketImageRepository.saveRedPacketImage(
					new RedPacketImage(rpi.getId(), imgPath, i+1));
			imgStrList.add(imgPath);
		}
		
		//导入历史红包会有存在图片
		if (null != packetRequest.getImgIdList() && packetRequest.getImgIdList().size() > 0) {
			for (int i = 0; i<packetRequest.getImgIdList().size(); i++) {
				RedPacketImage image = redPacketImageRepository.getRedPacketImageById(packetRequest.getImgIdList().get(i));
				redPacketImageRepository.saveRedPacketImage(
						new RedPacketImage(rpi.getId(), image.getImageUrl(), imgStrList.size() + (i+1)));
			}
		}
		
		PacketInfoResponse packetResponse = new PacketInfoResponse();
		BeanUtils.copyProperties(rpi, packetResponse);
		packetResponse.setImgs(imgStrList);
		
		return packetResponse;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public PacketMesResponse openPacket(Long packetId, Long userId)
			throws Exception {
		
		//判断是否已经拆过该红包
		RedPacketDividingGrabbing rpdg = redPacketDividingGrabbingRepository.getPacketGrabbing(packetId, userId);
		if (null == rpdg) {
			//檢查紅包領取人權限
			RedPacketIssuance packet = redPacketIssuanceRepository.getPacketIssuanceById(packetId);
			User user = userRepository.getUserById(userId);
			if (null != packet) {
				if (null != packet.getGrabbingLimit() && packet.getGrabbingLimit() != user.getGender()) {
					throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "该红包只限" + MerchantGenderType.getKey(packet.getGrabbingLimit()).des() +"领取！");
				}
			} else {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "红包已失效！");
			}

			//随机获取红信子
			RedPacketDividing rpd = redPacketDividingRepository.getPacketDividingByRand(packetId);
			if (null != rpd) {
				redPacketDividingGrabbingRepository.saveRedPacketGrabbing(
						new RedPacketDividingGrabbing(null, packetId, rpd.getId(), userId, System.currentTimeMillis()));
				//用户红包金额存入用户资金账户
				userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), rpd.getDividingAmount(), rpd.getDividingAmount());
				
				//用户行为统计
				accountAspect.addAccountBehaviour(userId, UserBehaviourStatisticsKey.GRAB_RED_PACKET.name());

				//记录拆红包挖矿记录
				userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.RED_LETTER.code(), System.currentTimeMillis(), rpd.getDividingAmount(), MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));
			}
		}
		
		return packetInfo(packetId, userId);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer thumbUp(PacketThumbUpRequest packetThumbUpRequest, Long userId)
			throws Exception {
		//是否有点赞
		RedPacketInteraction redPacketInteraction = redPacketInteractionRepository.getPacketInteractionByUserId(packetThumbUpRequest.getPacketId(), PacketActionType.LIKE.code(), userId, packetThumbUpRequest.getInteractionId());
		if (null != redPacketInteraction) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "已经赞过啦！");
		}
		
		redPacketInteractionRepository.saveRedPacketIssuance(
				new RedPacketInteraction(packetThumbUpRequest.getPacketId(), packetThumbUpRequest.getInteractionId(),
						PacketActionType.LIKE.code(), userId, System.currentTimeMillis()));
		
		List<RedPacketInteraction> interactionList = redPacketInteractionRepository.getPacketInteraction(packetThumbUpRequest.getPacketId(), PacketActionType.LIKE.code());
		return interactionList.size();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean linkClick(PacketLinkClickRequest packetLinkClickRequest, Long userId)
			throws Exception {

		redPacketInteractionRepository.saveRedPacketIssuance(
				new RedPacketInteraction(packetLinkClickRequest.getPacketId(), null,
						PacketActionType.LINK.code(), userId, System.currentTimeMillis()));
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<PacketCommentResponse> comment(PacketCommentRequest packetCommentRequest, Long userId)
			throws Exception {
		
		redPacketInteractionRepository.saveRedPacketIssuance(
				new RedPacketInteraction(packetCommentRequest.getPacketId(), packetCommentRequest.getInteractionId(),
						PacketActionType.COMMENT.code(), packetCommentRequest.getDescription(), userId, System.currentTimeMillis()));
		return getCommentList(packetCommentRequest.getPacketId(), userId);
	}
	
	@Override
	public PageResult<PacketHistoryResponse> getHistoryList(Integer pageNo, Integer pageSize, Long userId) throws Exception {
		Page<PacketHistoryResponse> page = PageHelper.startPage(pageNo, pageSize, true);
		
		List<PacketHistoryResponse> historyList = redPacketIssuanceRepository.getHistoryList(userId, PacketActionType.COMMENT.code(), PacketActionType.LIKE.code());
		historyList = historyList.stream().map(e -> 
		{
			String urlPath;
			try {
				urlPath = fileStorageHandler.genDownloadUrl(e.getImageUrl());
			} catch (Exception ex) {
				ex.printStackTrace();
				urlPath = null;
			}
			return new PacketHistoryResponse(
					e.getId(), e.getDescription(), e.getReceiveNum(), e.getCommentNum(), e.getLikeNum(),
					urlPath, null);
		}
		).collect(Collectors.toList());
		
		return new PageResult<PacketHistoryResponse>(pageNo, pageSize, page.getTotal(), page.getPages(), historyList);
		
	}
	
	@Override
	public PacketImportInfoResponse getPacketImportInfo(Long packetId, Long userId) throws Exception {
		RedPacketIssuance rpi = redPacketIssuanceRepository.getPacketIssuanceById(packetId);
		PacketImportInfoResponse pri = new PacketImportInfoResponse(
				null, rpi.getDescription(), rpi.getDividingNumber(), rpi.getDividingAmount(),
				rpi.getLng(), rpi.getLat(), rpi.getLocation(), rpi.getRadius(), rpi.getGrabbingLimit(),
				rpi.getLinkTitle(), rpi.getLinkUrl(), null);
		
		List<RedPacketImage> imgsList = redPacketImageRepository.getRedPacketImage(packetId);
		
		List<PacketImgResponse> imgs = imgsList.stream().map(e->{
			try {
				return new PacketImgResponse(e.getId(), fileStorageHandler.genDownloadUrl(e.getImageUrl()));
			} catch (Exception e1) {
				return null;
			}
			}).collect(Collectors.toList());
		
		pri.setImgs(imgs);
		return pri;
	}
	
	@Override
	public PacketMesResponse packetInfo(Long packetId, Long userId)throws Exception {
		//发红包方信息
		RedPacketIssuance rpi = redPacketIssuanceRepository.getPacketIssuanceById(packetId);
		User issuerUser = userRepository.getUserById(rpi.getIssuerId());
		//点赞数
		List<RedPacketInteraction> interactionList = redPacketInteractionRepository.getPacketInteraction(packetId, PacketActionType.LIKE.code());
		RedPacketInteraction redPacketInteraction = redPacketInteractionRepository.getPacketInteractionByUserId(packetId, PacketActionType.LIKE.code(), userId, null);
		
		//剩余红包
		List<RedPacketDividing> surplusPackets = redPacketDividingRepository.getPacketDividingSurplus(packetId);
		
		PacketMesResponse pmr = new PacketMesResponse(packetId, issuerUser.getId(), issuerUser.getName(),
				(null == surplusPackets || surplusPackets.size() == 0) ? PacketStatusEnum.END_OF_COLLECTION.code() : PacketStatusEnum.IN_DISTRIBUTION.code(),
						(null == interactionList || interactionList.size() == 0) ? 0 : interactionList.size(),
								rpi.getDescription(), rpi.getLinkTitle(), rpi.getLinkUrl());
		
		//判断是否已经拆过该红包
		RedPacketDividingGrabbing rpdg = redPacketDividingGrabbingRepository.getPacketGrabbing(packetId, userId);
		if (null == rpdg) {
			//随机获取红信子项
			RedPacketDividing rpd = redPacketDividingRepository.getPacketDividingByRand(packetId);
			if (null == rpd) {
				pmr.setReceivingStatus(PacketReceivingStatusEnum.UNCOLLECTED_END.code());
			} else {
				pmr.setReceivingStatus(PacketReceivingStatusEnum.UNCOLLECTED.code());
			}
		} else {
			RedPacketDividing redPacketDividing = redPacketDividingRepository.getPacketDividingById(rpdg.getDividingId());
			pmr.setReceivingStatus(PacketReceivingStatusEnum.HAVE_RECEIVED.code());
			pmr.setAmount(redPacketDividing.getDividingAmount());
		}
		//当前用户是否点赞
		pmr.setIsLikes(null == redPacketInteraction ? StateType.NO_STATE.code() : StateType.OK_STATE.code());
		
		//领取人列表
		List<String> grabbers = redPacketDividingGrabbingRepository.getGrabbers(packetId);
		pmr.setGrabberList(grabbers);
		
		//图片列表
		List<RedPacketImage> imgs = redPacketImageRepository.getRedPacketImage(packetId);
		pmr.setImgList(imgs.stream().map(e -> {
			String urlPath;
			try {
				urlPath = fileStorageHandler.genDownloadUrl(e.getImageUrl());
			} catch (Exception ex) {
				ex.printStackTrace();
				urlPath = null;
			}
			return urlPath;
		}).collect(Collectors.toList()));
		
		//留言列表
		pmr.setCommentList(getCommentList(packetId, userId));
		
		return pmr;
	}
	
	@Override
	public PacketReceiveInfoResponse getReceiveInfo(Long userId)throws Exception {
		//当前用户领取红包统计
		BigDecimal totalAmount = redPacketDividingGrabbingRepository.getReceiveTotalAmount(userId);
		
		//历史最佳手气
		BigDecimal bestLuckAmount = redPacketDividingGrabbingRepository.getBestLuck(userId);
		
		//领取红包分页记录
		PageResult<PacketHistoryResponse> pageResult = getReceiveList(DEFAULT_PAGENO, DEFAULT_PAGESIZE, userId);
		
		setMiningRecordNextPagePath(pageResult, PACKET_RECEIVE_TECORD_NEXT_PAGEPATH);
		
		return new PacketReceiveInfoResponse(totalAmount, bestLuckAmount, pageResult);
	}
	
	@Override
	public PageResult<PacketHistoryResponse> getReceiveList(Integer pageNo, Integer pageSize, Long userId) throws Exception {
		Page<PacketHistoryResponse> page = PageHelper.startPage(pageNo, pageSize, true);
		
		List<PacketHistoryResponse> historyList = redPacketDividingGrabbingRepository.getReceiveRecordList(userId, PacketActionType.COMMENT.code(), PacketActionType.LIKE.code());
		historyList = historyList.stream().map(e -> 
		{
			try {
				return new PacketHistoryResponse(
					e.getId(), e.getDescription(), e.getReceiveNum(), e.getCommentNum(), e.getLikeNum(),
					fileStorageHandler.genDownloadUrl(e.getImageUrl()), e.getIssuerName());
			} catch (Exception e1) {
				return null;
			}
		}
		).collect(Collectors.toList());
		
		return new PageResult<PacketHistoryResponse>(pageNo, pageSize, page.getTotal(), page.getPages(), historyList);
		
	}
	
	@Override
	public PacketIssueInfoResponse getIssueInfo(Long userId)throws Exception {
		//当前用户发布总金额
		BigDecimal totalAmount = redPacketIssuanceRepository.getTotalAmount(userId);
		
		//累计影响人数
		Integer receiveNum = redPacketIssuanceRepository.getTotalReceiveNum(userId);
		
		//发布红包分页记录
		PageResult<PacketHistoryResponse> pageResult = getHistoryList(DEFAULT_PAGENO, DEFAULT_PAGESIZE, userId);
		
		setMiningRecordNextPagePath(pageResult, PACKET_ISSUE_TECORD_NEXT_PAGEPATH);
		
		return new PacketIssueInfoResponse(totalAmount, receiveNum, pageResult);
	}
	
	@Override
	public PacketStatisticesInfoResponse effectStatistics(Long packetId)throws Exception {
		//基础信息
		RedPacketIssuance rpi = redPacketIssuanceRepository.getPacketIssuanceById(packetId);
		//小贴士
		BriefText briefText = briefTextRepository.getBriefText(BriefTextKeyEnum.TIPS.name());
		PacketStatisticesInfoResponse psir = new PacketStatisticesInfoResponse(rpi.getDividingAmount(), rpi.getLocation(), rpi.getGrabbingLimit(), briefText.getContent());
		
		//计算领取总耗时
		Long lastTime = redPacketDividingGrabbingRepository.getLatestCollectionTime(packetId);
		if (null != lastTime) {
			psir.setElapsedTimeStr(TimeUtil.formatSeconds((lastTime - rpi.getIssuanceTime())/1000));
		}
		
		//转化数据率
		List<String> grabbersList = redPacketDividingGrabbingRepository.getGrabbers(packetId);
		List<RedPacketDividing> elements = redPacketDividingRepository.getPacketTotalNum(packetId);
		if (null == grabbersList || grabbersList.size() == 0) {
			psir.setTakeUpRate(0);
			psir.setExposureRate(0);
		} else {
			psir.setTakeUpRate((int) (((float)grabbersList.size())/elements.size()*100));
			psir.setExposureRate((int) (((float)grabbersList.size())/elements.size()*100));
		}
		
		List<RedPacketInteraction> likeList = redPacketInteractionRepository.getPacketInteraction(packetId, PacketActionType.LIKE.code());
		if (null == grabbersList || grabbersList.size() == 0 || null == likeList || likeList.size() == 0) {
			psir.setPointPraiseRate(0);
		} else {
			psir.setPointPraiseRate((int) (((float)likeList.size())/grabbersList.size()*100));
		}
		
		List<Long> issuerIds = new ArrayList<Long>();
		issuerIds.add(rpi.getIssuerId());
		List<Integer> commentList = redPacketInteractionRepository.getCommentByUserId(packetId, PacketActionType.COMMENT.code(), issuerIds);
		
		if (null == grabbersList || grabbersList.size() == 0 || null == commentList || commentList.size() == 0) {
			psir.setCommentRate(0);
		} else {
			psir.setCommentRate((int) (((float)commentList.size())/grabbersList.size()*100));
		}
		
		List<RedPacketInteraction> linkList = redPacketInteractionRepository.getPacketInteraction(packetId, PacketActionType.LINK.code());
		if (null == grabbersList || grabbersList.size() == 0 || null == linkList || linkList.size() == 0) {
			psir.setClickThroughRate(0);
		} else {
			psir.setClickThroughRate((int) (((float)linkList.size())/grabbersList.size()*100));
		}
		
		return psir;
	}
	
	/**
	 * 获取红包评论列表
	 * @param packetId
	 * @param userId
	 * @return
	 */
	public List<PacketCommentResponse> getCommentList(Long packetId, Long userId){
		List<PacketCommentResponse> commentList = redPacketInteractionRepository.getLeavingMessage(packetId, PacketActionType.COMMENT.code(), userId, PacketActionType.LIKE.code());
		if (null != commentList && commentList.size() > 0) {
			for (PacketCommentResponse pcr : commentList) {
				List<PacketCommentResponse> pcrList = redPacketInteractionRepository.getReplyList(pcr.getCommentId(), PacketActionType.COMMENT.code());
				if (null != pcrList && pcrList.size() > 0) {
					pcr.setReplyList(pcrList);
				}
			}
		}
		
		return commentList;
	}
	
	/**
	 * 手动构建下一页请求路径
	 * @param <T>
	 * @param pageResult
	 * @param pagePath
	 */
	private <T> void setMiningRecordNextPagePath(PageResult<T> pageResult, String pagePath){
		pageResult.setNextPagePath(String.format(pagePath, DEFAULT_PAGENO, DEFAULT_PAGESIZE));
		pageResult.buildNextPagePath();
	}
}
