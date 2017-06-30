/**
 * 项目名：FccsApp
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.callback;

import java.util.List;

/**
 * 类名：MultiChoiceCallback<br>
 * 类描述：TODO<br>
 * 创建者：赵朱佳<br>
 * 创建日期：2016年7月27日 下午4:43:39
 */
public interface MultiChoiceCallback {

	public void onMultiChoice(List<String> choices, String values);

}
