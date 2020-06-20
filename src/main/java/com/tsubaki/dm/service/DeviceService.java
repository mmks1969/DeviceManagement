package com.tsubaki.dm.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsubaki.dm.model.DeviceBean;
import com.tsubaki.dm.dao.DeviceDao;

@Transactional
@Service
public class DeviceService {

    @Autowired
    @Qualifier("DeviceDaoJdbcImpl2")
    DeviceDao dao;

    /**
     * insert用メソッド.
     */
    public boolean insert(DeviceBean device) {

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
     * カウント用メソッド.
     */
    public int count() {
        return dao.count();
    }

    /**
     * 全件取得用メソッド.
     */
    public List<DeviceBean> selectMany() {
        // 全件取得
        return dao.selectMany();
    }

    /**
     * １件取得用メソッド.
     */
    public DeviceBean selectOne(String userId) {
        // selectOne実行
        return dao.selectOne(userId);
    }

    /**
     * １件更新用メソッド.
     */
    public boolean updateOne(DeviceBean device) {

        // 判定用変数
        boolean result = false;

        // １件更新
        int rowNumber = dao.updateOne(device);

        if (rowNumber > 0) {
            // update成功
            result = true;
        }

        return result;
    }

    /**
     * １件削除用メソッド.
     */
    public boolean deleteOne(String userId) {

        // １件削除
        int rowNumber = dao.deleteOne(userId);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // delete成功
            result = true;
        }
        return result;
    }

    // デバイス一覧をCSV出力する.
    /**
     * @throws DataAccessException
     */
    public void deviceCsvOut() throws DataAccessException {
        // CSV出力
        dao.deviceCsvOut();
    }

    /**
     * サーバーに保存されているファイルを取得して、byte配列に変換する.
     */
    public byte[] getFile(String fileName) throws IOException {

        // ファイルシステム（デフォルト）の取得
        FileSystem fs = FileSystems.getDefault();

        // ファイル取得
        Path p = fs.getPath(fileName);

        // ファイルをbyte配列に変換
        byte[] bytes = Files.readAllBytes(p);

        return bytes;
    }
    
    // デバイスIDの最大値を取得
    public String getMaxNo() {
    	return dao.selectMaxId();
    }
}
