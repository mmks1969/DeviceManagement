package com.tsubaki.dm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tsubaki.dm.model.DeviceForm;
import com.tsubaki.dm.model.VvsBean;
import com.tsubaki.dm.model.VvsForm;
import com.tsubaki.dm.service.VvsService;

@Secured("IS_AUTHENTICATED_FULLY")
@Controller
public class MasterController {
	
	@Autowired
	VvsService vvsService;
	
    // 初期ID
    private int initId;

    @GetMapping("/vvsList")
    public String getVvsList(Model model, @RequestParam("kbn") String usedAttibute) {
    	
    	System.out.println("kbn="+usedAttibute);
    	
    	// コンテンツ部分にデバイス区分一覧を表示するための文字列を登録
    	model.addAttribute("contents", "dev/vvsList :: vvsList_contents");
    	
    	// デバイス区分リスト一覧の生成
    	List<VvsBean> deviceKbnList = vvsService.selectAttribute(usedAttibute);
    	
    	// ModelにdeviceKbnListを登録
    	model.addAttribute("kbnList",deviceKbnList);
    	
    	// データ件数を取得し、Modelにデータ件数を登録
    	int count = vvsService.count(usedAttibute);
    	model.addAttribute("kbnListCount", count);
    	model.addAttribute("kbn",usedAttibute);
    	
    	return "dev/homeLayout";
    	
    }
    
    /**
     * 詳細画面のGETメソッド用処理
     * @param form
     * @param model
     * @param deviceId
     * @return
     */
    @GetMapping("/vvsDetail/{id}")
    public String getVvsDetail(@ModelAttribute VvsForm form, Model model, @PathVariable("id") int vvsId) {

    	// コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/vvsDetail :: vvsDetail_contents");

        VvsBean vvsBean = vvsService.selectOne(vvsId);

        // Modelに登録
        model.addAttribute("vvsForm", vvsBean);

        return "dev/homeLayout";
    }
    
    /**
     * vvs更新画面のGETメソッド用処理
     * @param form
     * @param model
     * @param vvsId
     * @return
     */
    @GetMapping("/vvsUpdate/{id}")
    public String getDeviceUpdate(@ModelAttribute VvsForm form, Model model, @PathVariable("id") int vvsId) {

        // コンテンツ部分にユーザー詳細を表示するための文字列を登録
        model.addAttribute("contents", "dev/vvsUpdate :: vvsUpdate_contents");

        // vvs情報を取得
        VvsBean vvs = vvsService.selectOne(vvsId);

        // vvsクラスをフォームクラスに変換
        form.setVvsId(vvs.getVvsId());
        form.setUsedAttribute(vvs.getUsedAttribute());
        form.setSortKey(vvs.getSortKey());
        form.setKey(vvs.getKey()); 
        form.setValue(vvs.getValue());

        // Modelに登録
        model.addAttribute("vvsForm", form);

        return "dev/homeLayout";
    }

    /**
     * vvs更新処理
     * @param form
     * @param model
     * @return
     */
    @PostMapping(value = "/vvsUpdate", params = "update")
    public String postDeviceUpdate(@ModelAttribute VvsForm form, Model model) {

        VvsBean vvsBean = new VvsBean();

        //フォームクラスをUserクラスに変換
        vvsBean.setVvsId(form.getVvsId());
        vvsBean.setSortKey(form.getSortKey());
        vvsBean.setKey(form.getKey());
        vvsBean.setValue(form.getValue());

        try {

            //更新実行
            boolean result = vvsService.updateOne(vvsBean);

            if (result == true) {
                model.addAttribute("result", "更新成功");
            } else {
                model.addAttribute("result", "更新失敗");
            }

        } catch(DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト!!)");

        }

        //デバイス一覧画面を表示
        return getVvsDetail(form, model, form.getVvsId());
    }

    /**
     * vvs登録画面のGETメソッド用処理.
     */
    @GetMapping("/insVvs")
    public String getSignUp(@ModelAttribute VvsForm form, Model model, @RequestParam("kbn") String usedAttibute) {

        // コンテンツ部分にデバイス一覧を表示するための文字列を登録
        model.addAttribute("contents", "dev/insVvs :: insVvs_contents");

        // IDの初期化
        initId = initVvsId();
        
        // 初期IDをModelに登録
        form.setVvsId(initId);
        form.setUsedAttribute(usedAttibute);
        model.addAttribute("vvsForm", form);

        return "dev/homeLayout";
    }

    /**
     * deviceIdの初期化メソッド.
     */
    private int initVvsId() {
    	return vvsService.getMaxNo();
    }
    
}
