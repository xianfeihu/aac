package com.yz.aac.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("用户登录基本信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

	@ApiModelProperty(value = "用户ID", position = 1)
	private Long id;
	
	@ApiModelProperty(value = "真实姓名", position = 2)
	private String name;
	
	@ApiModelProperty(value = "性别(1-男 2-女)", position = 3)
	private Integer gender;
	
	@ApiModelProperty(value = "身份证号码", position = 4)
	private String idNumber;
	
	@ApiModelProperty(value = "电话号码", position = 5)
	private Long mobileNumber;
	
	@ApiModelProperty(value = "用户来源(1-常规注册用户 2-虚拟挂单用户)", position = 6)
	private Integer source;
	
	@ApiModelProperty(value = "注册时间", position = 7)
	private Long regTime;
	
	@ApiModelProperty(value = "推荐人ID", position = 8)
	private Long inviterId;
	
	@ApiModelProperty(value = "是否是商户(1-是 2-否)", position = 9)
	private Integer isMerchant;
	
	@ApiModelProperty(value = "是否是广告主(1-是 2-否)", position = 10)
	private Integer isAdvertiser;
	
	@ApiModelProperty(value = "是否缴纳押金(1-已发币，等待资格审核 2-资格审核通过，待付押金 3-押金已付，待审核 4-押金审核失败 5-押金审核通过)", position = 11)
	private Integer depositStatus;
	
	@ApiModelProperty(value = "是否设置支付密码(1-是 2-否)", position = 11)
	private Integer isSetPaymentPassword;
	
	@ApiModelProperty(value = "用户登录token", position = 12)
	private String token;
	
	@ApiModelProperty(value = "邀请码", position = 13)
	private String inviterCode;

	public UserInfoResponse(Long id, Long mobileNumber, Integer source,
			Long regTime, Integer isMerchant, Integer isAdvertiser,
			String name, Integer gender, String idNumber, Integer isSetPaymentPassword, String inviterCode) {
		super();
		this.setId(id);
		this.mobileNumber = mobileNumber;
		this.source = source;
		this.regTime = regTime;
		this.isMerchant = isMerchant;
		this.isAdvertiser = isAdvertiser;
		this.name = name;
		this.gender = gender;
		this.idNumber = idNumber;
		this.isSetPaymentPassword = isSetPaymentPassword;
		this.inviterCode = inviterCode;
	}
	
}
