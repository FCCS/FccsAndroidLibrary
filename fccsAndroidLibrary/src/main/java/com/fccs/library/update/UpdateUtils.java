package com.fccs.library.update;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.fccs.library.callback.SingleButtonCallBack;
import com.fccs.library.data.ParamUtils;
import com.fccs.library.net.HttpUtils;
import com.fccs.library.net.RequestCallback;
import com.fccs.library.net.okhttp.UIProgressListener;
import com.fccs.library.notice.DialogUtils;
import com.fccs.library.system.AppUtils;
import com.fccs.library.widget.numberprogress.NumberProgressBar;

import java.io.File;

public class UpdateUtils {

    private static final Object lock = new Object();
    private static final int FORCE_UPDATE = 0x1;
    private static final int UNFORCE_UPDATE = 0x2;
    private static final int NOTIFICATION = 0x3;
    private static UpdateUtils updateUtils;
    private Context context;
    private int logoResID;
    private NumberProgressBar numberProgressBar;
    private boolean forcedUpdate;
    private String message, address;
    private int versionCode;

    private int progress = 0;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private DialogUtils dialogUtils;
    private String appName;

    private boolean isNotification = false;
    @SuppressLint("HandlerLeak")
    private Handler downloadHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FORCE_UPDATE:
                    dialogUtils.alertSingleButton(context, message, new SingleButtonCallBack() {

                        @Override
                        public void onPositive() {
                            onSyncLocalOrNetAPK();
                        }
                    });
                    break;
                case UNFORCE_UPDATE:
                    dialogUtils.alert(context, message, new SingleButtonCallBack() {

                        @Override
                        public void onPositive() {
                            onSyncLocalOrNetAPK();
                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public static UpdateUtils getInstance() {
        if (updateUtils == null) {
            synchronized (lock) {
                if (updateUtils == null) {
                    updateUtils = new UpdateUtils();
                }
            }
        }
        return updateUtils;
    }

    public void update(Context context, String channel, int logoResID, int appId, int clientTypeId, final boolean... isHome) {

        this.context = context;
        this.logoResID = logoResID;

        dialogUtils = DialogUtils.getInstance();

        appName = AppUtils.getApplicationName(context.getApplicationContext());

        ParamUtils param = ParamUtils.getInstance()
                .setURL("system/version/upgrade.do")
                .setParam("appId", appId)
                .setParam("clientTypeId", clientTypeId)
                .setParam("version", AppUtils.getVersionName(context))
                .setParam("channel", channel)
                .setParam("sdk", AppUtils.getSDK_INT())
                .setParam("versionCode", AppUtils.getVersionCode(context));

        HttpUtils.async(param, new RequestCallback<AppUpdate>(context) {

            @Override
            public void onSuccess(Context context, AppUpdate result) {
                if (result != null) {
                    forcedUpdate = (result.getUpgradeType() == 1) ? true : false;
                    message = result.getMessage();
                    address = result.getDownloadAddress();
                    versionCode = result.getVersionCode();

                    Message message = new Message();
                    if (forcedUpdate) {
                        dialogUtils.force();
                        message.what = FORCE_UPDATE;
                    } else {
                        message.what = UNFORCE_UPDATE;
                    }
                    downloadHandler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Context context, String error) {
                if (isHome.length > 0) {
                    DialogUtils.getInstance().toast(context, "当前已经是最新版本");
                }
            }

        });
    }

    private void onDownloadAPK(String address, final String saveFilePath) {
        numberProgressBar = dialogUtils.alertNumberProgress(context, new SingleButtonCallBack() {

            @Override
            public void onPositive() {
                notification();
                DialogUtils.getInstance().hideAlert();
            }
        });
        HttpUtils.download(address + "?time=" + System.currentTimeMillis(), saveFilePath, new UIProgressListener() {

            @Override
            public void onUIStart() {
            }

            @Override
            public void onUIProgress(long bytes, long contentLength) {
                progress = (int) (bytes * 100 / contentLength);
                if (isNotification) {
                    notificationProgress();
                } else {
                    numberProgressBar.setProgress(progress);
                }
            }

            @Override
            public void onUIError(String error) {
                DialogUtils.getInstance().hideAlert();
                DialogUtils.getInstance().toast(context, error);
            }

            @Override
            public void onUIDone() {
                DialogUtils.getInstance().hideAlert();
                if (!isNotification) {
                    onInstallApk(saveFilePath);
                } else {
                    notificationFinish(saveFilePath);
                }
            }
        });
    }

    private void notification() {
        isNotification = true;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(logoResID)
                .setContentTitle(appName)
                .setContentText("正在下载")
                .setTicker(appName + "正在下载...")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(100, progress, false);

        notificationManager.notify(NOTIFICATION, builder.build());
    }

    private void notificationProgress() {
        builder.setProgress(100, progress, false);
        notificationManager.notify(NOTIFICATION, builder.build());
    }

    private void notificationFinish(String saveFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(saveFilePath)),
                "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTicker(appName + "已下载完成，点击安装")
                .setContentText("下载完成，点击安装")
                .setProgress(0, 0, false)
                .setOngoing(false);
        notificationManager.notify(NOTIFICATION, builder.build());
    }

    private String getAPK_PATH(String apkURL) {
        String[] spliteString = apkURL.split("/");
        String apkName = spliteString[spliteString.length - 1];
        String apkPath = Environment.getExternalStorageDirectory() + "/" + AppUtils.getPackageName(context) + "/"
                + apkName;
        return apkPath;
    }

    /**
     * 获取本地或者网络上的APK 添加者：倪少强 添加时间：2016年2月16日 下午2:07:44：
     */
    private void onSyncLocalOrNetAPK() {
        String apkPath = getAPK_PATH(address);
        File apk = new File(apkPath);
        if (!apk.getParentFile().exists()) {
            apk.getParentFile().mkdirs();
        }
        if (apk.exists()) {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            int localVersionCode = packageInfo.versionCode;

            if (localVersionCode == versionCode) {
                onInstallApk(apkPath);
            } else {
                onDownloadAPK(address, apkPath);
            }

        } else {
            onDownloadAPK(address, apkPath);
        }
    }

    /**
     * 安装APK 添加者：倪少强 添加时间：2016年2月16日 下午2:08:02
     *
     * @param apkPath：
     */
    private void onInstallApk(String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        context.startActivity(intent);

        if (forcedUpdate) {
            ((Activity) context).finish();
            System.exit(0);
        }
    }

}
