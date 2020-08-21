package com.tsubaki.dm.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.tsubaki.dm.model.DeviceBean;
import com.tsubaki.dm.model.DeviceForm;
import com.tsubaki.dm.model.FileBean;
import com.tsubaki.dm.model.GroupOrder;
import com.tsubaki.dm.service.DeviceService;
import com.tsubaki.dm.service.FileService;
import com.tsubaki.dm.service.VvsService;
import com.tsubaki.dm.util.ExternalPropertiesUtil;

@Controller
public class SignupController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private FileService fileService;
    
    @Autowired
    private VvsService vvsService;
    
    @Autowired
    private DeviceController homeController;
    
	@Autowired
	ExternalPropertiesUtil externalProp;

	// ラジオボタン（デバイス区分:deviceKbn）の変数
    private Map<String, String> radioDeviceKbn;
    
    // コンボボックス（メーカー:maker）の変数
    private Map<String, String> selectMaker;
    
    // 初期ID
    private String initId;
    
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
     * deviceIdの初期化メソッド.
     */
    private String initDeviceId() {
    	return deviceService.getMaxNo();
    }
    
    /**
     * デバイス登録画面のGETメソッド用処理.
     */
    @GetMapping("/signup")
    public String getSignUp(@ModelAttribute DeviceForm form, Model model) {

        // コンテンツ部分にデバイス一覧を表示するための文字列を登録
        model.addAttribute("contents", "dev/deviceSignup :: signup_contents");

        // デバイス区分用ラジオボタンの初期化メソッド呼び出し
        radioDeviceKbn = initRadioDevice();

        // ラジオボタン用のMapをModelに登録
        model.addAttribute("radioDeviceKbn", radioDeviceKbn);
        
        // メーカー用コンボボックスの初期化メソッド呼び出し
        selectMaker = initSelectMaker();
        
        // メーカー用コンボボックスのMapをModelに登録
        model.addAttribute("selectMaker", selectMaker);
        
        // IDの初期化
        initId = initDeviceId();
        
        // 初期IDをModelに登録
        form.setDeviceId(initId);

        return "dev/homeLayout";
    }

    /**
     * デバイス登録画面のPOSTメソッド用処理.
     */
    @PostMapping("/signup")
    public String postSignUp(@ModelAttribute @Validated(GroupOrder.class) DeviceForm form,
            BindingResult bindingResult,
            Model model) {

        // 入力チェックに引っかかった場合、ユーザー登録画面に戻る
        if (bindingResult.hasErrors()) {

            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻ります
            return getSignUp(form, model);

        }

        // 現在時刻の取得（登録日時・更新日時）
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        System.out.println(sdf.format(date));
        
        // formの中身をコンソールに出して確認します
        System.out.println(form);

        // insert用変数（デバイス情報）
        DeviceBean deviceBean = new DeviceBean();

        deviceBean.setDeviceId(initDeviceId());					// デバイスID
        deviceBean.setDeviceKbn(form.getDeviceKbn());		// デバイス区分
        deviceBean.setKataban(form.getKataban());			// 形番
        deviceBean.setOwner(form.getOwner());				// 所有者
        deviceBean.setMaker(form.getMaker());				// メーカー
        deviceBean.setPurchaseDate(form.getPurchaseDate());	// 購入日
        deviceBean.setCreationdate(sdf.format(date));		// 登録日時　初回登録なので登録日時と更新日時は同じ
        deviceBean.setLastupdate(sdf.format(date));				// 更新日時　初回登録なので登録日時と更新日時は同じ

        // デバイス登録処理
        boolean result = deviceService.insert(deviceBean);

        // デバイス登録結果の判定
        if (result == true) {
            System.out.println("insert成功");
        } else {
            System.out.println("insert失敗");
        }

        // 添付ファイルがあれば、ファイルを登録する。
        if (chkExistFiles(form)) {
            // ファイルをVaultLocに配置する
            saveFiles(form);
        }

        return homeController.getDeviceList(model);
    }
    
    /**
     * 添付ファイルの存在チェック
     * @param form
     * @return chkFlg
     */
    private boolean chkExistFiles(DeviceForm form) {
		boolean chkFlg = false;
		
    	// ファイルが添付されているかのチェック
		List<MultipartFile> multipartFiles = form.getFile();
		String fileName = multipartFiles.get(0).getOriginalFilename();
    	if(!fileName.equals("")) {
    		chkFlg = true;
    	}

		return chkFlg;
	}
    
    /**
     * ファイルの登録処理
     * @param form
     */
    private void saveFiles(DeviceForm form) {
    	List<MultipartFile> multipartFiles = form.getFile();
    	
    	// 一つのデバイスに複数のファイルを付けることが出来る。
    	// ファイル名：deviceId_fileNo_revNo.拡張子
    	// 例：1020_0000_000.pdf
    	String fileNo = "0000";
    	
        for (MultipartFile file : multipartFiles) {

        	// 拡張子のチェック
        	int dot = file.getOriginalFilename().lastIndexOf(".");
            String extString = file.getOriginalFilename().substring(dot).toLowerCase();
            
            // 選択されたファイルのアップロード
        	String deviceId = form.getDeviceId();
        	String filepath = externalProp.get("FILE_PATH");
            Path uploadfile = Paths.get(filepath + deviceId + "_" + fileNo + "_000" + extString);
            try (OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
              byte[] bytes = file.getBytes();
              os.write(bytes);
            } catch (IOException e) {
              //エラー処理は省略
            }

        	FileBean fileBean = new FileBean();
            fileBean.setDeviceId(deviceId);
            fileBean.setFileName(deviceId + "_" + fileNo + "_000" + extString);
            fileBean.setOriginalFileName(file.getOriginalFilename());

            // デバイス登録処理
            boolean result = fileService.insert(fileBean);
            
            // デバイス登録結果の判定
            if (result == true) {
                System.out.println("file insert成功");
            } else {
                System.out.println("file insert失敗");
            }
            
            // 次ファイルのfileNoをインクリメント
            fileNo = String.format("%04d", Integer.parseInt(fileNo) + 10);
      }
        
    }

    /**
     * DataAccessException発生時の処理メソッド.
     */
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model) {

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("error", "内部サーバーエラー（DB）：ExceptionHandler");

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("message", "SignupControllerでDataAccessExceptionが発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }

    /**
     * Exception発生時の処理メソッド.
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("error", "内部サーバーエラー：ExceptionHandler");

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("message", "SignupControllerでExceptionが発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }
}