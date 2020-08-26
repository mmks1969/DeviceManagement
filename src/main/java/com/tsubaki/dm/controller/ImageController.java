package com.tsubaki.dm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tsubaki.dm.util.DownloadImage;

@Secured("IS_AUTHENTICATED_FULLY")
@Controller
public class ImageController {
	
	@Autowired
	DownloadImage downloadImage;

    @ResponseBody
	@RequestMapping(value = "/getImage")
    /**
     * ＰＤＦファイル（フレーム内）表示
     * @param file_table
     * @param p_key
     * @param httpSession
     * @param request
     * @param response
     */
    @Secured("IS_AUTHENTICATED_FULLY")
	public void getImage(@RequestParam("p_key") String p_key
						,HttpSession httpSession
						,HttpServletRequest request
						,HttpServletResponse response) {
		
    	try {
    		downloadImage.downloadInline(httpSession, request, response, p_key);
    	} catch (Exception e) {
		}
    	return;
    }
	
}
