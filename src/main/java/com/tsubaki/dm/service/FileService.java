package com.tsubaki.dm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsubaki.dm.dao.FileDao;
import com.tsubaki.dm.model.FileBean;

@Transactional
@Service
public class FileService {

    @Autowired
    @Qualifier("FileDaoJdbcImpl2")
    FileDao dao;

    /**
     * insert用メソッド.
     */
    public boolean insert(FileBean device) {

        // insert実行
        int rowNumber = dao.insertOne(device);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // insert成功
            result = true;
        }

        return result;
    }

    /**
     * １件取得用メソッド.
     */
    public FileBean selectOne(String fileName) {
        // selectOne実行
        return dao.selectOne(fileName);
    }

    /**
     * デバイスに添付されている全ファイルデータを取得するメソッド.
     */
    public List<FileBean> selectMany(String deviceId) {
    	// selectOne実行
    	return dao.selectMany(deviceId);
    }
    
    /**
     * １件削除用メソッド.
     */
    public boolean deleteOne(String fileName) {

        // １件削除
        int rowNumber = dao.deleteOne(fileName);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // delete成功
            result = true;
        }
        return result;
    }

}
