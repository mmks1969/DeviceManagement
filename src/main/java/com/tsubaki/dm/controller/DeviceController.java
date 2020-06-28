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
public class DeviceController {

    @Autowired
    DeviceService deviceService;
    
    @Autowired
    FileService fileService;
    
    @Autowired
    private VvsService vvsService;
    
    // ラジオボタン（デバイス区分:deviceKbn）の変数
    private Map<String, String> radioDeviceKbn;
    
    // コンボボックス（メーカー:maker）の変数
    private Map<String, String> selectMaker;
    
    /**
     * ラジオボタン(デバイス区分:deviceKbn)の初期化メソッド
     * @return
     */
    private Map<String, String> initRadioDevice() {
    	
    	Map<String, String> radioDeviceMap = vvsService.getMap("deviceKbn");
    	
    	return radioDeviceMap;
    }   
    
    /**
     * コンボボックス(メーカー:maker)の初期化メソッド
     * @return
     */
    private Map<String, String> initSelectMaker() {
    	
    	// 返却用のMapの作成
    	Map<String, String> combMaker = vvsService.getMap("maker");
    	
    	return combMaker;
    }
    
   /**
     * ホーム画面のGET用メソッド
     */
//    @GetMapping("/home")
//    public String getHome(Model model) {

        //コンテンツ部分にユーザー詳細を表示するための文字列を登録
//        model.addAttribute("contents", "dev/home :: home_contents");

//        return "dev/homeLayout";
//    }

    /**
     * デバイス一覧画面のGETメソッド用処理.
     */
    @GetMapping("/deviceList")
    public String getDeviceList(Model model) {

        // コンテンツ部分にデバイス一覧を表示するための文字列を登録
        model.addAttribute("contents", "dev/deviceList :: deviceList_contents");

        // デバイス一覧の生成
        List<DeviceBean> deviceList = deviceService.selectMany();

        // Modelにデバイスリストを登録
        model.addAttribute("deviceList", deviceList);

        // データ件数を取得し、Modelにデータ件数を登録
        int count = deviceService.count();
        model.addAttribute("deviceListCount", count);

        return "dev/homeLayout";
    }

    /**
     * デバイス詳細画面のGETメソッド用処理
     * @param form
     * @param model
     * @param deviceId
     * @return
     */
    @GetMapping("/deviceDetail/{id}")
    public String getDeviceDetail(@ModelAttribute DeviceForm form, Model model, @PathVariable("id") String deviceId) {

    	// コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/deviceDetail :: devicelDetail_contents");

        // デバイスIDのチェック
        if (deviceId != null && deviceId.length() > 0) {
        	
            // デバイス情報を取得
            DeviceBean device = deviceService.selectOne(deviceId);

            // 添付ファイルの情報を取得
            List<FileBean> fileBeanList = fileService.selectMany(deviceId);
            
            form.setDeviceId(device.getDeviceId()); // デバイスID
            form.setDeviceKbn(device.getDeviceKbn()); // デバイス区分
            form.setKataban(device.getKataban()); // 形番
            form.setOwner(device.getOwner()); // 所有者
            form.setMaker(device.getMaker()); // メーカー
            form.setPurchaseDate(device.getPurchaseDate()); // 購入日
            form.setCreationdate(device.getCreationdate()); // 登録日時
            form.setLastupdate(device.getLastupdate()); // 更新日時

            // Modelに登録
            model.addAttribute("deviceForm", form);
            model.addAttribute("fileBeanList",fileBeanList);

        }

        return "dev/homeLayout";
    }
    
