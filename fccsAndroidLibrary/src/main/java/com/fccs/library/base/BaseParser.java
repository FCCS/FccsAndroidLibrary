/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.base;

import java.io.Serializable;

/**
 * 类名：BaseParser<br>
 * 类描述：基础解析类<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月11日 下午3:15:46
 */
public class BaseParser implements Serializable {

	private static final long serialVersionUID = 1L;

	private int ret; // 返回值 1：成功 0：失败
	private int errno; // 错误编码
	private String msg; // 错误信息
	private String data; // 返回数据

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
