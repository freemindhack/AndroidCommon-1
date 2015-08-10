/** Copyright © 2015-2020 100msh.com All Rights Reserved */
package com.bmsh.common.util;

import android.util.Log;

/**
 * Log管理类
 * 
 * @author Frank
 * @date 2015年8月7日上午10:15:35
 */

public class LogUtils {
	
	/**
	 * 是否需要打印bug，可以在application的onCreate函数里面初始化
	 */
	public static boolean isDebug = true;

	/**
	 * 日志TAG
	 */
	private static final String TAG = "Bmsh";

	private LogUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	/**
	 * instantiated
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}
}