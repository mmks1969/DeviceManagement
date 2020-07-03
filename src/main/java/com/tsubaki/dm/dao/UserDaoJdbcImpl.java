package com.tsubaki.dm.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.User;

@Repository
public class UserDaoJdbcImpl implements UserDao {
	
	@Autowired
	JdbcTemplate jdbc;
	
	// Userテーブルの件数を取得
	@Override
	public int count() throws DataAccessException{
		// 全件取得してカウント
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_user", Integer.class); 
		return count;
	}

	// Userテーブルにデータを1件insert
	public int insertOne(User user) throws DataAccessException{
		int rowNumber = jdbc.update("INSERT INTO m_user(user_id,password,user_name,birthday,age,marriage,role)"
				+ "VALUES(?,?,?,?,?,?,?)"
				, user.getUserId()
				, user.getPassword()
				, user.getUserName()
				, user.getBirthday()
				, user.getAge()
				, user.isMarriage()
				, user.getRole());
		return rowNumber;
	}
	
	// Userテーブルのデータを1件取得
	public User selectOne(String userId) throws DataAccessException{
		// 一見取得
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user WHERE user_id = ?", userId);
		
		// 結果返却用の変数
		User user = new User();

		// MySQLのBooleanはTinyintなので、ここでBooleanに変換しておく
		int tesInt =(Integer)map.get("marriage");
		Boolean marriageBoolean = true;
		if (tesInt == 0) {
			marriageBoolean = false; 
		}

		// 取得したデータを結果返却用の変数にセットしていく
		user.setUserId((String)map.get("user_id"));
		user.setPassword((String)map.get("password"));
		user.setUserName((String)map.get("user_name"));
		user.setBirthday((Date)map.get("birthday"));
		user.setAge((Integer)map.get("age"));
		user.setMarriage(marriageBoolean);
		user.setRole((String)map.get("role"));
		
		return user;
	}
	
	// Userテーブルの全データを取得
	public List<User> selectMany() throws DataAccessException{
		
		// m_userマスタのデータを全件取得
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM m_user");
		
		// 結果返却用の変数
		List<User> userList = new ArrayList<>();
		
		// 取得したデータを結果返却用のListに格納していく
		for(Map<String, Object> map:getList) {
			// Userインスタンスの生成
			User user = new User();
			
			// MySQLのBooleanはTinyintなので、ここでBooleanに変換しておく
			int tesInt =(Integer)map.get("marriage");
			Boolean marriageBoolean = true;
			if (tesInt == 0) {
				marriageBoolean = false; 
			}
			
			// Userインスタンスに取得したデータをセットする
			user.setUserId((String)map.get("user_id"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday((Date)map.get("birthday"));
			user.setAge((Integer)map.get("age"));
			user.setMarriage(marriageBoolean);
			user.setRole((String)map.get("role"));
			
			// 結果返却用のListに追加
			userList.add(user);
		}
		
		return userList;
	}
	
	// Userテーブルを1件更新
	public int updateOne(User user) throws DataAccessException{
		// 一件更新
		int rowNumber = jdbc.update("UPDATE M_USER SET password = ?,"
				+ " user_name = ?,"
				+ " birthday = ?,"
				+ " age = ?,"
				+ " marriage = ?"
				+ " WHERE user_id = ?"
				, user.getPassword()
				, user.getUserName()
				, user.getBirthday()
				, user.getAge()
				, user.isMarriage()
				, user.getUserId());
		
		return rowNumber;
	}
	
	// Userテーブルを1件削除
	public int deleteOne(String userId) throws DataAccessException{
		// 1件削除
		int rowNumber = jdbc.update("DELETE FROM m_user WHERE user_id = ?", userId);
		
		return rowNumber;
	}
	
	// SQL取得結果をサーバーにCSVで保存する
	public void userCsvOut() throws DataAccessException{
		
	}

}

































