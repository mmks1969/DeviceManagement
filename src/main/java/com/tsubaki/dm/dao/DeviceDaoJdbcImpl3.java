package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.DeviceBean;

@Repository("DeviceDaoJdbcImpl3")
public class DeviceDaoJdbcImpl3 extends DeviceDaoJdbcImpl {

    @Autowired
    private JdbcTemplate jdbc;

    //ユーザー１件取得
    @Override
    public DeviceBean selectOne(String deviceId) {

        //１件取得用SQL
        String sql = "SELECT * FROM m_user WHERE user_id = ?";

        //RowMapperの生成
        RowMapper<DeviceBean> rowMapper = new BeanPropertyRowMapper<DeviceBean>(DeviceBean.class);

        //SQL実行
        return jdbc.queryForObject(sql, rowMapper, deviceId);
    }

    //ユーザー全件取得
    @Override
    public List<DeviceBean> selectMany() {

        //M_USERテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM m_user";

        //RowMapperの生成
        RowMapper<DeviceBean> rowMapper = new BeanPropertyRowMapper<DeviceBean>(DeviceBean.class);

        //SQL実行
        return jdbc.query(sql, rowMapper);
    }
}
