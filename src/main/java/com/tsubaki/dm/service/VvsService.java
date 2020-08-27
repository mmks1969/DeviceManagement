package com.tsubaki.dm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsubaki.dm.dao.VvsDao;
import com.tsubaki.dm.model.VvsBean;

@Transactional
@Service
public class VvsService {

    @Autowired
    VvsDao vvsDao;

    /**
     * m_vvsから使用する項目(usedAttribute)で選択肢を抽出する
     * @param usedAttribute
     * @return
     */
    public Map<String, String>  getMap(String usedAttribute) {
    	
    	// usedAttributeのMap取得
    	Map<String, String> vvsMap = new LinkedHashMap<>();
    	
    	// データの取得
    	List<VvsBean> vvsList = vvsDao.selectAttribute(usedAttribute);
    	
        // 取得したデータをMapに格納
        for (VvsBean list : vvsList) {
        	vvsMap.put(list.getKey(), list.getValue());
        }

        // 全件取得
        return vvsMap;
    }

    /**
     * usedAttribute毎のデータを取得する
     * @param usedAttribute
     * @return
     */
    public List<VvsBean> selectAttribute(String usedAttribute){
    	return vvsDao.selectAttribute(usedAttribute);
    }
    
    /**
     * カウント用メソッド（全件）
     * @return
     */
    public int count() {
    	return vvsDao.count();
    }

    /**
     * カウント用メソッド（usedAttribute毎）
     * @return
     */
    public int count(String usedAttibute) {
    	return vvsDao.count(usedAttibute);
    }
    
    /**
     * 1件取得用のメソッド
     * @param id
     * @return
     */
    public VvsBean selectOne(int id) {
    	return vvsDao.selectOne(id);
    }
    
    /**
     * １件更新用メソッド.
     */
    public boolean updateOne(VvsBean vvsBean) {

        // 判定用変数
        boolean result = false;

        // １件更新
        int rowNumber = vvsDao.updateOne(vvsBean);

        if (rowNumber > 0) {
            // update成功
            result = true;
        }

        return result;
    }

    /**
     * １件登録用メソッド.
     */
    public boolean insertOne(VvsBean vvsBean) {
    	
    	// 判定用変数
    	boolean result = false;
    	
    	// １件更新
    	int rowNumber = vvsDao.insertOne(vvsBean);
    	
    	if (rowNumber > 0) {
    		// update成功
    		result = true;
    	}
    	
    	return result;
    }
    
    // デバイスIDの最大値を取得
    public int getMaxNo() {
    	return vvsDao.getMaxNo();
    }
}
