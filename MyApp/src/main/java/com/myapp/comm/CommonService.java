package com.myapp.comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonService {
	@Autowired
	private SqlSession session;
	
	/**
	 * @Desc	: DB Table Select Query
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public List queryForListData ( String queryId, Map condition ) {
		List rResultList = null;
		rResultList = session.selectList(queryId, condition);
		return rResultList;
	}
	
	/**
	 * @Desc	: DB Table Update Query
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Utils
	 */
	public int update ( String queryId, Map condition ) {
		int nInsert = session.insert(queryId, condition);
		return nInsert;
	}
	
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
			throw new BusinessException("오류가 발생했습니다.\n잠시 후 다시 시도해주세요.", paramMap);
		}

	}
}
