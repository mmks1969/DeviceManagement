package com.tsubaki.dm.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.FileBean;

@Repository("FileDaoJdbcImpl")
public class FileDaoJdbcImpl implements FileDao {

    @Autowired
    JdbcTemplate jdbc;

    // ファイルテーブルにデータを1件insert.
    @Override
    public int insertOne(FileBean fileBean) throws DataAccessException {
    	// 現在時刻を取得
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss");
    	System.out.println(sdf.format(calendar.getTime()));

        // １件登録
        int rowNumber = jdbc.update("INSERT INTO m_file(file_name,"
                + " device_id,"
                + " original_file_name,"
                + " path1,"
                + " path2,"
                + " creationdate)"
                + " VALUES(?, ?, ?, ?, ?, ?)",
                fileBean.getFileName(),
                fileBean.getDeviceId(),
                fileBean.getOriginalFileName(),
                "VaultLoc",
                "torisetsu",
                sdf.format(calendar.getTime()));

        return rowNumber;
    }

    // ファイルテーブルのデータを１件取得
    @Override
    public FileBean selectOne(String fileName) throws DataAccessException {

        // １件取得
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_file"
                + " WHERE file_name = ?", fileName);

        // 結果返却用の変数
        FileBean fileBean = new FileBean();

        // 取得したデータを結果返却用の変数にセットしていく
        fileBean.setDeviceId((String) map.get("device_id")); // デバイスID
        fileBean.setFileName((String) map.get("file_name")); // 形番
        fileBean.setOriginalFileName((String) map.get("original_file_name")); // 所有者
        fileBean.setCreationdate((String) map.get("creationdate")); // 登録日

        return fileBean;
    }

    // デバイスに添付された全ファイルをファイルテーブル（m_file）から取得.
    @Override
    public List<FileBean> selectMany(String deviceId) throws DataAccessException {

        // m_deviceテーブルのデータを全件取得
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_file WHERE device_id = ?");

        // 結果返却用の変数
        List<FileBean> fileList = new ArrayList<>();

        // 取得したデータを結果返却用のListに格納していく
        for (Map<String, Object> map : getList) {

            //Userインスタンスの生成
        	FileBean fileBean = new FileBean();

            // Userインスタンスに取得したデータをセットする
            fileBean.setDeviceId((String) map.get("device_id")); //ユーザーID
            fileBean.setFileName((String) map.get("file_name")); //パスワード
            fileBean.setOriginalFileName((String) map.get("original_file_name")); //ユーザー名
            fileBean.setCreationdate((String) map.get("creationdate")); //年齢

            //結果返却用のListに追加
            fileList.add(fileBean);
        }

        return fileList;
    }

    // Userテーブルを１件削除.
    @Override
    public int deleteOne(String fileName) throws DataAccessException {

        //１件削除
        int rowNumber = jdbc.update("DELETE FROM m_device WHERE device_id = ?", fileName);

        return rowNumber;
    }

}
