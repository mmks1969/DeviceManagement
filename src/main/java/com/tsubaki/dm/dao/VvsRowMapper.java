package com.tsubaki.dm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tsubaki.dm.model.VvsBean;

public class VvsRowMapper implements RowMapper<VvsBean> {

    @Override
    public VvsBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        // 結果返却用の変数
    	VvsBean vvs = new VvsBean();

            // Userインスタンスに取得したデータをセットする
    		vvs.setVvsId(rs.getInt("vvs_id"));
    		vvs.setUsedAttribute(rs.getString("used_attribute"));
    		vvs.setSortKey(rs.getString("sort_key"));
            vvs.setKey(rs.getString("key_str"));
            vvs.setValue(rs.getString("value_str"));

        return vvs;
    }
}
