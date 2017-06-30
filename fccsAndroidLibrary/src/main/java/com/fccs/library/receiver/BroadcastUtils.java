package com.fccs.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadcastUtils {

	private static final Object lock = new Object();
	private static BroadcastUtils broadcastUtils;

	private Context context;

	private BroadcastUtils(Context context) {
		this.context = context;
	}

	public static BroadcastUtils getInstance(Context context) {
		if (broadcastUtils == null) {
			synchronized (lock) {
				if (broadcastUtils == null) {
					broadcastUtils = new BroadcastUtils(context);
				}
			}
		}
		return broadcastUtils;
	}

	public void registerReceiver(BroadcastReceiver receiver, String action) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(action);
		filter.setPriority(Integer.MAX_VALUE);
		context.registerReceiver(receiver, filter);
	}
	
	public void unRegisterReceiver(BroadcastReceiver receiver) {
		if (receiver != null) {
			context.unregisterReceiver(receiver);
		}
	}

	public void sendBroadcast(String action) {
		context.sendBroadcast(new Intent(action));
	}

	public void sendBroadcast(Intent intent) {
		context.sendBroadcast(intent);
	}
}
