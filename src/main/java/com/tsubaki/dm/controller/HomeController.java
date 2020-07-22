package com.tsubaki.dm.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.AppUserDetails;
import com.tsubaki.dm.model.User;
import com.tsubaki.dm.model.UserSignupForm;
import com.tsubaki.dm.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
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
    public String getHome(Model model, @AuthenticationPrincipal AppUserDetails user) {
    	
    	log.info("HomeController Start");
    	
    	// ログインユーザー情報の表示
    	log.info(user.toString());
    	
    	log.info("HomeController End");

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
    	
    	// ユーザーIDのチェック
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
    
    // ユーザー更新要処理
    @PostMapping(value = "/userDetail", params = "update")
    public String postUserDetailUpdate(@ModelAttribute UserSignupForm form, Model model) {
    	System.out.println("更新ボタンの処理");
    	
    	// Userインスタンスの生成
    	User user = new User();
    	
    	// フォームクラスをUserクラスに変換
    	user.setUserId(form.getUserId());
    	user.setPassword(form.getPassword());
    	user.setUserName(form.getUserName());
    	user.setBirthday(form.getBirthday());
    	user.setAge(form.getAge());
    	user.setMarriage(form.isMarriage());
    	
    	try {
        	// 更新実行
        	boolean result = userService.updateOne(user);
        	
        	if(result == true) {
        		model.addAttribute("result", "更新成功");
        	} else {
        		model.addAttribute("result", "更新失敗");
        	}
			
		} catch (DataAccessException e) {
			// TODO: handle exception
			model.addAttribute("result","更新失敗（トランザクションテスト）");
		}
    	
    	
    	// ユーザー一覧画面を表示
    	return getUserList(model);
    	
    }
    
    
    // ユーザー削除用処理
    @PostMapping(value = "userDetail",params = "delete")
    public String postUserDetailDelete(@ModelAttribute UserSignupForm form, Model model) {
    	System.out.println("削除ボタンの処理");
    	
    	// 削除実行
    	boolean result = userService.deleteOne(form.getUserId());
    	
    	if(result == true) {
    		model.addAttribute("result", "削除成功");
    	} else {
    		model.addAttribute("result", "削除失敗");
    	}
    	
    	// ユーザー一覧画面を表示
    	return getUserList(model);
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
    public ResponseEntity<byte[]> getUserListCsv(Model model) {
    	// ユーザーを全件取得して、CSVをサーバーに保存する
    	userService.userCsvOut();
    	
    	byte[] bytes = null;
    	
    	try {
    		// サーバーに保存されているuserList.csvをbyteで取得する。
    		bytes = userService.getFile("userList.csv");
    		
    	} catch (IOException e) {
			// TODO: handle exception
    		e.fillInStackTrace();
		}
    	
    	// HTTPヘッダの設定
    	HttpHeaders header = new HttpHeaders();
    	header.add("Content-Type","text/csv; charset=UTF8");
    	header.setContentDispositionFormData("fileName","userList.csv");
    	
    	// userList.csvを戻す
        return new ResponseEntity<>(bytes, header, HttpStatus.OK);
    }
    
    // アドミン権限専用のGET用メソッド
    @GetMapping("/admin")
    public String getAdmin(Model model) {
    	// コンテンツ部分にユーザー詳細を表示するための文字列を登録
    	model.addAttribute("contents","login/admin :: admin_contents");
    	
    	// レイアウト用テンプレート
    	return "dev/home_Layout";
    }

}
