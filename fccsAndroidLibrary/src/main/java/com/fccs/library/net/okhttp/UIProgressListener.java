package com.fccs.library.net.okhttp;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class UIProgressListener implements ProgressListener {

	private static final int PROGRESS_START = 0x01;
	private static final int PROGRESS_UPDATE = 0x02;
	private static final int PROGRESS_DONE = 0x03;

	private boolean isFirst = true;

	// 处理UI层的Handler子类
	private static class UIHandler extends Handler {
		// 弱引用
		private final WeakReference<UIProgressListener> mUIProgressListenerWeakReference;

		public UIHandler(Looper looper, UIProgressListener UIProgressListener) {
			super(looper);
			mUIProgressListenerWeakReference = new WeakReference<UIProgressListener>(UIProgressListener);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROGRESS_START: {
				UIProgressListener UIProgressListener = mUIProgressListenerWeakReference.get();
				if (UIProgressListener != null) {
					UIProgressListener.onUIStart();
				}
				break;
			}
			case PROGRESS_UPDATE: {
				UIProgressListener UIProgressListener = mUIProgressListenerWeakReference.get();
				if (UIProgressListener != null) {
					// 获得进度实体类
					Progress progress = (Progress) msg.obj;
					// 回调抽象方法
					UIProgressListener.onUIProgress(progress.getCurrentBytes(), progress.getContentLength());
				}
				break;
			}
			case PROGRESS_DONE: {
				UIProgressListener UIProgressListener = mUIProgressListenerWeakReference.get();
				if (UIProgressListener != null) {
					// 回调抽象方法
					UIProgressListener.onUIDone();
				}
				break;
			}
			default:
				super.handleMessage(msg);
				break;
			}
		}
	}

	// 主线程Handler
	private final Handler mHandler = new UIHandler(Looper.getMainLooper(), this);

	@Override
	public void onProgress(long bytes, long contentLength, boolean done) {

		if (isFirst) {
			Message start = Message.obtain();
			start.what = PROGRESS_START;
			mHandler.sendMessage(start);
			isFirst = false;
		}

		// 通过Handler发送进度消息
		Message update = Message.obtain();
		update.obj = new Progress(bytes, contentLength);
		update.what = PROGRESS_UPDATE;
		mHandler.sendMessage(update);
		
		if (done) {
			Message message = Message.obtain();
			message.what = PROGRESS_DONE;
			mHandler.sendMessage(message);
		}
	}

	/**
	 * 开始请求时UI层回调抽象方法
	 * 
	 * @param currentBytes
	 *            当前的字节长度
	 * @param contentLength
	 *            总字节长度
	 * @param done
	 *            是否写入完成
	 */
	public abstract void onUIStart();

	/**
	 * 刷新进度时UI层回调抽象方法
	 * 
	 * @param bytes
	 *            当前写入的字节长度
	 * @param contentLength
	 *            总字节长度
	 * @param done
	 *            是否写入完成
	 */
	public abstract void onUIProgress(long bytes, long contentLength);

	/**
	 * 完成时UI层回调抽象方法
	 */
	public abstract void onUIDone();

	/**
	 * 发生错误时UI层回调抽象方法
	 * 
	 * @param error：错误信息
	 */
	public abstract void onUIError(String error);
	
	/**
	 * 返回结果时UI层回调抽象方法
	 * 
	 * @param result：结果
	 */
	public void onUIResult(String result) {};
}
