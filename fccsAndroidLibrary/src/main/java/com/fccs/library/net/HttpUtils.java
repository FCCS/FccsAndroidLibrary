/**
 * 项目名：FccsAndroidLibrary
 * Copyright © 2016 浙江房超信息科技有限公司.All Rights Reserved.
 */
package com.fccs.library.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.fccs.library.base.BaseParser;
import com.fccs.library.data.JsonUtils;
import com.fccs.library.data.ParamUtils;
import com.fccs.library.net.okhttp.DownloadBody;
import com.fccs.library.net.okhttp.UIProgressListener;
import com.fccs.library.net.okhttp.UploadBody;
import com.fccs.library.notice.DialogUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 类名：HttpUtils<br>
 * 类描述：网络请求工具<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月12日 上午10:08:22
 */
public class HttpUtils {
	
//	private static OkHttpClient.Builder builder = new OkHttpClient.Builder()
//			.connectTimeout(15, TimeUnit.SECONDS)
//			.writeTimeout(20, TimeUnit.SECONDS)
//			.readTimeout(20, TimeUnit.SECONDS);
//	
//	private static OkHttpClient okHttpClient;
	
	public static <T> void async(ParamUtils param, RequestCallback<T> callback) {
		
		String network = NetworkUtils.getNetworkType(callback.getContext());
		
		if (!TextUtils.isEmpty(network)) {
			FormBody formBody = new FormBody.Builder().add("param", param.getParam()).build();
			
			Request request = new Request.Builder().url(param.getURL())
					.header("User-Agent", "Android").post(formBody).build(); 
			
			OkHttpClient.Builder builder = new OkHttpClient.Builder()
					.connectTimeout(10, TimeUnit.SECONDS)
					.writeTimeout(20, TimeUnit.SECONDS)
					.readTimeout(20, TimeUnit.SECONDS);
			
			OkHttpClient okHttpClient = builder.build();
			
			Call call = okHttpClient.newCall(request); 
			
			call.enqueue(callback); 
		} else {
			DialogUtils.getInstance().hideAlert();
		}
		
    }
    
    public static void download(String url, final String saveFilePath, final UIProgressListener listener) {
    	Request request = new Request.Builder().url(url).header("User-Agent", "Android").build();
    	
    	OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS);
		
		OkHttpClient okHttpClient;
		
    	if (listener != null) {
    		okHttpClient = builder.addInterceptor(new Interceptor() {
    			@Override
    			public Response intercept(Chain chain) throws IOException {
    				// 拦截
    				Response originalResponse = chain.proceed(chain.request());
    				// 包装响应体并返回
    				return originalResponse.newBuilder()
    						.body(new DownloadBody(originalResponse.body(), listener)).build();
    			}
    		}).build();
    	} else {
    		okHttpClient = builder.build();
    	}
    	
    	Call call = okHttpClient.newCall(request); 
        
