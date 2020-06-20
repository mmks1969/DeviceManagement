package com.tsubaki.dm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsubaki.dm.dao.VvsDao;
import com.tsubaki.dm.model.Vvs;

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
    	List<Vvs> vvsList = vvsDao.selectAttribute(usedAttribute);
    	
        // 取得したデータをMapに格納
        for (Vvs list : vvsList) {
        	vvsMap.put(list.getKey(), list.getValue());
        }

        // 全件取得
        return vvsMap;
    }

}
