package com.myapp.social;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.myapp.comm.BusinessException;
import com.myapp.comm.CommonService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TwiiterController extends CommonService {
	
	/*
	 * Define Service Variables
	 */
	@Autowired (required=false)
	private OAuth2Operations auth2Operations;
	
	@Autowired (required=false)
	private TwitterConnectionFactory connectionFactory;
	
	@Autowired (required=false)
	private ConnectionFactoryLocator factoryLocator;
	
	@Autowired (required=false)
	private ConnectionRepository connectionRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(TwiiterController.class);
	
	/**
	 * @Desc	: 트위터 로그인인증
	 * @Author	: 김성준
	 * @Create	: 2015년 07월 11일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/twitter/login", method = RequestMethod.GET)
	public void findTwiiterLogin(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		connectionFactory = (TwitterConnectionFactory) factoryLocator.getConnectionFactory(Twitter.class);
		auth2Operations = (OAuth2Operations) connectionFactory.getOAuthOperations();
		OAuth2Parameters parameters = new OAuth2Parameters();
		String sRedirectUrl = "http://58.232.121.39/twitter/accessResult";
		
		parameters.setRedirectUri(sRedirectUrl);
		
		String authorizeUrl = auth2Operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, parameters);
		
		try {
			response.sendRedirect(authorizeUrl);
		} catch (IOException e) {
			throw new BusinessException("트위터 로그인 인증 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
		}
	}
	
	/**
	 * @Desc	: 트위터 로그인인증
	 * @Author	: 김성준
	 * @Create	: 2015년 07월 11일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/twitter/accessResult", method = RequestMethod.GET)
	public String findTwitterAccessResult(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		String sAccessToken = ObjectUtils.toString(paramMap.get("code"));
		String sRedirectUrl = "http://58.232.121.39/twitter/accessResult";
		String sReferer = request.getHeader("REFERER");
		
//		AccessGrant accessGrant = auth2Operations.exchangeForAccess(sAccessToken, sRedirectUrl, null);
//		Connection<Twitter> connection = connectionFactory.createConnection(accessGrant);
//		Twitter twitter = (Twitter)(connection != null ? connection.getApi() : new FacebookTemplate());
//		TwitterProfile userProfile = twitter.userOperations().getUserProfile();
		
//		logger.info("======================");
//		logger.info(userProfile.getName());
//		System.out.println(userProfile.getId());
//		logger.info(userProfile.getProfileUrl());
//		logger.info(userProfile.toString());
//		logger.info("======================");
//		logger.info(sReferer);
//		logger.info("======================");
//		
//		// 트위터 세션 키 암호화 (Bcrypt)
//		String sSessionKey = "T" + userProfile.getId() + System.currentTimeMillis();
//		String sEncodedSessionKey = BCrypt.hashpw(sSessionKey, BCrypt.gensalt(12));
//
//		// 트위터 로그인 세션 쿠키생성
//		Map mCookieInfo = new HashMap<String, Object>();
//		mCookieInfo.put("cookieName", "SNS_SESSION");
//		mCookieInfo.put("cookieValue", sEncodedSessionKey);
//		mCookieInfo.put("cookieExpire", -1);
//		mCookieInfo.put("cookiePath", "/");
//		
//		System.out.println("세션키 " + sSessionKey);
//		System.out.println("암호화 세션키 " + sEncodedSessionKey);
//		
//		if ( !RequestUtil.setCookie(mCookieInfo, request, response)) {
//			throw new BusinessException("쿠키생성 중 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
//		}
		
		// redirect
		return "redirect:" + sReferer;
	}
}