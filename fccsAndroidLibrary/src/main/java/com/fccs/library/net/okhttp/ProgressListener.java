package com.fccs.library.net.okhttp;

public interface ProgressListener {

	public void onProgress(long bytes, long contentLength, boolean done);
	
}
