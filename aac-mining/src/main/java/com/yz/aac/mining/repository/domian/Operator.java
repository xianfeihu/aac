package com.yz.aac.mining.repository.domian;

import lombok.Data;

@Data
public class Operator {

	/** ID */
	private Long id;
	
	/** 账号 */
	private String loginName;
	
	/** 密码 */
	private String password;
	
	/** 姓名  */
	private String name;
	
	/** 部门  */
	private String department;
	
	/** 状态（1-启用 2-停用）  */
	private Integer status;
	
	/** 角色（1-普通用户 2-超级管理员） */
	private Integer role;
	
	/** 创建时间  */
	private Long createTime;
	
	/** 最近更新时间 */
	private Long updateTime;

}
