package com.tsubaki.dm.model;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class UserSignupForm {
	private String userId; // ユーザーＩＤ
	private String password; // パスワード
	private String userName; // ユーザー名
	
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date birthday; // 誕生日
	
	private int age; // 年齢
	private boolean marriage; // 結婚ステータス
}
