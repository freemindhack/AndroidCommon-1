/** Copyright © 2015-2020 100msh.com All Rights Reserved */
package com.bmsh.common.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * SD卡相关的辅助类
 * 
 * @author Frank
 * @date 2015年8月7日上午10:34:43
 */

public class SdcardUtils {

	private SdcardUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	
	/**
	 * 获取系统存储路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * 获取外部缓存目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getExternalCacheDir(Context context) {
		String path = context.getExternalCacheDir().getAbsolutePath();
		return path;
	}

}
