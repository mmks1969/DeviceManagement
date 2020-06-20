package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.tsubaki.dm.model.FileBean;

public interface FileDao {

    // ファイルテーブルにデータを1件insert.
    public int insertOne(FileBean fileBean) throws DataAccessException;

    // ファイルテーブルのデータを１件取得
    public FileBean selectOne(String fileName) throws DataAccessException;

    // デバイスに添付されている全ファイルデータを取得.
    public List<FileBean> selectMany(String deviceId) throws DataAccessException;

    // ファイルテーブルを１件削除.
    public int deleteOne(String fileName) throws DataAccessException;

}
