package com.tsubaki.dm;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tsubaki.dm.model.AppUserDetails;
import com.tsubaki.dm.service.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Component("SuccessHandler")
@Slf4j
public class SuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	UserDetailsServiceImpl service;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException{
		
		log.info("ログイン成功イベント開始");
		
		// ユーザー情報の取得
		AppUserDetails user = (AppUserDetails)SecurityContextHolder
											.getContext()
											.getAuthentication()
											.getPrincipal();
		
		log.info(user.toString());
		
		String redirectPath = request.getContextPath();
		
		// ログイン失敗回数をリセット
		service.updateLoginMissTimes(user.getUserId());
		
		// パスワード更新日付のチェック
		if (user.getPassUpdateDate().after(new Date())) {
			// パスワード期限が切れていない
			log.info("遷移先：ホーム");
			redirectPath += "/home";
				
		} else {
			// パスワード有効期限切れ
			log.info("遷移先：パスワード変更");
			redirectPath += "/password/change";
			
		}
		
		log.info("ログイン成功イベント終了");
		
		// リダイレクト
		response.sendRedirect(redirectPath);
				
	}

}
