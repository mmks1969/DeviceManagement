package com.tsubaki.dm.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.GroupOrder;
import com.tsubaki.dm.model.User;
import com.tsubaki.dm.model.UserSignupForm;
import com.tsubaki.dm.service.UserService;

@Controller
public class UserSignupController {
	
	@Autowired
	private UserService userService;

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
		
		// insert用変数
		User user = new User();
		
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());
		
		// ユーザー登録処理
		boolean result = userService.insert(user);
		
		// ユーザー登録結果の判定
		if(result == true) {
			System.out.println("insert成功");
		} else {
			System.out.println("insert失敗");
		}
		
		// login.htmlにリダイレクト
		return "redirect:/login";
	}
	
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {
		
		// 例外クラスのメッセージをモデルに登録
		model.addAttribute("error", "内部サーバーエラー（DB）:ExceptionHandler");
		
		// 例外クラスのメッセージをモデルに登録
		model.addAttribute("message","UserSignupControllerでDataAccessExceptionが発生しました");
		
		// HTTPのエラーコード(500)をModelに登録
		model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);
		
		return "error";
		
	}
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		// 例外クラスのメッセージをモデルに登録
		model.addAttribute("error", "内部サーバーエラー:ExceptionHandler");
		
		// 例外クラスのメッセージをモデルに登録
		model.addAttribute("message","UserSignupControllerでExceptionが発生しました");
		
		// HTTPのエラーコード(500)をModelに登録
		model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);
		
		return "error";

	}
	
}
