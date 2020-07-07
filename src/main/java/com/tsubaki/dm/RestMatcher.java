package com.tsubaki.dm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class RestMatcher implements RequestMatcher {
	
	// マッチャー
	private AntPathRequestMatcher matcher;
	
	// コンストラクタ
	public RestMatcher(String url) {
		super();
		matcher = new AntPathRequestMatcher(url);
	}
	
	// URLのマッチ条件
	@Override
	public boolean matches(HttpServletRequest request) {
		
		// GETメソッドならCSRFのチェックはしない
		if("GET".equals(request.getMethod())) {
			return false;
		}
		
		// 特定のURLに外装する場合、CSRFチェックしない
		if(matcher.matches(request)) {
			return false;
		}
		
		return true;
	}

}
