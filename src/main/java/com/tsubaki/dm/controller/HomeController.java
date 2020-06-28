package com.tsubaki.dm.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tsubaki.dm.model.DeviceBean;
import com.tsubaki.dm.model.DeviceForm;
import com.tsubaki.dm.model.FileBean;
import com.tsubaki.dm.service.DeviceService;
import com.tsubaki.dm.service.FileService;
import com.tsubaki.dm.service.VvsService;

@Controller
public class HomeController {

    @Autowired
    DeviceService uService;

    /**
     * ホーム画面のGET用メソッド
     */
    @GetMapping("/home")
    public String getHome(Model model) {

        //コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/home :: home_contents");

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

}
