package com.tsubaki.dm.model;

import java.util.Collection;
import java.util.Date;

import org.aspectj.weaver.loadtime.Agent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDetails implements UserDetails{
	// springで必要なフィールド
	private String userId; // ユーザーID
	private String password; // パスワード
	private Date passUpdateDate; // パスワード更新日付
	private int loginMissTimes; // ログイン失敗回数
	private boolean unlock; // ロック状態フラグ
	private boolean enabled; // 有効・向こうフラグ
	private Date userDueDate; // ユーザー有効期限
	// 権限のCollection
	private Collection<? extends GrantedAuthority> authority;
	
	// 独自フィールド
	private String tenantId; // テナントID
	private String appUserName; // ユーザー名
	private String mailAddress; // メールアドレス
	private Date birthday; // 誕生日
	private int age; // 年齢
	private boolean marriage; // 既婚
	private String role; // ロール
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return authority;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.userId;
	}
	
	/**
	 * アカウントの有効期限チェック
	 * true：有効　　false：無効
	 */
	@Override
	public boolean isAccountNonExpired() {
		// ユーザー有効期限のチェック
		if(this.userDueDate.after(new Date())) {
			return true;
		} else {
			// 現在日付よりも前なら無効
			return false;
		}
	}
	
	/**
	 * アカウントのロックチェック
	 * true：有効　　false：無効
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.unlock;
	}
	
	/**
	 * パスワードの有効期限チェック
	 */
	@Override
	public boolean isCredentialsNonExpired() {
//		// パスワードの有効期限のチェック
//		if(this.passUpdateDate.after(new Date())) {
//			// 現在日付よりも後なら有効
//			return true;
//		} else {
//			// 現在日付よりも前なら無効
//			return false;
//		}
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
