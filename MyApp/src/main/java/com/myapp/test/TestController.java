package com.myapp.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.comm.BusinessException;
import com.myapp.comm.CommonConfig;
import com.myapp.comm.FileService;
import com.myapp.comm.MailService;
import com.myapp.comm.RequestUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TestController {
	
	/*
	 * Define Service Variables
	 */
	@Autowired (required=false)
	private TestService testSvc;
	@Autowired (required=false)
	private MailService mailSvc;
	@Autowired (required=false)
	private CommonConfig commonConfig;
	@Autowired (required=false)
	private FileService fileSvc;
	
	/**
	 * @Desc	: 메인페이지 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/")
	public ModelAndView userView(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List findUserList = testSvc.findUserList(paramMap);
		
		// writeFile
		String sFile = "/batch/data/MyApp/test/rUserList.json";
		fileSvc.writeFileToJSONList(sFile, findUserList);
		
		// redirect
		return new ModelAndView("redirect:/test");
	}
	
	/**
	 * @Desc	: 대표 url 리다이렉션 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test")
	public ModelAndView userView2(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		// readFile
		String sFile = "/batch/data/MyApp/test/rUserList.json";
		List findUserList = fileSvc.readFileFromJSONList(sFile);
		
		ModelAndView mv = new ModelAndView("test/index");
		mv.addObject("rData", findUserList);
		
		return mv;
	}
	
	/**
	 * @Desc	: 사용자 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/findUserList", method = RequestMethod.POST)
	public @ResponseBody List findUserList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List rUserList = testSvc.findUserList(paramMap);
		
		return rUserList;
	}
	
	/**
	 * @Desc	: 사용자 저장 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/saveUserInfo", method = RequestMethod.POST)
	public @ResponseBody Map saveUserInfo(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		List rParamData = (List) paramMap.get("rUserInfo");
		Map mRtnData = testSvc.saveUserInfo(rParamData, paramMap);
		
		return mRtnData;
	}

	/**
	 * @Desc	: RESTful 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/findRestTmpList", method = RequestMethod.POST)
	public @ResponseBody Map findRestTmpList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map mTmpData = new HashMap();
		Map mRtnData = new HashMap();
		String sFile = "/batch/data/MyApp/test/";
		
		if ( "json".equals(ObjectUtils.toString(paramMap.get("restType"))) ) {
			mTmpData.put("key", testSvc.findJSONList(paramMap));
			sFile += "restTmpFromJson.json";
		} else if ( "xml".equals(ObjectUtils.toString(paramMap.get("restType"))) ) { 
			mTmpData.put("key", testSvc.findXMLList(paramMap));
			sFile += "restTmpFromXml.json";
		}
		
		// writeFile
		fileSvc.writeFileToJSONMap(sFile, mTmpData);
		
		// readFile
		mRtnData = fileSvc.readFileFromJSONMap(sFile);
		
		return mRtnData;
	}
	
	/**
	 * @Desc	: DOM 클롤링 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/findNaverRealRankList", method = RequestMethod.POST)
	public @ResponseBody List findNaverRealRankList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		String sUrl = "http://www.naver.com";
		paramMap.put("url", sUrl);
		
		List rRtnData = testSvc.findNaverRealRankList(paramMap);
		
		return rRtnData;
	}
	
	/**
	 * @Desc	: 관리자 메일 발송
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/saveSendMail", method = RequestMethod.POST)
	public @ResponseBody Map saveSendMail(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map rRtnData  = mailSvc.sendMail(paramMap);
		
		return rRtnData;
	}
	
	/**
	 * @Desc	: 설정파일 조회 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/findBeanConfigure", method = RequestMethod.POST)
	public @ResponseBody Map findBeanConfigure(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		Map rRtnData = new HashMap<String, Object>();
		rRtnData.put("serviceShutDownStartDt", commonConfig.getServiceShutDownStartDt());
		rRtnData.put("serviceShutDownEndDt", commonConfig.getServiceShutDownEndDt());
		
		return rRtnData;
	}
	
	/**
	 * @Desc	: 예외처리 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 16일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/findExceptionTest", method = RequestMethod.POST)
	public void findExceptionTest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		
		throw new BusinessException("오류가 발생했습니다.\n잠시 후 다시 시도해주세요.", paramMap);
	}
	
	/**
	 * @Desc	: 쿠키 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 23일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/saveCookieTest", method = RequestMethod.POST)
	public @ResponseBody Map saveCookieTest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		Map rRtnData = new HashMap<String, Object>();
		
		// 쿠키생성
		if ( !RequestUtil.setCookie(paramMap, request, response) ) {
			throw new BusinessException("쿠키생성 중 발생했습니다.\n잠시 후 다시 시도해주세요.", paramMap);
		}
		
		// 쿠키읽기
		rRtnData = RequestUtil.getCookie(paramMap, request, response);
		
		return rRtnData;
	}
	
	/**
	 * @Desc	: 런타임 커맨드 실행 테스트
	 * @Author	: 김성준
	 * @Create	: 2015년 05월 23일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/test/findRuntimeExecTest", method = RequestMethod.POST)
	public @ResponseBody Map findRuntimeExecTest(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		paramMap = RequestUtil.getParameter(paramMap, request, response);
		Map rRtnData = new HashMap<String, Object>();
		
		// 런타임 커맨드 실행
		String sCommand = ObjectUtils.toString(paramMap.get("command"));
		
		try {
			Process process = Runtime.getRuntime().exec(sCommand);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String sCommandLine = null;
		    
		    while( (sCommandLine=br.readLine()) != null){
		    	System.out.println(sCommandLine);
		    }
		    
		    process.getErrorStream().close();
			process.getOutputStream().close();
		    process.getInputStream().close();
		    process.waitFor();
		    
		} catch (Exception e) {
			throw new BusinessException("커맨드 실행 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.", paramMap);
		}
		
		return rRtnData;
	}
}