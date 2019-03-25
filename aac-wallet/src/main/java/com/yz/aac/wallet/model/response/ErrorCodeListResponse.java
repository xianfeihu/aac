package com.yz.aac.wallet.model.response;

import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import com.yz.aac.common.Constants.ResponseMessageInfo;

@Data
@ApiModel("系统ErrorCode描述集合")
public class ErrorCodeListResponse {

	private List<Map<String, String>> errorCodeList; 
	
	public ErrorCodeListResponse() {
		List<Map<String, String>> erList = new ArrayList<Map<String,String>>();
		Map<String, String> erMap = new HashMap<String, String>();
 		for(ResponseMessageInfo rmi : ResponseMessageInfo.values()){
 			erMap.put(rmi.code(), rmi.message());
		}
 		erList.add(erMap);
 		errorCodeList = erList;
	}

}
