package com.fccs.library.system;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ResourcesUtils {
	
	public static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
    }
	
	@SuppressWarnings("deprecation")
	public static int getColor(Context context, int resId) {
		return context.getResources().getColor(resId);
	}
	
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(Context context, int resId) {
		return context.getResources().getDrawable(resId);
	}

}
