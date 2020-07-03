package com.tsubaki.dm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.tsubaki.dm.model.DeviceBean;
import com.tsubaki.dm.model.User;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        // 戻り値用のデバイスインスタンスを生成
    	User user = new User();
    	
    	int mInt = rs.getInt("marriage");
    	boolean marriageFlg = false;
    	
    	if( mInt != 0) {
    		marriageFlg = true;
    	}
        
        // ResultSetの取得結果をUserインスタンスにセット
    	user.setUserId(rs.getString("user_id"));
    	user.setPassword(rs.getString("password"));
    	user.setUserName(rs.getString("user_name"));
    	user.setBirthday(rs.getDate("birthday"));
    	user.setAge(rs.getInt("age"));
    	user.setMarriage(marriageFlg);
    	user.setRole(rs.getString("role"));

        return user;
    }
}
