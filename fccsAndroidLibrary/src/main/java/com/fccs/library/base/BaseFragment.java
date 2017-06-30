package com.fccs.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseFragment extends Fragment {
	
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
		startActivityForResult(intent, requestCode);
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
	 * 初始化界面
	 */
	protected abstract void initLayoutViews();
    
}
