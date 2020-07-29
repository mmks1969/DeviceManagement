package com.tsubaki.dm.util;

import java.util.ResourceBundle;
import org.springframework.stereotype.Component;

/**
 * External.Propertiesファイルに関する共通処理
 *
 *
 */

@Component
public class ExternalPropertiesUtil {

	ResourceBundle extra_cons = null;
	
	public ExternalPropertiesUtil() {
        extra_cons = ResourceBundle.getBundle( "extra_cons" );
	}

	public String get(String s) {
		return extra_cons.getString(s);
	}
}