/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.data;

import java.lang.reflect.Type;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fccs.library.base.BaseParser;

import android.text.TextUtils;

/**
 * 类名：JsonUtils<br>
 * 类描述：JSON工具类<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月12日 上午10:20:01
 */
public class JsonUtils {

    public static BaseParser getParser(String json) {
        try {
            String s = DesUtils.decrypt(json);
            if (TextUtils.isEmpty(s)) {
                return null;
            } else {
                return JSON.parseObject(s, new TypeReference<BaseParser>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T parser(String data, Type bean) {
        try {
            if (TextUtils.isEmpty(data)) {
                return null;
            } else {
                return JSON.parseObject(data, bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static <T> T getBean(String data, Class<T> bean) {
//        try {
//            if (TextUtils.isEmpty(data)) {
//                return null;
//            } else {
//                return JSON.parseObject(data, bean);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
    public static <T> List<T> getList(String data, Class<T> bean) {
        try {
            if (TextUtils.isEmpty(data)) {
                return null;
            } else {
                return JSON.parseArray(data, bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getURL(String data){
        String URL = JSON.parseObject(data).getString("url");
        return URL;
    }

    public static String getString(String data, String param){
        String result = JSON.parseObject(data).getString(param);
        return result;
    }

    public static int getInt(String data, String param){
        int result = JSON.parseObject(data).getInteger(param);
        return result;
    }
}
