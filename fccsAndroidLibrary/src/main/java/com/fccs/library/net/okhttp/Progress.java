package com.fccs.library.net.okhttp;

import java.io.Serializable;

public class Progress implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 当前读取字节长度
	private long currentBytes;
	// 总字节长度
	private long contentLength;

	public Progress(long currentBytes, long contentLength) {
		this.currentBytes = currentBytes;
		this.contentLength = contentLength;
	}

	public long getCurrentBytes() {
		return currentBytes;
	}

	public void setCurrentBytes(long currentBytes) {
		this.currentBytes = currentBytes;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	@Override
	public String toString() {
		return "Progress {" + "currentBytes=" + currentBytes + ", contentLength=" + contentLength + "}" ;
	}
}