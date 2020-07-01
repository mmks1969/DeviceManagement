package com.tsubaki.dm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsubaki.dm.dao.UserDao;
import com.tsubaki.dm.model.User;

@Service
public class UserService {
	
	@Autowired
	UserDao dao;
	
	// insert用メソッド
	public boolean insert(User user) {
		// insert実行
		int rowNumber = dao.insertOne(user);
		
		// 判定用変数
		boolean result = false;
		
		if(rowNumber > 0) {
			// insert成功
			result = true;
		}
		
		return result;
	}
	
	// カウント用メソッド
	public int count() {
		return dao.count();
	}
	
	// 全件取得用メソッド
	public List<User> selectMany() {
		// 全件取得
		return dao.selectMany();
	}

	// 1件取得用メソッド
	public User selectOne(String userId){
		return dao.selectOne(userId);
	}
}
