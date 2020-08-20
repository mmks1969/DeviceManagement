package com.tsubaki.dm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("SuccessHandler")
	private AuthenticationSuccessHandler successHandler;
	
	@Autowired
	@Qualifier("UserDetailsServiceImpl")
	private UserDetailsService userDetailsService;
	
	// パスワードエンコーダーのBean定義
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// データソース
	@Override
	public void configure(WebSecurity web) throws Exception{
		// 静的リソースへのアクセスには、セキュリティーを適用しない
		web.ignoring().antMatchers("/webjars/**","/css/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 直リンクの禁止＆ﾛｸﾞｲﾝ不要ページの設定
		http
			.authorizeRequests()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/login").permitAll()  // アクセス許可
				.antMatchers("/userSignup").permitAll()
				.antMatchers("/rest/**").permitAll()
				.antMatchers("/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated();  // それ以外は直リンク禁止
		
		// ログイン処理
		http
			.formLogin()
				.loginProcessingUrl("/login")	// ログイン処理のパス
				.loginPage("/login")	// ログインページの指定
				.failureUrl("/login?error")	// ログイン失敗時の遷移先
				.usernameParameter("userId")	// ログインページのユーザーID
				.passwordParameter("password")	// ログインページのパスワード
				.defaultSuccessUrl("/home", true)	// ログイン成功時の遷移先
				.successHandler(successHandler); // SuccessHandlerの設定
		
		// ログアウト処理
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login");
				
		// CSRFを無効にするURLを設定
		RequestMatcher csrfMatcher = new RestMatcher("/rest/**");
		
		// RESTのみCSRF対策を無効に設定
		http.csrf().requireCsrfProtectionMatcher(csrfMatcher);
		
		http
			.headers()
			.frameOptions()
			.sameOrigin();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{

		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}

}
