/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.net;

import com.fccs.library.notice.DialogUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 类名：NetworkUtils<br>
 * 类描述：网络检查<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月11日 下午3:23:18
 */
public class NetworkUtils {

    @SuppressLint("DefaultLocale")
	public static String getNetworkType(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            DialogUtils.getInstance().toast(context, "当前网络不可用，请检查网络连接");
            return "";
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String type = info.getTypeName();// MOBILE;WIFI
            if (!TextUtils.isEmpty(type)) {
                type = type.toUpperCase();
                return type;
            } else {
                return "";
            }
        } else {
            DialogUtils.getInstance().toast(context, "当前网络不可用，请检查网络连接");
            return "";
        }
    }
}
