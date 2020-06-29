package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Jdbc;
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
		return 0;
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
		return null;
	}
	
	// Userテーブルの全データを取得
	public List<User> selectMany() throws DataAccessException{
		return null;
	}
	
	// Userテーブルを1件更新
	public int updateOne(User user) throws DataAccessException{
		return 0;
	}
	
	// Userテーブルを1件削除
	public int deleteOne(String userId) throws DataAccessException{
		return 0;
	}
	
	// SQL取得結果をサーバーにCSVで保存する
	public void userCsvOut() throws DataAccessException{
		
	}

}