    /**
     * デバイス更新画面のGETメソッド用処理.
     * @param form
     * @param model
     * @param deviceId
     * @return
     */
    @GetMapping("/deviceUpdate/{id}")
    public String getDeviceUpdate(@ModelAttribute DeviceForm form, Model model, @PathVariable("id") String deviceId) {

        // コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/deviceUpdate :: deviceUpdate_contents");

        // 区分用ラジオボタンの初期化
        radioDeviceKbn = initRadioDevice();
        
        // ラジオボタン用のMapをModelに登録
        model.addAttribute("radioDeviceKbn",radioDeviceKbn);
        
        // メーカー用コンボボックスの初期化メソッド呼び出し
        selectMaker = initSelectMaker();
        
        // メーカー用コンボボックスのMapをModelに登録
        model.addAttribute("selectMaker", selectMaker);
        
       // デバイスIDのチェック
        if (deviceId != null && deviceId.length() > 0) {

            // デバイス情報を取得
            DeviceBean device = deviceService.selectOne(deviceId);

            // Userクラスをフォームクラスに変換
            form.setDeviceId(device.getDeviceId()); // デバイスID
            form.setDeviceKbn(device.getDeviceKbn()); // デバイス区分
            form.setKataban(device.getKataban()); // 形番
            form.setOwner(device.getOwner()); // 所有者
            form.setMaker(device.getMaker()); // メーカー
            form.setPurchaseDate(device.getPurchaseDate()); // 購入日

            // Modelに登録
            model.addAttribute("deviceForm", form);
        }

        return "dev/homeLayout";
    }

    /**
     * デバイス情報更新処理.
     */
    @PostMapping(value = "/deviceUpdate", params = "update")
    public String postDeviceUpdate(@ModelAttribute DeviceForm form, Model model) {

        System.out.println("更新ボタンの処理");
        // 現在時刻の取得
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");

        //Userインスタンスの生成
        DeviceBean device = new DeviceBean();

        //フォームクラスをUserクラスに変換
        device.setDeviceId(form.getDeviceId());
        device.setDeviceKbn(form.getDeviceKbn());
        device.setKataban(form.getKataban());
        device.setOwner(form.getOwner());
        device.setMaker(form.getMaker());
        device.setPurchaseDate(form.getPurchaseDate());
        device.setLastupdate(sdf.format(date));

        try {

            //更新実行
            boolean result = deviceService.updateOne(device);

            if (result == true) {
                model.addAttribute("result", "更新成功");
            } else {
                model.addAttribute("result", "更新失敗");
            }

        } catch(DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト!!)");

        }

        //デバイス一覧画面を表示
        return getDeviceList(model);
    }

    /**
     * デバイス削除用処理.
     */
    @PostMapping(value = "/deviceUpdate", params = "delete")
    public String postUserDetailDelete(@ModelAttribute DeviceForm form,
            Model model) {

        System.out.println("削除ボタンの処理");

        //削除実行
        boolean result = deviceService.deleteOne(form.getDeviceId());

        if (result == true) {

            model.addAttribute("result", "削除成功");

        } else {

            model.addAttribute("result", "削除失敗");

        }

        // デバイス一覧画面を表示
        return getDeviceList(model);
    }

    /**
     * デバイス更新キャンセル処理
     */
    @PostMapping(value = "/deviceUpdate", params = "cancel")
    public String postDeviceUpdateCancel(@ModelAttribute DeviceForm form,
    		Model model) {
    	
    	//ユーザー一覧画面を表示
    	return getDeviceList(model);
    }
    
    /**
     * ログアウト用処理.
     */
//    @PostMapping("/logout")
//    public String postLogout() {

        //ログイン画面にリダイレクト
//        return "redirect:/login";
//    }

    /**
     * デバイス一覧のCSV出力用処理.
     */
    @GetMapping("/deviceList/csv")
    public ResponseEntity<byte[]> getUserListCsv(Model model) {

        // デバイスを全件取得して、CSVをサーバーに保存する
        deviceService.deviceCsvOut();

        byte[] bytes = null;

        try {

            // サーバーに保存されているDeviceList.csvファイルをbyteで取得する
            bytes = deviceService.getFile("DeviceList.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // HTTPヘッダーの設定
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "text/csv; charset=UTF-8");
        header.setContentDispositionFormData("filename", "DviceList.csv");

        // DeviceList.csvを戻す
        return new ResponseEntity<>(bytes, header, HttpStatus.OK);
    }

    /**
     * アドミン権限専用画面のGET用メソッド.
     * @param model Modelクラス
     * @return 画面のテンプレート名
     */
    @GetMapping("/admin")
    public String getAdmin(Model model) {

        //コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/admin :: admin_contents");

        //レイアウト用テンプレート
        return "dev/homeLayout";
    }
}
