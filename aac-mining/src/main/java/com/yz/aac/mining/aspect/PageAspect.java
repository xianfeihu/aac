package com.yz.aac.mining.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.mining.model.PageResult;

/**
 * 系统分页数据封装
 *
 */

@Order(40)
@Component
@Aspect
public class PageAspect {

	 /**
	  * controller 分页数据设置下一页数据请求路径
	  * @param pjp
	  * @return
	  * @throws Throwable
	  */
	@SuppressWarnings("unchecked")
	@Around("@annotation(com.yz.aac.mining.aspect.targetCustom.PageControllerAspect)")
	public RootResponse<PageResult<Object>> idCardAuth(ProceedingJoinPoint pjp) throws Throwable{
		 HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		 
		 RootResponse<PageResult<Object>> result = (RootResponse<PageResult<Object>>) pjp.proceed();
		 PageResult<Object> pageResult = result.getContent();
		 if (null != pageResult) {
			 pageResult.setNextPagePath(request.getRequestURI() + "?" + request.getQueryString());
			 pageResult.buildNextPagePath();
			 result.setContent(pageResult);
		 }
		 
		 return result;
	}

}
