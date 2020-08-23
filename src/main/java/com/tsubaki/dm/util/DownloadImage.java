package com.tsubaki.dm.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
//@Transactional
public class DownloadImage {

    @Autowired
	ExternalPropertiesUtil externalProp;
    @Autowired
    MessageSource messagesource;
	
	/**
	 * ドキュメント（フレーム内への）表示
	 * @param httpSession
	 * @param request
	 * @param response
	 * @param file_table
	 * @param p_key
	 * @throws Exception
	 */
	public void downloadInline(HttpSession httpSession
							,HttpServletRequest request
							,HttpServletResponse response
							,String p_key) throws Exception {
    	response.reset();
   		response.setContentType("application/pdf");
    	response.setHeader("Pragma", ""); 
    	response.setHeader("Cache-Control", ""); 
    	response.setHeader("Content-Disposition", "inline; filename=10827.pdf");

    	String filepath = externalProp.get("FILE_PATH");

		BufferedInputStream in = null;
		byte[] buf;
		in = new BufferedInputStream(new FileInputStream(filepath + p_key));
		buf = new byte[in.available()];
		in.read(buf);
		OutputStream out = response.getOutputStream();
		out.write(buf);
		out.flush();					

		in.close();
		out.close();

	}

}