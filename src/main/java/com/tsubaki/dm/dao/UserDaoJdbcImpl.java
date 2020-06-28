package com.tsubaki.dm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tsubaki.dm.model.User;

@Repository
public class UserDaoJdbcImpl implements UserDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	// Userテーブルの件数を取得
	@Override
	public int count() throws DataAccessException{
		return 0;
	}

	// Userテーブルにデータを1件insert
	public int insertOne(User user) throws DataAccessException{
		return 0;
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
