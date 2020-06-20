package com.tsubaki.dm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.tsubaki.dm.model.DeviceBean;

public class DeviceRowMapper implements RowMapper<DeviceBean> {

    @Override
    public DeviceBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        // 戻り値用のデバイスインスタンスを生成
    	DeviceBean deviceBean = new DeviceBean();
        
        // ResultSetの取得結果をデバイスインスタンスにセット
        deviceBean.setDeviceId(rs.getString("device_id"));
        deviceBean.setDeviceKbn(rs.getString("device_kbn"));
        deviceBean.setKataban(rs.getString("kataban"));
        deviceBean.setOwner(rs.getString("owner"));
        deviceBean.setMaker(rs.getString("maker"));
        deviceBean.setPurchaseDate(rs.getString("purchase_date"));
        deviceBean.setCreationdate(rs.getString("creationdate"));
        deviceBean.setLastupdate(rs.getString("lastupdate"));

        return deviceBean;
    }
}
