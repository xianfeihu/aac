package com.yz.aac.mining.repository.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketInteraction {

	/** ID */
	private Long id;
	
	/** 红信ID */
	private Long redPacketId;
	
	/** 父级互动ID */
	private Long parentId;
	
	/** 互动类型：1-评论 2-点赞 3-点击链接 */
	private Integer action;
	
	/** 互动描述信息（评论内容） */
	private String actionDescription;
	
	/** 互动用户ID */
	private Long actionUserId;
	
	/** 互动时间 */
	private Long actionTime;

	public RedPacketInteraction(Long redPacketId, Long parentId,
			Integer action, Long actionUserId, Long actionTime) {
		super();
		this.redPacketId = redPacketId;
		this.parentId = parentId;
		this.action = action;
		this.actionUserId = actionUserId;
		this.actionTime = actionTime;
	}

	public RedPacketInteraction(Long redPacketId, Long parentId,
			Integer action, String actionDescription, Long actionUserId,
			Long actionTime) {
		super();
		this.redPacketId = redPacketId;
		this.parentId = parentId;
		this.action = action;
		this.actionDescription = actionDescription;
		this.actionUserId = actionUserId;
		this.actionTime = actionTime;
	}
	
}
