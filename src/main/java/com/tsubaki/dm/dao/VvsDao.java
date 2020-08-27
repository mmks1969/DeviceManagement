package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.tsubaki.dm.model.VvsBean;

public interface VvsDao {

	// テーブルの件数を取得.
    public int count() throws DataAccessException;

    // usedAttribute毎のテーブルの件数を取得.
    public int count(String usedAttribute) throws DataAccessException;
    
    // usedAttribute毎のm_vvsのレコードを取得.
    public List<VvsBean>  selectAttribute(String usedAttribute) throws DataAccessException;

    // レコードを1件取得.
    public VvsBean selectOne(int id) throws DataAccessException;
    
    // レコードを1件更新.
    public int updateOne(VvsBean vvsBean) throws DataAccessException;
    
}
