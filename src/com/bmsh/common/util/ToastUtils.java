/** Copyright © 2015-2020 100msh.com All Rights Reserved */
package com.bmsh.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 * 
 * @author Frank
 * @date 2015年8月7日上午10:41:00
 */

public class ToastUtils {

	/**
	 * 是否显示Toast
	 */
	public static boolean isShow = true;

	private ToastUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void show(Context context, CharSequence message) {
		if (isShow)
			show(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void show(Context context, int message) {
		if (isShow)
			show(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow)
			show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (isShow)
			show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

}
