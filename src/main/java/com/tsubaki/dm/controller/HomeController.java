package com.tsubaki.dm.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.User;
import com.tsubaki.dm.model.UserSignupForm;
import com.tsubaki.dm.service.DeviceService;
import com.tsubaki.dm.service.UserService;

@Controller
public class HomeController {

    @Autowired
    UserService userService;
    
    // 結婚ステータスのラジオボタン用変数
    private Map<String, String> radioMarriage;
    
    // ラジオボタンの初期化メソッド（ユーザー登録画面と同じ）
    private Map<String, String> initRadioMarriage(){
    	Map<String, String> radioMap =new LinkedHashMap<>();
    	
    	// 既婚、未婚をMapに格納
    	radioMap.put("既婚", "true");
    	radioMap.put("未婚", "false");
    	
    	return radioMap;
    }

    /**
     * ホーム画面のGET用メソッド
     */
    @GetMapping("/home")
    public String getHome(Model model) {

        //コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/home :: home_contents");

        return "dev/homeLayout";
    }

    // ユーザー一覧画面のGET用メソッド
    @GetMapping("/userList")
    public String getUserList(Model model) {
    	// コンテンツ部分にユーザー一覧を表示するための文字列を登録
    	model.addAttribute("contents","login/userList :: userList_contents");
    	
    	// ユーザー一覧の生成
    	List<User> userList = userService.selectMany();
    	
    	// Modelにユーザーリストを登録
    	model.addAttribute("userList",userList);
    	
    	// データー件数を取得
    	int count = userService.count();
    	model.addAttribute("userListCount",count);
    	
    	return "dev/homeLayout";
    }
    
    @GetMapping("/userDetail/{id:.+}")
    public String getUserDetail(@ModelAttribute UserSignupForm form, Model model, @PathVariable("id")String userId) {
    	// ユーザーID認識（デバッグ）
    	// コンテンツ部分にユーザー詳細を表示するための文字列を登録
    	model.addAttribute("contents", "login/userDetail :: userDetail_contents");
    	
    	// 結婚ステータス用ラジオボタンの初期化
    	radioMarriage = initRadioMarriage();
    	
    	// ラジオボタン用のMapをModelに登録
    	model.addAttribute("radioMarriage", radioMarriage);
    	
    	// ーザーIDのチェック
    	if (userId != null && userId.length() > 0) {
    		// ユーザー情報を取得
    		User user = userService.selectOne(userId);
    		
    		// Userクラスをフォームクラスに変換
    		form.setUserId(user.getUserId());
    		form.setUserName(user.getUserName());
    		form.setBirthday(user.getBirthday());
    		form.setAge(user.getAge());
    		form.setMarriage(user.isMarriage());
    		
    		// Modelに登録
    		model.addAttribute("userSignupForm", form);
    	}
    	return "dev/homeLayout";
    }
    
    /**
     * ログアウト用処理.
     */
    @PostMapping("/logout")
    public String postLogout() {

        //ログイン画面にリダイレクト
        return "redirect:/login";
    }
    
    // ユーザー一覧のCSV出力用メソッド
    @GetMapping("/userList/csv")
    public String getUserListCsv(Model model) {
    	return getUserList(model);
    }

}
