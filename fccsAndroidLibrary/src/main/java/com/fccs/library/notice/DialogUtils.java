/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.notice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fccs.library.R;
import com.fccs.library.callback.ItemCallback;
import com.fccs.library.callback.MultiChoiceCallback;
import com.fccs.library.callback.SingleButtonCallBack;
import com.fccs.library.data.EmptyUtils;
import com.fccs.library.widget.numberprogress.NumberProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名：DialogUtils<br>
 * 类描述：通知工具<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月11日 下午3:22:33
 */
public class DialogUtils {

	private static final Object lock = new Object();

	private static DialogUtils dialogUtils;

	public AlertDialog dialog;

	private boolean cancelable = true;

	public static DialogUtils getInstance() {
		if (dialogUtils == null) {
			synchronized (lock) {
				if (dialogUtils == null) {
					dialogUtils = new DialogUtils();
				}
			}
		}
		return dialogUtils;
	}

	/**
	 * 设置弹出框无法取消
	 * 添加者：倪少强 
	 * 添加时间：2016年4月12日 下午2:28:34
	 * @return：
	 */
	public DialogUtils force() {
		cancelable = false;
		return this;
	}

	/**
	 * toast提示 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月12日 上午9:13:14
	 * @param context
	 * @param text
	 */
	public void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public void toast(Context context, int textId) {
		Toast.makeText(context, textId, Toast.LENGTH_SHORT).show();
	}

	public AlertDialog.Builder getBuilder(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
		return builder;
	}

	/**
	 * 等待弹出框 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月11日 下午4:46:44
	 * @param context
	 */
	public void alertProgress(Context context) {
		alertProgress(context, null);
	}

	/**
	 * 等待弹出框(自定义提示内容) 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月11日 下午4:47:30
	 * @param context
	 * @param text
	 */
	public void alertProgress(Context context, String text) {
		View view = View.inflate(context, R.layout.lib_dialog_progress_circle, null);
		TextView txtContent = (TextView) view.findViewById(R.id.txt_content);
		if (!TextUtils.isEmpty(text)) {
			txtContent.setText(text);
		} else {
			txtContent.setText(R.string.wait);
		}
		dialog = getBuilder(context).setView(view).create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
	}

	/**
	 * 下载进度弹出框 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月12日 上午9:11:23
	 * @param context
	 * @return：
	 */
	@SuppressWarnings("deprecation")
	public NumberProgressBar alertNumberProgress(Context context, final SingleButtonCallBack callback) {
		View view = View.inflate(context, R.layout.lib_dialog_progress_number, null);
		NumberProgressBar npbNumber = (NumberProgressBar) view.findViewById(R.id.npb_number);
		npbNumber.setProgressTextColor(context.getResources().getColor(R.color.green_500));
		npbNumber.setReachedBarColor(context.getResources().getColor(R.color.green_500));
		AlertDialog.Builder builder = getBuilder(context);
		builder.setView(view).setTitle("正在下载");
		if (cancelable) {
			builder.setPositiveButton("后台下载", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (callback != null) {
						callback.onPositive();
					}
				}

			});
		}
		dialog = builder.create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
		return npbNumber;
	}

	/**
	 * 普通弹出框 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月11日 下午4:33:49
	 * @param context
	 * @param text
	 * @param callback
	 */
	public void alert(Context context, String text, final SingleButtonCallBack callback) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(text);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callback != null) {
					callback.onPositive();
				}
			}

		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
	}

	/**
	 * 单个按钮弹出框 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月13日 下午2:53:52
	 * @param context
	 * @param text
	 * @param callback
	 */
	public void alertSingleButton(Context context, String text, final SingleButtonCallBack callback) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(text);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callback != null) {
					callback.onPositive();
				}
			}

		});
		dialog = builder.create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
	}

	/**
	 * 单选弹出框 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月11日 下午4:38:36
	 * @param context
	 * @param items
	 */
	public void alertSingleChoice(Context context, String[] items, final ItemCallback callback) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setItems(items, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callback != null) {
					callback.onItemChoice(which);
				}
			}

		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
	}
	
	/**
	 * 单选弹出框 
	 * @param context
	 * @param items
	 * @param checkedItem
	 * @param callback
	 */
	public void alertSingleChoice(Context context, String[] items, int checkedItem, final ItemCallback callback) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setSingleChoiceItems(items, checkedItem, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (callback != null) {
					callback.onItemChoice(which);
				}
			}

		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		dialog = builder.create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
	}

	/**
	 * 多选弹出框 
	 * 添加者：倪少强 
	 * 添加时间：2016年4月11日 下午4:43:30
	 * @param context
	 * @param items
	 */
	public void alertMultiChoice(Context context, final String[] items, String checkedIds, final MultiChoiceCallback callback) {

		final List<String> ids = new ArrayList<String>();
		boolean[] checkedItems = null;
		if (!TextUtils.isEmpty(checkedIds)) {
			checkedItems = new boolean[items.length];
			for (int i = 0; i < items.length; i++) {
				checkedItems[i] = false;
			}
			checkedIds = checkedIds.substring(1);
			checkedIds = checkedIds.substring(0, checkedIds.length() - 1);
			String[] str_ids = checkedIds.split("-");
			for (int j = 0; j < str_ids.length; j++) {
				ids.add(str_ids[j]);
				checkedItems[Integer.valueOf(str_ids[j])] = true;
			}
		}

		final AlertDialog.Builder builder = getBuilder(context);


		builder.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener() {

			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (isChecked) {
					ids.add(which + "");
				} else {
					ids.remove(which + "");
				}
			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				if (callback != null) {
					if (!EmptyUtils.isEmpty(ids)) {
						String values = "-";
						for (int i = 0; i < ids.size(); i++) {
							values += items[Integer.parseInt(ids.get(i))] + "-";
						}
						callback.onMultiChoice(ids, values);
					}else{
						callback.onMultiChoice(null, "");
					}
				}
			}

		});
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
	}

	/**
	 * 隐藏弹出框
	 * 添加者：倪少强 
	 * 添加时间：2016年4月11日 下午4:44:31：
	 */
	public void hideAlert() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

}
