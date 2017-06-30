/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.net;

/**
 * 类名：ServerConfig<br>
 * 类描述：<br>
 * 创建人：赵朱佳<br>
 * 创建日期：2014年3月26日
 */
public class ServerConfigUtils {
	
	private static final Object lock = new Object();
	
	private String serverAddress;
	private static ServerConfigUtils instance;
	
	public static boolean DEBUG = false;

	private ServerConfigUtils() {
		if (DEBUG) {
			serverAddress = "http://testapi.fccs.cn/";// 测试
		} else {
			serverAddress = "http://api2.m.fccs.com/";// 正式
		}
	}

	public static ServerConfigUtils getInstance() {
		if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                	instance = new ServerConfigUtils();
                }
            }
        }
		return instance;
	}

	public String getServerAddress() {
		return serverAddress;
	}
}