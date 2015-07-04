package com.myapp.social;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
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
public class FacebookController extends CommonService {
	
	/*
	 * Define Service Variables
	 */
	@Autowired (required=false)
	private OAuth2Operations auth2Operations;
	
	@Autowired (required=false)
	private FacebookConnectionFactory connectionFactory;
	
	@Autowired (required=false)
	private ConnectionFactoryLocator factoryLocator;
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookController.class);
	
	/**
	 * @Desc	: 페이스북 로그인인증
	 * @Author	: 김성준
	 * @Create	: 2015년 06월 29일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/facebook/login", method = RequestMethod.GET)
	public void findFacebookLogin(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		connectionFactory = (FacebookConnectionFactory) factoryLocator.getConnectionFactory("facebook");
		auth2Operations = connectionFactory.getOAuthOperations();
		OAuth2Parameters parameters = new OAuth2Parameters();
		String sRedirectUrl = "http://localhost:8080/facebook/accessResult";
		
		parameters.setScope("email, user_about_me, user_birthday, user_hometown, user_website, read_stream, read_friendlists");
		parameters.setRedirectUri(sRedirectUrl);

		String authorizeUrl = auth2Operations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, parameters);
		
		try {
			response.sendRedirect(authorizeUrl);
		} catch (IOException e) {
			throw new BusinessException("페이스북 로그인 인증 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.");
		}
	}
	
	/**
	 * @Desc	: 페이스북 로그인인증
	 * @Author	: 김성준
	 * @Create	: 2015년 06월 29일 
	 * @stereotype Action
	 */
	@RequestMapping(value = "/facebook/accessResult", method = RequestMethod.GET)
	public String findFacebookAccessResult(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		String sAccessToken = ObjectUtils.toString(paramMap.get("code"));
		String sRedirectUrl = "http://localhost:8080/facebook/accessResult";
		String sReferer = request.getHeader("REFERER");
		
		AccessGrant accessGrant = auth2Operations.exchangeForAccess(sAccessToken, sRedirectUrl, null);
		Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
		Facebook facebook = (Facebook)(connection != null ? connection.getApi() : new FacebookTemplate());
		FacebookProfile userProfile = facebook.userOperations().getUserProfile();
		
		logger.info("======================");
		logger.info(userProfile.getEmail());
		logger.info(userProfile.getBirthday());
		logger.info(userProfile.getId());
		logger.info(userProfile.getFirstName());
		logger.info(userProfile.getName());
		logger.info(userProfile.getGender());
		logger.info(userProfile.toString());
		logger.info("======================");
		logger.info(sReferer);
		logger.info("======================");
		
		// redirect
		return "redirect:" + sReferer;
	}
}