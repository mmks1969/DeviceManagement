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

import com.tsubaki.dm.model.DeviceBean;

@Repository("DeviceDaoJdbcImpl")
public class DeviceDaoJdbcImpl implements DeviceDao {

    @Autowired
    JdbcTemplate jdbc;

    // Userテーブルの件数を取得.
    @Override
    public int count() throws DataAccessException {

        // 全件取得してカウント
        int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_device", Integer.class);

        return count;
    }

    // Userテーブルにデータを1件insert.
    @Override
    public int insertOne(DeviceBean device) throws DataAccessException {
    	// 現在時刻を取得
    	Calendar calendar = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss");
    	System.out.println(sdf.format(calendar.getTime()));

        // １件登録
        int rowNumber = jdbc.update("INSERT INTO m_device(device_id,"
                + " device_kbn,"
                + " kataban,"
                + " owner,"
                + " maker,"
                + " purchase_date,"
                + " creationdate,"
                + " lastupdate)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                device.getDeviceId(),
                device.getDeviceKbn(),
                device.getKataban(),
                device.getOwner(),
                device.getMaker(),
                device.getPurchaseDate(),
                sdf.format(calendar.getTime()),
                sdf.format(calendar.getTime()));

        return rowNumber;
    }

    // Userテーブルのデータを１件取得
    @Override
    public DeviceBean selectOne(String udeviceId) throws DataAccessException {

        // １件取得
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_device"
                + " WHERE device_id = ?", udeviceId);

        // 結果返却用の変数
        DeviceBean device = new DeviceBean();

        // 取得したデータを結果返却用の変数にセットしていく
        device.setDeviceId((String) map.get("device_id")); // デバイスID
        device.setKataban((String) map.get("kataban")); // 形番
        device.setOwner((String) map.get("owner")); // 所有者
        device.setMaker((String) map.get("maker")); // メーカー
        device.setCreationdate((String) map.get("creationdate")); // 登録日
        device.setLastupdate((String) map.get("lastupdate")); // 更新日

        return device;
    }

    // デバイステーブル（m_device）の全データを取得.
    @Override
    public List<DeviceBean> selectMany() throws DataAccessException {

        // m_deviceテーブルのデータを全件取得
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_device");

        // 結果返却用の変数
        List<DeviceBean> deviceList = new ArrayList<>();

        // 取得したデータを結果返却用のListに格納していく
        for (Map<String, Object> map : getList) {

            //Userインスタンスの生成
        	DeviceBean device = new DeviceBean();

            // Userインスタンスに取得したデータをセットする
            device.setDeviceId((String) map.get("device_id")); //ユーザーID
            device.setKataban((String) map.get("kataban")); //パスワード
            device.setOwner((String) map.get("owner")); //ユーザー名
            device.setMaker((String) map.get("maker")); //誕生日
            device.setCreationdate((String) map.get("creationdate")); //年齢
            device.setLastupdate((String) map.get("lastupdate")); //年齢

            //結果返却用のListに追加
            deviceList.add(device);
        }

        return deviceList;
    }

    // m_deviceテーブルを１件更新.
    @Override
    public int updateOne(DeviceBean device) throws DataAccessException {

        //１件更新
        int rowNumber = jdbc.update("UPDATE m_device"
                + " SET"
                + " device_kbn = ?,"
                + " kataban = ?,"
                + " owner = ?,"
                + " maker = ?,"
                + " purchase_date = ?,"
                + " lastupdate = ?"
                + " WHERE device_id = ?",
                device.getDeviceKbn(),
                device.getKataban(),
                device.getOwner(),
                device.getMaker(),
                device.getPurchaseDate(),
                device.getLastupdate(),
                device.getDeviceId());

        //トランザクション確認のため、わざと例外をthrowする
//                if (rowNumber > 0) {
//                    throw new DataAccessException("トランザクションテスト") {
//                    };
//                }

        return rowNumber;
    }

    // Userテーブルを１件削除.
    @Override
    public int deleteOne(String deviceId) throws DataAccessException {

        //１件削除
        int rowNumber = jdbc.update("DELETE FROM m_device WHERE device_id = ?", deviceId);

        return rowNumber;
    }

    //SQL取得結果をサーバーにCSVで保存する
    @Override
    public void deviceCsvOut() throws DataAccessException {

        // M_USERテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM m_device";

        // ResultSetExtractorの生成
        DeviceRowCallbackHandler handler = new DeviceRowCallbackHandler();

        //SQL実行＆CSV出力
        jdbc.query(sql, handler);
    }
    
    // デバイスIDの最大値を取得
    @Override
    public String selectMaxId() {
    	int nextId = 0;
    	String deviceIdString = jdbc.queryForObject("SELECT MAX(device_id) FROM m_device", String.class);
    	
    	if (deviceIdString == null) {
			nextId = 0;
		} else {
	    	nextId = Integer.parseInt(deviceIdString) + 1;
		}
    	return String.format("%04d", nextId);
    }

}
