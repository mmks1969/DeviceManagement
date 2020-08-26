package com.tsubaki.dm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tsubaki.dm.model.Vvs;

public class VvsRowMapper implements RowMapper<Vvs> {

    @Override
    public Vvs mapRow(ResultSet rs, int rowNum) throws SQLException {

        // 結果返却用の変数
    	Vvs vvs = new Vvs();

            // Userインスタンスに取得したデータをセットする
    		vvs.setUsedAttribute(rs.getString("used_attribute"));
    		vvs.setSortKey(rs.getString("sort_key"));
            vvs.setKey(rs.getString("key"));
            vvs.setValue(rs.getString("value"));

        return vvs;
    }
}
