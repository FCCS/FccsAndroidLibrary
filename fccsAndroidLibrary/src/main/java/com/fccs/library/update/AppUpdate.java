package com.fccs.library.update;

public class AppUpdate {

	private int upgradeType;// 是否强制
	private String message;// 更新消息
	private String downloadAddress;// apk下载链接
	private int versionCode;// apk版本号

	public int getUpgradeType() {
		return upgradeType;
	}

	public void setUpgradeType(int upgradeType) {
		this.upgradeType = upgradeType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}
