package com.tsubaki.dm.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tsubaki.dm.dao.LoginUserRepository;

import lombok.extern.slf4j.Slf4j;

@Component("UserDetailsServiceImpl")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private LoginUserRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	// ログイン失敗の上限回数
	private static final int LOGIN_MISS_LIMIT = 3;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		
		// リポジトリー（DAO)からUserDetailsを取得
		UserDetails user = repository.selectOne(username);
		
		return user;
	}
	
	/**
	 * パスワードを更新する
	 * パスワード更新日を3ヶ月後に設定する
	 * @param userId
	 * @param password
	 * @throws ParseException
	 */
	public void updatePasswordDate(String userId, String password) throws ParseException {
		
		// パスワード暗号化
		String encryptPass = encoder.encode(password);
		
		// パスワード更新日付（3カ月先）の作成
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 3);
		date = calendar.getTime();
		
		// リポジトリーからパスワードを更新
		repository.updatePassword(userId, encryptPass, date);
	}
	
	/**
	 * 失敗回数と有効・無効フラグを更新する
	 * パスワードを間違えた場合に失敗回数を一つ増やし、ログイン失敗回数の上限を超えたときに
	 * アカウントをロックする
	 * @param userId
	 * @param loginMissTime
	 */
	public void updateUnlock(String userId, int loginMissTime) {
		
		// 有効・無効フラグ（有効）
		boolean unlock = true;
		
		if(loginMissTime >= LOGIN_MISS_LIMIT) {
			unlock = false;
			log.info(userId + "をロックします");
		}
		
		// リポジトリ―～パスワードを変更
		repository.updateUnlock(userId, loginMissTime, unlock);
	}
	
	/**
	 * ログイン失敗回数を0に更新する
	 * ログインに成功した時にログイン失敗回数を0にリセットする
	 * @param userId
	 */
	public void updateLoginMissTimes(String userId) {
		
		// ログイン失敗回数を0に戻す
		repository.updateLoginMissTimes(userId);
	}

}
