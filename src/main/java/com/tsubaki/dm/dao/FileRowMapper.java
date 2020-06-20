package com.tsubaki.dm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tsubaki.dm.model.FileBean;

public class FileRowMapper implements RowMapper<FileBean> {

    @Override
    public FileBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        //戻り値用のUserインスタンスを生成
    	FileBean fileBean = new FileBean();
        
        //ResultSetの取得結果をUserインスタンスにセット
        fileBean.setDeviceId(rs.getString("device_id"));
        fileBean.setFileName(rs.getString("file_name"));
        fileBean.setOriginalFileName(rs.getString("original_file_name"));
        fileBean.setCreationdate(rs.getString("creationdate"));

        return fileBean;
    }
}
