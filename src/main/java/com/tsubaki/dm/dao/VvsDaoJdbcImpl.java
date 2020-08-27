package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.DeviceBean;
import com.tsubaki.dm.model.VvsBean;

@Repository("VvsDaoJdbcImpl")
public class VvsDaoJdbcImpl implements VvsDao {

    @Autowired
    JdbcTemplate jdbc;

    // m_vvsのusedAttribute毎のデータを取得.
    @Override
    public List<VvsBean> selectAttribute(String usedAttribute) {

        //RowMapperの生成
        RowMapper<VvsBean> rowMapper = new VvsRowMapper();

        // m_vvsから指定された属性のデータを全件取得
        return jdbc.query("SELECT * FROM m_vvs where used_attribute= ? ORDER BY sort_key",rowMapper,usedAttribute);

    }

    /**
     * vvsマスタの件数を取得する
     */
    @Override
    public int count() throws DataAccessException {

        // 全件取得してカウント
        int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_vvs", Integer.class);

        return count;
    }

    /**
     * usedAttribute毎のｖｖｓマスタの件数を取得する
     */
    @Override
    public int count(String usedAttribute) throws DataAccessException {
    	
    	// 全件取得してカウント
    	int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_vvs where used_attribute = ?", Integer.class, usedAttribute);
    	
    	return count;
    }
    
    @Override
    public VvsBean selectOne(int id) throws DataAccessException {
    	
    	// 1件取得用SQL
    	String sql = "SELECT * FROM m_vvs where vvs_id = ?";
    	
    	// RowMapperの生成
    	VvsRowMapper vvsRowMapper = new VvsRowMapper();
    	
    	// 全件取得してカウント
    	return jdbc.queryForObject(sql, vvsRowMapper, id);
    	
    }
    
    // m_deviceテーブルを１件更新.
    @Override
    public int updateOne(VvsBean vvsVean) throws DataAccessException {

    	// 更新用SQL
    	String sql ="UPDATE m_vvs SET sort_key = ?, key_str = ?, value_str = ? where vvs_id = ?";
        //１件更新
        int rowNumber = jdbc.update(sql,vvsVean.getSortKey(), vvsVean.getKey(), vvsVean.getValue(), vvsVean.getVvsId());

        return rowNumber;
    }

}
