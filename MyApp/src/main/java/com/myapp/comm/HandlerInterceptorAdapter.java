package com.myapp.comm;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HandlerInterceptorAdapter extends SuperDelegationAdapter implements HandlerInterceptor {
	
	/**
	 * @Desc	: 전처리기
	 * @Author	: 김성준
	 * @Create	: 2015년 07월 04일 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String sReferer = request.getHeader("REFERER");
		
		System.out.println("================");
		System.out.println("전처리기 실행 !!!!!!");
		System.out.println("Referer : " + sReferer);
		System.out.println("================");
		
		// SNS 로그인 확인 
		Map mCookieInfo = new HashMap<String, Object>();
		Map mRtnCookieInfo = new HashMap<String, Object>();
		
		mCookieInfo.put("cookieName", "SNS_SESSION");
		mRtnCookieInfo = RequestUtil.getCookie(mCookieInfo, request, response);
		
		if ( !StringUtil.isEmpty(ObjectUtils.toString(mRtnCookieInfo.get("SNS_SESSION"))) ) {
			SNS_SESSION_LOGIN_YN = true;
		} else {
			SNS_SESSION_LOGIN_YN = false;
		}
		
		System.out.println(mRtnCookieInfo.get("SNS_SESSION"));
		System.out.println("로그인 한 상태인가 ? : " + SNS_SESSION_LOGIN_YN);
		
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	};
}