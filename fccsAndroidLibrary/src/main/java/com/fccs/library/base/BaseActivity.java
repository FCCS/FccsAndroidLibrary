/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * 类名：BaseActivity<br>
 * 类描述：基础Activity<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月11日 下午3:15:02
 */
public abstract class BaseActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * Activity跳转 添加者：倪少强 添加时间：2016年1月21日 下午12:00:41
	 * 
	 * @param activity
	 * @param cls
	 * @param bundle
	 */
	public void startActivity(AppCompatActivity activity, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		activity.startActivity(intent);
	}

	/**
	 * Activity跳转 添加者：倪少强 添加时间：2016年3月10日 下午2:08:20
	 * 
	 * @param activity
	 * @param cls
	 * @param bundle
	 * @param requestCode：
	 */
	public void startActivityForResult(AppCompatActivity activity, Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * Activity跳转 添加者：倪少强 添加时间：2016年1月22日 下午3:29:51
	 * 
	 * @param activity
	 * @param cls
	 * @param bundle
	 */
	public void startActivityWithFinish(AppCompatActivity activity, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(activity, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * 点击事件 添加者：倪少强 添加时间：2016年1月21日 下午12:00:29
	 * 
	 * @param v
	 */
	public void onViewClick(View v) {
	}

	/**
	 * 初始化界面
	 */
	protected abstract void initLayoutViews();

}
