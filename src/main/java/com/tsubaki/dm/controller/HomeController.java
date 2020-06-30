package com.tsubaki.dm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.User;
import com.tsubaki.dm.service.DeviceService;
import com.tsubaki.dm.service.UserService;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

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
