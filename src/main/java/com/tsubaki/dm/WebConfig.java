package com.tsubaki.dm;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class WebConfig {
	
	@Bean
	public MessageSource messageSource() {
		
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		
		// メッセージのプロパティファイル名（デフォルト）を指示
		bean.setBasename("classpath:messages");
		
		// メッセージプロパティの文字コード指定
		bean.setDefaultEncoding("UTF-8");
		
		return bean;
	}
	
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		
		return localValidatorFactoryBean;
	}
}
