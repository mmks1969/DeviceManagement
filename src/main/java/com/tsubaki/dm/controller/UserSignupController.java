package com.tsubaki.dm.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.GroupOrder;
import com.tsubaki.dm.model.UserSignupForm;

@Controller
public class UserSignupController {

	// ラジオボタンの実装
	private Map<String, String> radioMarriage;
	
	// ラジオボタンの初期化メソッド
	private Map<String, String> initRadioMarrige(){
		Map<String, String> radioMap = new LinkedHashMap<>();
		// 既婚、未婚をMapに格納
		radioMap.put("既婚", "true");
		radioMap.put("未婚", "false");
		return radioMap;
	}
	
	// ユーザー登録画面のGET用コントローラ
	@GetMapping("/userSignup")
	public String getUserSignup(@ModelAttribute UserSignupForm form, Model model) {
		// ラジオボタンの初期化メソッド呼び出し
		radioMarriage = initRadioMarrige();
		
		// ラジオボタン用のMapをModelに登録
		model.addAttribute("radioMarriage", radioMarriage);
		
		// userSignup.htmlに画面遷移
		return "login/userSignup";
	}
	
	// ユーザー登録画面のPOSTコントローラ
	@PostMapping("/userSignup")
	public String postUserSignup(@ModelAttribute @Validated(GroupOrder.class) UserSignupForm form, BindingResult bindingResult, Model model) {
		
		// 入力チェックに引っかかった場合、ユーザー登録画面に戻る
		if(bindingResult.hasErrors()) {
			// GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
			return getUserSignup(form, model);
		}
		
		// formの中身をコンソールに出して確認する
		System.out.println(form);
		
		// login.htmlにリダイレクト
		return "redirect:/login";
	}
	
}
