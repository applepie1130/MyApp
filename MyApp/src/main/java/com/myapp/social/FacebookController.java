package com.myapp.social;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.myapp.comm.BusinessException;
import com.myapp.comm.CommonConfig;
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
	private CommonConfig commonConfig;
	
	@Autowired (required=false)
	private OAuth2Operations auth2Operations;
	
	@Autowired (required=false)
	private FacebookConnectionFactory connectionFactory;
	
	@Autowired (required=false)
	private ConnectionFactoryLocator factoryLocator;
	
	/**
	 * @Desc	: 페이스북 로그인인증
	 * @Author	: 김성준
	 * @Create	: 2015년 06월 29일 
	 * @stereotype Action
	 */
	@ResponseBody
	@RequestMapping(value = "/facebook/login", method = RequestMethod.GET)
	public void findFacebookLogin(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		connectionFactory = (FacebookConnectionFactory) factoryLocator.getConnectionFactory("facebook");
		auth2Operations = connectionFactory.getOAuthOperations();
		OAuth2Parameters parameters = new OAuth2Parameters();
		
		//email, user_about_me, user_birthday, user_hometown, user_website, offline_access, read_stream, publish_stream, read_friendlists
		parameters.setScope("user_about_me");
		parameters.setRedirectUri("http://localhost:8080/facebook/accessResult");

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
	@ResponseBody
	@RequestMapping(value = "/facebook/accessResult", method = RequestMethod.GET)
	public void findFacebookAccessResult(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		
		String sAccessToken = ObjectUtils.toString(paramMap.get("code"));
		
		AccessGrant accessGrant = auth2Operations.exchangeForAccess(sAccessToken, "http://localhost:8080/facebook/accessResult", null);
		Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
 
		Facebook facebook = (Facebook)(connection != null ? connection.getApi() : new FacebookTemplate());
		FacebookProfile profile = facebook.userOperations().getUserProfile();
		
		System.out.println("-----------------------");
		System.out.println("Result!!!");
		System.out.println(profile.getUsername());
		System.out.println(paramMap);
		System.out.println("-----------------------");
		
//		return "facebook/index";
	}
	
//	/**
//	 * @Desc	: 페이스북 로그인인증
//	 * @Author	: 김성준
//	 * @Create	: 2015년 06월 29일 
//	 * @stereotype Action
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/facebook/facebookAccessSign", method = RequestMethod.GET)
//	public void findFacebookSign(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
//		paramMap = RequestUtil.getParameter(paramMap, request, response);
//		
//		String sAppId = commonConfig.getSocialFacebookId();
//		String sRedirectUri = "http://localhost:8080/facebook/facebookAccessToken?scope=publish_stream,offline_access";
//		String sDialUrl = "http://www.facebook.com/dialog/oauth?client_id=" + sAppId + "&redirect_uri=" + sRedirectUri;
//		StringBuffer strBuff = new StringBuffer();
//		strBuff.append(sDialUrl);
//		
//		try {
//			response.sendRedirect(sRedirectUri);
//		} catch (IOException e) {
//			throw new BusinessException("페이스북 로그인 인증 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.", paramMap);
//		}
//	}
//	
//	/**
//	 * @Desc	: 페이스북 로그인인증
//	 * @Author	: 김성준
//	 * @Create	: 2015년 06월 29일 
//	 * @stereotype Action
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/facebook/facebookAccessToken", method = RequestMethod.GET)
//	public void findFacebookSAccessToken(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
//		paramMap = RequestUtil.getParameter(paramMap, request, response);
//		
//		String sErrorReason = ObjectUtils.toString(paramMap.get("error_reason"));
//		String sError = ObjectUtils.toString(paramMap.get("error"));
//		String sErrorDescription = ObjectUtils.toString(paramMap.get("error_description"));
//		
//		String sCode = ObjectUtils.toString(paramMap.get("code"));
//		String sRedirectUri = "http://localhost:8080/facebook/loginResult";
//		String sUrl = "https://graph.facebook.com/oauth/access_token"+
//						"?client_id" + commonConfig.getSocialFacebookId() +
//						"&client_secret=" + commonConfig.getSocialFacebookKey() + 
//						"&redirect_uri=" + sRedirectUri +
//						"&code=" + sCode;
//		
//		HttpGet httpGet = new HttpGet(sUrl);
//		DefaultHttpClient httpClient = new DefaultHttpClient();
//		String sResult = "";
//		String sAccessToken = "";
//		
//		try {
//			sResult = httpClient.execute(httpGet, new BasicResponseHandler());
//			sAccessToken = sResult.split("&")[0].replaceFirst("access_token", "");
//			
//		} catch (ClientProtocolException e) {
//			throw new BusinessException("페이스북 로그인 인증 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.", paramMap);
//		} catch (IOException e) {
//			throw new BusinessException("페이스북 로그인 인증 중 오류가 발생했습니다.<br/>잠시 후 다시 시도해주세요.", paramMap);
//		}
//		
//		// 토큰저장
//		this.storeAccessToken(request, sAccessToken);
//	}
//	
//	
//	/**
//	 * @Desc	: 페이스북 로그인인증 결과페이지
//	 * @Author	: 김성준
//	 * @Create	: 2015년 06월 29일 
//	 * @stereotype Action
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/facebook/loginResult", method = RequestMethod.GET)
//	public String findLoginResult(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
//		paramMap = RequestUtil.getParameter(paramMap, request, response);
//		
//		System.out.println("------------------------");
//		System.out.println(paramMap);
//		System.out.println("------------------------");
//		
//		return "OK";
//	}
//	
//
//	/**
//	 * @Desc	: 페이스북 토큰저장
//	 * @Author	: 김성준
//	 * @Create	: 2015년 06월 29일 
//	 * @stereotype Action
//	 */
//	private void storeAccessToken(HttpServletRequest request, String accessToken) {
//		request.getSession().setAttribute("SOCIAL_FACEBOOK_ACCESS_TOKEN", accessToken);
//	}
}