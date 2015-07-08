package com.myapp.comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SuperDelegationAdapter {
	
	// SNS 로그인 확인 전역변수
	public static Boolean SNS_SESSION_LOGIN_YN = false;
	
	// 모바일 기기 확인 전역변수
	public static Boolean USER_AGENT_MOBILE_YN = false;
	
	// 사용자 IP
	public static String USER_IP = "";
	
	private static final Logger logger = LoggerFactory.getLogger(SuperDelegationAdapter.class);
	
	/**
	 * @Desc	: Execute JavaScript 
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public void execJavaScript ( String scripts, Map<String, Object> paramMap ) {
		HttpServletResponse response = (HttpServletResponse) paramMap.get("response");
		
		try {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(scripts);
			out.flush();
		} catch (IOException e) {
			throw new BusinessException("오류가 발생했습니다.\n잠시 후 다시 시도해주세요.");
		}
	}
	
	
	/**
	 * @Desc	: BusinessException 핸들러
	 * @Author	: 김성준
	 * @Create	: 2015년 07월 3일 
	 * @stereotype Utils
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleBusinessException(BusinessException ex, HttpServletResponse response) throws IOException {
		logger.info(ex.toString());
		ex.printStackTrace();
		
		response.setContentType("text/html; charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().write(ex.getMessage());
		response.flushBuffer();
	}
}
