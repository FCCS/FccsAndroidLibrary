package com.fccs.library.system;

import com.fccs.library.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class ToolbarUtils {

	private static Toolbar toolbar;
	private static SystemBarTintManager tintManager;

	/**
	 * 设置页面标题
	 * 
	 * @param title
	 */
	public static Toolbar initToolbar(final AppCompatActivity activity, String title, int naviResId) {
		toolbar = (Toolbar) activity.findViewById(R.id.tl_title);
		if (!TextUtils.isEmpty(title)) {
			toolbar.setTitle(title);
		} else {
			toolbar.setTitle("");
		}
		activity.setSupportActionBar(toolbar);
		ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null && TextUtils.isEmpty(title)) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		if (naviResId != 0) {
			toolbar.setNavigationIcon(naviResId);
			toolbar.setNavigationOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
		}
		return toolbar;
	}

	/**
	 * 设置页面标题
	 * 
	 * @param activity
	 * @param toolbabID
	 * @param title
	 * @param naviResId
	 */
	public static Toolbar initToolbar(final AppCompatActivity activity, int toolbabID, String title, int naviResId) {
		toolbar = (Toolbar) activity.findViewById(toolbabID);
		if (!TextUtils.isEmpty(title)) {
			toolbar.setTitle(title);
		} else {
			toolbar.setTitle("");
		}
		activity.setSupportActionBar(toolbar);
		ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		if (naviResId != 0) {
			toolbar.setNavigationIcon(naviResId);
			toolbar.setNavigationOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
		}
		return toolbar;
	}

	/**
	 * 获取toolbar
	 * 
	 * @param activity
	 * @return
	 */
	public static Toolbar getToolbar(AppCompatActivity activity) {
		toolbar = (Toolbar) activity.findViewById(R.id.tl_title);
		activity.setSupportActionBar(toolbar);
		ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		return toolbar;
	}

	/**
	 * 获取toolbar
	 * 
	 * @param activity
	 * @param toolbabID
	 * @return
	 */
	public static Toolbar getToolbar(AppCompatActivity activity, int toolbabID) {
		toolbar = (Toolbar) activity.findViewById(toolbabID);
		activity.setSupportActionBar(toolbar);
		ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		return toolbar;
	}

	/**
	 * 设置自定义View
	 * 
	 * @param activity
	 * @param layoutResID
	 * @return
	 */
	public static View setCustomView(AppCompatActivity activity, int layoutResID) {
		View customView = null;
		ActionBar actionBar = activity.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowCustomEnabled(true);
			customView = LayoutInflater.from(activity).inflate(layoutResID, null);
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
					ActionBar.LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.CENTER_HORIZONTAL;
			actionBar.setCustomView(customView, params);
		}
		return customView;
	}

	@SuppressLint("NewApi")
	public static void initStatusBar(AppCompatActivity activity, int statusBarColor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4 全透明状态栏
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintResource(statusBarColor);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarAlpha(100);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 全透明实现
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);// calculateStatusColor(Color.WHITE,
														// (int) alphaValue)
		}
	}
	
	@SuppressLint("NewApi")
	public static void resetStatusBar(AppCompatActivity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintColor(Color.BLACK);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarAlpha(100);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.BLACK);
		}
	}

}
