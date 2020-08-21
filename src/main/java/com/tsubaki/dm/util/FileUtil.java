package com.tsubaki.dm.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {

    @Autowired
	ExternalPropertiesUtil externalProp;
    @Autowired
    MessageSource messagesource;
	
    /**
     * ファイルの移動
     * path1に存在しているfileNameをpath2に移動させる
     * @param path1
     * @param path2
     * @param fileName
     */
	public void moveFile(String path1, String path2, String fileName) {
		File orgFile = new File(path1 + fileName);
		File destFile = new File(path2 + fileName);
		
		orgFile.renameTo(destFile);

	}

}