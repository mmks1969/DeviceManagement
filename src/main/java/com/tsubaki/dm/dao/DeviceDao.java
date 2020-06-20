package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.tsubaki.dm.model.DeviceBean;

public interface DeviceDao {

    // Userテーブルの件数を取得.
    public int count() throws DataAccessException;

    // Userテーブルにデータを1件insert.
    public int insertOne(DeviceBean user) throws DataAccessException;

    // Userテーブルのデータを１件取得
    public DeviceBean selectOne(String userId) throws DataAccessException;

    // Userテーブルの全データを取得.
    public List<DeviceBean> selectMany() throws DataAccessException;

    // Userテーブルを１件更新.
    public int updateOne(DeviceBean user) throws DataAccessException;

    // Userテーブルを１件削除.
    public int deleteOne(String userId) throws DataAccessException;

    //SQL取得結果をサーバーにCSVで保存する
    public void deviceCsvOut() throws DataAccessException;
    
    //デバイスIDの最大値を取得
    public String selectMaxId() throws DataAccessException;
}
