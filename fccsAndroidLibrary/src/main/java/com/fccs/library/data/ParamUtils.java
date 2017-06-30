/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.data;

import com.fccs.library.log.Logger;
import com.fccs.library.net.ServerConfigUtils;

import android.text.TextUtils;

/**
 * 类名：ParamUtils<br>
 * 类描述：参数工具<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月11日 下午3:19:05
 */
public class ParamUtils {

    private StringBuilder param = new StringBuilder();

    private String url;

    public static ParamUtils getInstance() {
        return new ParamUtils();
    }

    public String getURL() {
        return url;
    }

    public ParamUtils setURL(String url) {
        this.url = ServerConfigUtils.getInstance().getServerAddress() + url;
        return this;
    }

    public ParamUtils setParam(String key, Object value) {
        param.append(key);
        param.append("=");
        param.append(value);
        param.append("&");
        return this;
    }
    
    public String getParam() {
		if (TextUtils.isEmpty(url)) {
			throw new IllegalArgumentException("You have not set the interface url!!!");
		}

		if (param.length() != 0) {
    		param.deleteCharAt(param.length() - 1);
    	}

        Logger.e(url + "?" + param.toString());
        
        try {
            return DesUtils.encrypt(param.toString());
        } catch (Exception e) {
            return "";
        }
    }
}
