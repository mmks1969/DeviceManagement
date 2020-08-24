package com.tsubaki.dm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.FileBean;;

@Repository("FileDaoJdbcImpl2")
public class FileDaoJdbcImpl2 extends FileDaoJdbcImpl {

    @Autowired
    private JdbcTemplate jdbc;

    //ユーザー１件取得
    @Override
    public FileBean selectOne(String fileName) {

        //１件取得用SQL
        String sql = "SELECT * FROM m_file WHERE file_name = ?";

        //RowMapperの生成
        FileRowMapper rowMapper = new FileRowMapper();

        //SQL実行
        return jdbc.queryForObject(sql, rowMapper, fileName);
    }

    // デバイスに添付された全ファイルをファイルテーブル（m_file）から取得.
    @Override
    public List<FileBean> selectMany(String deviceId) {

        //M_USERテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM m_file WHERE device_id = ?";

        //RowMapperの生成
        RowMapper<FileBean> rowMapper = new FileRowMapper();

        //SQL実行
        return jdbc.query(sql, rowMapper, deviceId);
    }

    // デバイスに添付されたファイルの個数を取得する
    @Override
    public String countFile(String deviceId) {
    	// SQL
    	String sql = "SELECT count(file_no) as cnt FROM pcm.m_file where device_id = ?";
    	
    	List<Map<String, Object>> list =jdbc.queryForList(sql, deviceId); 
    	String cnt = list.get(0).get("cnt").toString();
    	
    	return cnt;
    	
    }

    // デバイスに添付されたファイルの最大FileNoを取得する
    public String selectFileNo(String deviceId) {
    	// SQL
    	String sql = "SELECT max(file_no) as maxno FROM pcm.m_file where device_id = ?";
    	
    	List<Map<String, Object>> list =jdbc.queryForList(sql, deviceId); 
    	String fileNo = list.get(0).get("maxno").toString();
    	
    	return fileNo;
    	
    }


}
