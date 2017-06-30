/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.data;

import java.util.List;

/**
 * 类名：EmptyUtils<br>
 * 类描述：判空<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年1月21日 下午1:35:57
 */
public class EmptyUtils {
	
	public static <T> boolean isEmpty(List<T> list) {
		if (list == null || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

}
