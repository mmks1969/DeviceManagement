package com.tsubaki.dm.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.AppUserDetails;
import com.tsubaki.dm.model.PasswordForm;
import com.tsubaki.dm.service.UserDetailsServiceImpl;


@Controller
public class PasswordChangeController {
	
	@Autowired
	UserDetailsServiceImpl service;
	
	// パスワード変更画面の表示
	@GetMapping("/password/change")
	public String getPasswordChange(Model model, @ModelAttribute PasswordForm form) {
		return "/login/password_change";
	}
	
	// パスワード変更
	@PostMapping("/password/change")
	public String postPasswordChange(Model model
			, @ModelAttribute PasswordForm form
			, @AuthenticationPrincipal AppUserDetails user) throws ParseException{
		
		service.updatePasswordDate(user.getUserId(), form.getPassword());
		
        //コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/home :: home_contents");

		return "com/homeLayout";
		
	}
	

}
