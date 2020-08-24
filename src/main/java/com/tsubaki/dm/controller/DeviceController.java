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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tsubaki.dm.model.DeviceBean;
import com.tsubaki.dm.model.DeviceForm;
import com.tsubaki.dm.model.FileBean;
import com.tsubaki.dm.service.DeviceService;
import com.tsubaki.dm.service.FileService;
import com.tsubaki.dm.service.VvsService;
import com.tsubaki.dm.util.DownloadImage;
import com.tsubaki.dm.util.ExternalPropertiesUtil;

@Controller
public class DeviceController {

    @Autowired
    DeviceService deviceService;
    
    @Autowired
    FileService fileService;
    
    @Autowired
    private VvsService vvsService;
    
	@Autowired
	DownloadImage downloadImage;

	@Autowired
	ExternalPropertiesUtil externalProp;

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
     * デバイス一覧画面のGETメソッド用処理
     * @param model
     * @return
     */
    @GetMapping("/deviceList")
    public String getDeviceList(Model model) {

        // コンテンツ部分にデバイス一覧を表示するための文字列を登録
        model.addAttribute("contents", "dev/deviceList2 :: deviceList_contents");

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
     * デバイス情報更新処理
     * @param form
     * @param model
     * @return
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
//        return getDeviceList(model);
        return getDeviceDetail(form, model, form.getDeviceId());
    }

    /**
     * デバイス削除用処理
     * @param form
     * @param model
     * @return
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
     * @param form
     * @param model
     * @return
     */
    @PostMapping(value = "/deviceUpdate", params = "cancel")
    public String postDeviceUpdateCancel(@ModelAttribute DeviceForm form,
    		Model model) {
    	
    	//ユーザー一覧画面を表示
    	return getDeviceList(model);
    }
    
    /**
     * デバイス一覧のCSV出力用処理
     * @param model
     * @return
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
     * PDF表示処理
     * @param form
     * @param model
     * @param httpSession
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    @PostMapping(value = "/pdfDisp")
    public String postPdfDisp(@ModelAttribute DeviceForm form
    		, Model model,HttpSession httpSession
			,HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam("key") String fileName) {

    	// コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "com/pdfDisp :: pdf_contents");
        model.addAttribute("p_key", fileName);
        
        // ファイル情報の取得
        FileBean fileBean = fileService.selectOne(fileName);
        
        model.addAttribute("fileInfo",fileBean);

        // デバイスIDのチェック
        if (fileName != null && fileName.length() > 0) {
        	System.out.println(fileName);
        }

    	//ユーザー一覧画面を表示
        return "dev/dispLayout";
    }
    
    /**
     * ファイル削除処理
     * ファイル名をキーに登録済みのファイルを削除する
     * @param form
     * @param model
     * @param fileName
     * @return
     */
    @GetMapping(value = "/fileDelete/{fileName}")
    public String getFileDelete(@ModelAttribute DeviceForm form, Model model, @PathVariable("fileName") String fileName) {

    	String deviceId = fileName.substring(0, 4);
    	
        //削除実行
        boolean result = fileService.deleteOne(fileName);

        if (result == true) {

            model.addAttribute("result", "削除成功");

        } else {

            model.addAttribute("result", "削除失敗");

        }

        // デバイス一覧画面を表示
//        return getDeviceList(model);
        return getDeviceDetail(form, model, deviceId);
    }

    
    /**
     * ファイル追加
     * @param form
     * @param model
     * @param fileName
     * @return
     */
    @PostMapping(value = "/fileAdd")
    public String postFileAdd(@ModelAttribute DeviceForm form, Model model, @RequestParam("deviceId") String deviceId) {
    	
    	List<MultipartFile> multipartFiles = form.getFile();
    	String orgName = multipartFiles.get(0).getOriginalFilename();
    	if(orgName.equals("")) {
    		model.addAttribute("message","ファイルを選択して追加ボタンを押下してください");
    		 return getDeviceDetail(form, model, deviceId);
    	}
    	
    	// 一つのデバイスに複数のファイルを付けることが出来る。
    	// ファイル名：deviceId_fileNo_revNo.拡張子
    	// 例：1020_0000_000.pdf
    	String fileNo = fileService.selectFileNo(deviceId);
        // fileNoをインクリメント
        fileNo = String.format("%04d", Integer.parseInt(fileNo) + 10);

        for (MultipartFile file : multipartFiles) {

        	// 拡張子のチェック
        	int dot = file.getOriginalFilename().lastIndexOf(".");
            String extString = file.getOriginalFilename().substring(dot).toLowerCase();
            
            // 選択されたファイルのアップロード
//        	String deviceId = form.getDeviceId();
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
        

        // デバイス一覧画面を表示
//        return getDeviceList(model);
        return getDeviceDetail(form, model, deviceId);
    }

}
