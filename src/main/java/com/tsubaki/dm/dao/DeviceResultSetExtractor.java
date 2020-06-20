package com.tsubaki.dm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import com.tsubaki.dm.model.DeviceBean;

public class DeviceResultSetExtractor implements ResultSetExtractor<List<DeviceBean>> {

    @Override
    public List<DeviceBean> extractData(ResultSet rs) throws SQLException, DataAccessException {

        //User格納用List
        List<DeviceBean> devicesList = new ArrayList<>();

        //取得件数分のloop
        while(rs.next()) {

            //Listに追加するインスタンスを生成する
            DeviceBean device = new DeviceBean();

            //取得したレコードをUserインスタンスにセット
            device.setDeviceId(rs.getString("device_id"));
            device.setKataban(rs.getString("kataban"));
            device.setOwner(rs.getString("owner"));
            device.setMaker(rs.getString("maker"));
            device.setCreationdate(rs.getString("creationdate"));
            device.setLastupdate(rs.getString("lastupdate"));

            //ListにUserを追加
            devicesList.add(device);
        }

        //１件も無かった場合は例外を投げる
        if(devicesList.size() == 0) {
            throw new EmptyResultDataAccessException(1);
        }

        return devicesList;
    }
}
