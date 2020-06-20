package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.tsubaki.dm.model.Vvs;

public interface VvsDao {

    // デバイステーブル（m_device）の全データを取得.
    public List<Vvs>  selectAttribute(String usedAttribute) throws DataAccessException;

}
