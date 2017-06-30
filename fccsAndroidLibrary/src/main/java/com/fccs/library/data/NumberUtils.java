/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.data;

import android.text.TextUtils;

/**
 * 类名：NumberUtils<br>
 * 类描述：数值工具<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月12日 上午9:23:05
 */
public class NumberUtils {

	public static double parseDouble(String str) {
		if (TextUtils.isEmpty(str)) {
			return 0.0;
		} else {
			return Double.parseDouble(str);
		}
	}
	
	public static float parseFloat(String str) {
		if (TextUtils.isEmpty(str)) {
			return 0;
		} else {
			return Float.parseFloat(str);
		}
	}
	
	public static int parseInt(String str) {
		if (TextUtils.isEmpty(str)) {
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}
	
}
