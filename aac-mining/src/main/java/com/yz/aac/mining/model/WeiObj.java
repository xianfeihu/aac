package com.yz.aac.mining.model;

import lombok.Data;

@Data
public class WeiObj {
	
	private String toUserName;
	
	private String fromUserName;
	
	private String msgType;
	
	private String content;
	
	private String event;
	
	private String eventKey;

}
