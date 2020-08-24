package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.tsubaki.dm.model.FileBean;

public interface FileDao {

    /**
     * ファイルテーブルにデータを1件insert.
     * @param fileBean
     * @return
     * @throws DataAccessException
     */
    public int insertOne(FileBean fileBean) throws DataAccessException;

    /**
     * ファイルテーブルのデータを１件取得
     * @param fileName
     * @return
     * @throws DataAccessException
     */
    public FileBean selectOne(String fileName) throws DataAccessException;

    /**
     * デバイスに添付されている全ファイルデータを取得.
     * @param deviceId
     * @return
     * @throws DataAccessException
     */
    public List<FileBean> selectMany(String deviceId) throws DataAccessException;

    /**
     * ファイルテーブルを１件削除.
     * @param fileName
     * @return
     * @throws DataAccessException
     */
    public int deleteOne(String fileName) throws DataAccessException;
    
    public String countFile(String deviceId) throws DataAccessException;

    public String selectFileNo(String deviceId) throws DataAccessException;
    
    

}
