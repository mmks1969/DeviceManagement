package com.tsubaki.dm.model;

import java.util.Date;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class UserSignupForm {
	
	// 必須入力、メールアドレス形式
	@NotBlank
	@Email
	private String userId; // ユーザーＩＤ
	
	@NotBlank
	@Length(min = 4,max = 100)
	private String password; // パスワード
	
	// 必須入力
	@NotBlank
	private String userName; // ユーザー名
	
	// 必須入力
	@NotNull
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date birthday; // 誕生日
	
	// 値が20から100まで
	@Min(20)
	@Max(100)
	private int age; // 年齢
	
	@AssertFalse
	private boolean marriage; // 結婚ステータス
}
