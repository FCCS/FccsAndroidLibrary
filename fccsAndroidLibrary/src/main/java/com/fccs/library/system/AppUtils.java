/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.system;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.fccs.library.notice.DialogUtils;

/**
 * 类名：AppUtils<br>
 * 类描述：系统工具<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月11日 下午3:28:35
 */
public class AppUtils {

	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		display.getMetrics(metrics);
		int width = metrics.widthPixels;
		return width;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		display.getMetrics(metrics);
		int height = metrics.heightPixels;
		return height;
	}

	public static float getScreenDensity(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		display.getMetrics(metrics);
		float density = metrics.density;
		return density;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = getScreenDensity(context);
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 获取应用名称
	 * 
	 * @param application
	 * @return
	 */
	public static String getApplicationName(Context application) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = application.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(application.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	// 版本名称
	public static String getVersionName(Context context) {
		try {
			PackageManager packagemanager = context.getPackageManager();
			PackageInfo packageInfo = packagemanager.getPackageInfo(getPackageName(context), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	public static int getVersionCode(Context context) {
		try {
			PackageManager packagemanager = context.getPackageManager();
			PackageInfo packageInfo = packagemanager.getPackageInfo(getPackageName(context), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static String getSignature(Context act) {
		try {
			PackageManager packageManager = act.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(act), PackageManager.GET_SIGNATURES);
			Signature[] signatures = packageInfo.signatures;
			Signature signature = signatures[0];
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			byte[] bs = signature.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bs);
			X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(bais);
			StringBuffer sb = new StringBuffer();
			sb.append(cert.getIssuerDN().toString());
			sb.append(cert.getSerialNumber().toString());
			sb.append(getPackageName(act).substring(0, 9));
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return getPackageName(act).substring(0, 9);
		}
	}

	/**
	 * 获取AndroidManifest.xml里面的meta-data
	 */
	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(getPackageName(context),
					PackageManager.GET_META_DATA);
			if (ai != null) {
				Bundle b = ai.metaData;
				if (b != null) {
					return b.getString(key, "");
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	// 设备标识号
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();// 移动通信国际识别码(串号)
		if (!TextUtils.isEmpty(imei)) {
			return imei;
		} else {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifi.getConnectionInfo().getMacAddress();
			if (!TextUtils.isEmpty(mac)) {
				return mac;
			} else {
				String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
				if (!TextUtils.isEmpty(android_id)) {
					return android_id;
				} else {
					return "";
				}
			}
		}
	}

	// 手机型号
	public static String getModel() {
		String model = Build.MODEL.trim();
		if (!TextUtils.isEmpty(model)) {
			return model.replace(" ", "");
		} else {
			return "";
		}
	}

	// 手机厂商
	public static String getManufacturer() {
		String manufacturer = Build.MANUFACTURER.trim();
		if (!TextUtils.isEmpty(manufacturer)) {
			return manufacturer;
		} else {
			return "";
		}
	}

	// OS版本
	public static String getRelease() {
		String release = Build.VERSION.RELEASE;// Android版本
		if (!TextUtils.isEmpty(release)) {
			return release;
		} else {
			return "";
		}
	}

	public static void call(Context context, String number) {
		if (AppUtils.isCanUseSim(context)) {
			if (!TextUtils.isEmpty(number)) {
				// 直接拨号
				Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
				// 启动
				context.startActivity(phoneIntent);
			} else {
				DialogUtils.getInstance().toast(context, "没有号码怎么能打电话呢？");
			}
		} else {
			DialogUtils.getInstance().toast(context, "请检查您的SIM卡");
		}
	}

	public static void sms(Context context, String number, String msg) {
		if (isCanUseSim(context)) {
			if (!TextUtils.isEmpty(number)) {
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
				intent.putExtra("sms_body", msg);
				context.startActivity(intent);
			} else {
				DialogUtils.getInstance().toast(context, "没有号码怎么能发短信呢？");
			}
		} else {
			DialogUtils.getInstance().toast(context, "请检查您的SIM卡");
		}
	}

	// 判断sim卡是否可用
	public static boolean isCanUseSim(Context context) {
		try {
			TelephonyManager tpm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return TelephonyManager.SIM_STATE_READY == tpm.getSimState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取系统可用内存
	public static long getSystemAvailMemery(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	public static int getSDK_INT() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 判断系统中是否存在可以启动的相机应用
	 *
	 * @return 存在返回true，不存在返回false
	 */
	public static boolean hasCamera(Context context) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static void hideSoftInput(Context context, View view) {
		InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (manager.isActive(view)) {
			manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
