package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.DeviceBean;

@Repository("UserDaoJdbcImpl4")
public class DeviceDaoJdbcImpl4 extends DeviceDaoJdbcImpl {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<DeviceBean> selectMany() {

        //M_USERテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM m_device";

        //ResultSetExtractorの生成
        DeviceResultSetExtractor extractor = new DeviceResultSetExtractor();

        //SQL実行
        return jdbc.query(sql, extractor);
    }
}
