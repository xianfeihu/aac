package com.yz.aac.exchange.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel("分页信息响应")
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> {

	@ApiModelProperty(value = "当前页码<从1开始>", position = 1)
	private Integer pageNo;
	
	@ApiModelProperty(value = "每页条数", position = 2)
	private Integer pageSize;
	
	@ApiModelProperty(value = "总共条数", position = 3)
	private Long pageTotal;
	
	@ApiModelProperty(value = "总共页码", position = 4)
	private Integer pageNum;
	
	@ApiModelProperty(value = "下一页数据请求地址", position = 5)
	private String nextPagePath;
	
	@ApiModelProperty(value = "返回数据集合", position = 6)
	private List<T> result;

	public PageResult(Integer pageNo, Integer pageSize, Long pageTotal,
			Integer pageNum, List<T> result) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.pageTotal = pageTotal;
		this.pageNum = pageNum;
		this.result = result;
	}
	
	/**
	 * 构建下一页请求路径
	 */
	public void buildNextPagePath(){
		if (null != pageNo && null != pageNum && pageNum > pageNo) {
			nextPagePath = nextPagePath.replace("pageNo=" + pageNo, "pageNo=" + (pageNo +1));
		} else {
			nextPagePath = null;
		}
	}
	
}
