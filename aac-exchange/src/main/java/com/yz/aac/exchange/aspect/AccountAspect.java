package com.yz.aac.exchange.aspect;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.exchange.repository.UserRepository;
import com.yz.aac.exchange.repository.domian.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.yz.aac.common.Constants.Misc.REQUEST_INFO_KEY;

/**
 * 交易模块用户相关事件
 *
 */

@Slf4j
@Order(1)
@Component
@Aspect
public class AccountAspect {
	
	@Autowired
	private UserRepository userRepository;

	@Around("@annotation(com.yz.aac.exchange.aspect.targetCustom.IdentityAuthenticationAspect)")
	public void IdentityAuthentication(ProceedingJoinPoint pjp) throws Throwable{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		LoginInfo loginInfo = (LoginInfo) request.getAttribute(REQUEST_INFO_KEY.value());
		if (null == loginInfo) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您还没有登录，请登录！");
		}
		User user = userRepository.getUserById(loginInfo.getId());

		if (null == user.getIdNumber() || "".equals(user.getIdNumber())) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您你还没实名认证，请实名认证！");
		}

	}
	
}
