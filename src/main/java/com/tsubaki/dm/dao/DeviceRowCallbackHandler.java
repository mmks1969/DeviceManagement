package com.tsubaki.dm.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

public class DeviceRowCallbackHandler implements RowCallbackHandler {

    @Override
    public void processRow(ResultSet rs) throws SQLException {

        try {

            //ファイル書き込みの準備
            File file = new File("DeviceList.csv");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            //取得件数分loop
            do {

                //ResultSetから値を取得してStringにセット
                String str = rs.getString("device_id") + ","
                        + rs.getString("device_kbn") + ","
                        + rs.getString("kataban") + ","
                        + rs.getString("owner") + ","
                        + rs.getString("maker") + ","
                        + rs.getString("purchase_date") + ","
                        + rs.getString("creationdate") + ","
                        + rs.getString("lastupdate");

                //ファイルに書き込み＆改行
                bw.write(str);
                bw.newLine();

            } while(rs.next());

            //強制的に書き込み＆ファイルクローズ
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }
}
