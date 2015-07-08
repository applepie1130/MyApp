package com.myapp.comm;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
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
		// Referer
		String sReferer = request.getHeader("REFERER");
		
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
		
		// 모바일 기기 확인
		SitePreference currentSitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
		if(currentSitePreference.isMobile()){
			USER_AGENT_MOBILE_YN = true;                  
		} else {
			USER_AGENT_MOBILE_YN = false;
		}
		
		// 사용자 IP 확인
		USER_IP = request.getHeader("X-FORWARDED-FOR");  
		if (USER_IP == null) {  
			USER_IP = request.getRemoteAddr();  
		}
		
		System.out.println("");
		System.out.println("==============PRE HANDLE==================");
		System.out.println("Referer\t\t: " + sReferer);
		System.out.println("LoginSession\t: " + mRtnCookieInfo.get("SNS_SESSION"));
		System.out.println("Login ?\t\t: " + SNS_SESSION_LOGIN_YN);
		System.out.println("isMobile ?\t: " + USER_AGENT_MOBILE_YN);
		System.out.println("Client IP ?\t: " + USER_IP);
		System.out.println("==============PRE HANDLE==================");
		System.out.println("");
		
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