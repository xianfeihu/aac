package com.yz.aac.mining.service;

import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.*;
import com.yz.aac.mining.model.response.*;

import java.math.BigDecimal;
import java.util.List;



/**
 *红信基本服务
 *
 */
public interface PacketService {
	
	/**
	 * 推荐能量详情
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	EnergyInfoResponse getEnergyInfo(Long userId) throws Exception;
	
	/**
	 * 根据范围获取周围红包
	 * @param lng
	 * @param lat
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PackeIndexResponse getPacketMapDate(BigDecimal lng, BigDecimal lat, Long userId) throws Exception;

	/**
	 * 发布红信
	 * @param packetRequest
	 * @param imgs
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PacketInfoResponse savePacket(PacketRequest packetRequest, List<UploadRequest> imgs, Long userId) throws Exception;
	
	/**
	 * 拆红包
	 * @param packetId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PacketMesResponse openPacket(Long packetId, Long userId) throws Exception;
	
	/**
	 * 点赞
	 * @param packetId
	 * @param interactionId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Integer thumbUp(PacketThumbUpRequest packetThumbUpRequest, Long userId) throws Exception;

	/**
	 * 链接点击
	 * @param packetLinkClickRequest
	 * @param userId
	 * @throws Exception
	 */
	Boolean linkClick(PacketLinkClickRequest packetLinkClickRequest, Long userId) throws Exception;

	/**
	 * 评论
	 * @param packetCommentRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<PacketCommentResponse> comment(PacketCommentRequest packetCommentRequest, Long userId) throws Exception;
	
	/**
	 * 获取红包发布历史
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PageResult<PacketHistoryResponse> getHistoryList(Integer pageNo, Integer pageSize, Long userId) throws Exception;
	
	/**
	 * 获取红包导入信息
	 * @param packetId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PacketImportInfoResponse getPacketImportInfo(Long packetId, Long userId) throws Exception;
	
	/**
	 * 红包详情
	 * @param packetId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PacketMesResponse packetInfo(Long packetId, Long userId) throws Exception;
	
	/**
	 * 抢到红包金额统计和领取历史记录
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PacketReceiveInfoResponse getReceiveInfo(Long userId) throws Exception;
	
	/**
	 * 领取红包记录列表
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PageResult<PacketHistoryResponse> getReceiveList(Integer pageNo, Integer pageSize, Long userId) throws Exception;
	
	/**
	 * 发布红包金额统计和发布历史记录
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PacketIssueInfoResponse getIssueInfo(Long userId) throws Exception;
	
	/**
	 * 红包效果统计
	 * @param packetId
	 * @return
	 * @throws Exception
	 */
	PacketStatisticesInfoResponse effectStatistics(Long packetId) throws Exception;
	
}
