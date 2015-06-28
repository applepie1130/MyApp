package com.myapp.comm;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class BusinessException extends RuntimeException {
	
	static final long serialVersionUID = -1L;
	
	public BusinessException(String sErrorMessage, Map<String, Object> paramMap) {
		try {
			HttpServletResponse response = (HttpServletResponse) paramMap.get("response");
			
			response.setContentType("text/html; charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(sErrorMessage);
			response.flushBuffer();
			
		} catch (IOException e) {
			throw new BusinessException("오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.", paramMap);
		}
	}
}