        call.enqueue(new Callback() {
			
        	@Override
        	public void onResponse(Call call, Response response) {
        		try {
        			if (response.isSuccessful()) {
        				InputStream is = response.body().byteStream();
        				File file = new File(saveFilePath);
        				FileOutputStream fos = new FileOutputStream(file);
        				BufferedInputStream bis = new BufferedInputStream(is);
        				byte[] buffer = new byte[1024];
        				int len;
        				while ((len = bis.read(buffer)) != -1) {
        					fos.write(buffer, 0, len);
        					fos.flush();
        				}
        				fos.close();
        				bis.close();
        				is.close();
        				call.cancel();
        			} else {
        				sendFailure(call, "下载失败");
        			}
        		} catch (Exception e) {
        			if (TextUtils.isEmpty(e.getMessage())) {
        				sendFailure(call, "连接超时");
        			} else {
        				sendFailure(call, e.getMessage());
        			}
        		}
        	}

        	@Override
        	public void onFailure(Call call, IOException e) {
        		sendFailure(call, "服务器连接失败");
        	}

        	private void sendFailure(final Call call, final String error) {
        		new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if (listener != null) {
							listener.onUIError(error);
						}
						call.cancel();
					}
				});
        	}
		}); 
    }
    
    public static void upload(ParamUtils param, String filePath, final UIProgressListener listener) {

		File file = new File(filePath);
		
		if (listener == null) {
			throw new IllegalArgumentException("UIProgressListener can not be null");
		}
		
		if (!file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			if (bitmap == null) {
				throw new IllegalArgumentException("文件不存在");
			} else {
				throw new IllegalArgumentException("图片不存在");
			}
		}

		// 构造上传请求，类似web表单
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("param", param.getParam())
				.addFormDataPart("file", file.getName(), RequestBody.create(null, file)).build();
		
		Request request = new Request.Builder().url(param.getURL()).header("User-Agent", "Android")
				.post(new UploadBody(requestBody, listener)).build();
		
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS);
		
		OkHttpClient okHttpClient = builder.build();

		Call call = okHttpClient.newCall(request);
        
        call.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) {
				try {
					if (response.isSuccessful()) {
						if (listener != null) {
							BaseParser baseParser = JsonUtils.getParser(response.body().string());
							if (baseParser.getRet() == 1) {
								sendResult(call, baseParser.getData());
							} else {
								sendFailure(call, baseParser.getMsg());
							}
						}
					} else {
						sendFailure(call, "上传失败");
					}
				} catch (Exception e) {
					if (TextUtils.isEmpty(e.getMessage())) {
						sendFailure(call, "连接超时");
					} else {
						sendFailure(call, e.getMessage());
					}
				}
			}
			
			@Override
        	public void onFailure(Call call, IOException e) {
        		sendFailure(call, "服务器连接失败");
        	}
			
			private void sendResult(final Call call, final String data) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if (listener != null) {
							listener.onUIResult(data);
						}
						call.cancel();
					}
				});
			}

        	private void sendFailure(final Call call, final String error) {
        		new Handler(Looper.getMainLooper()).post(new Runnable() {
					
					@Override
					public void run() {
						if (listener != null) {
							listener.onUIError(error);
						}
						call.cancel();
					}
				});
        	}
        	
        });
    }

	public static void upload(ParamUtils param, File file, final UIProgressListener listener) {

		if (listener == null) {
			throw new IllegalArgumentException("UIProgressListener can not be null");
		}

		if (!file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (bitmap == null) {
				throw new IllegalArgumentException("文件不存在");
			} else {
				throw new IllegalArgumentException("图片不存在");
			}
		}

		// 构造上传请求，类似web表单
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("param", param.getParam())
				.addFormDataPart("file", file.getName(), RequestBody.create(null, file)).build();

		Request request = new Request.Builder().url(param.getURL()).header("User-Agent", "Android")
				.post(new UploadBody(requestBody, listener)).build();

		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS);

		OkHttpClient okHttpClient = builder.build();

		Call call = okHttpClient.newCall(request);

		call.enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) {
				try {
					if (response.isSuccessful()) {
						if (listener != null) {
							BaseParser baseParser = JsonUtils.getParser(response.body().string());
							if (baseParser.getRet() == 1) {
								sendResult(call, baseParser.getData());
							} else {
								sendFailure(call, baseParser.getMsg());
							}
						}
					} else {
						sendFailure(call, "上传失败");
					}
				} catch (Exception e) {
					if (TextUtils.isEmpty(e.getMessage())) {
						sendFailure(call, "连接超时");
					} else {
						sendFailure(call, e.getMessage());
					}
				}
			}

			@Override
			public void onFailure(Call call, IOException e) {
				sendFailure(call, "服务器连接失败");
			}

			private void sendResult(final Call call, final String data) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						if (listener != null) {
							listener.onUIResult(data);
						}
						call.cancel();
					}
				});
			}

			private void sendFailure(final Call call, final String error) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						if (listener != null) {
							listener.onUIError(error);
						}
						call.cancel();
					}
				});
			}

		});
	}

}
