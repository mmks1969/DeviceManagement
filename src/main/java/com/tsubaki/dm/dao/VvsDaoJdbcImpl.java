package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.Vvs;

@Repository("VvsDaoJdbcImpl")
public class VvsDaoJdbcImpl implements VvsDao {

    @Autowired
    JdbcTemplate jdbc;

    // デバイステーブル（m_device）の全データを取得.
    @Override
//    public List<Map<String, Object>> selectAttribute(String usedAttribute) throws DataAccessException {
    public List<Vvs> selectAttribute(String usedAttribute) {

        //RowMapperの生成
        RowMapper<Vvs> rowMapper = new VvsRowMapper();

        // m_vvsから指定された属性のデータを全件取得
        return jdbc.query("SELECT * FROM m_vvs where used_attribute= ? ORDER BY sort_key",rowMapper,usedAttribute);

    }

}
