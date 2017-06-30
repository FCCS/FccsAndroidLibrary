package com.fccs.library.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fccs.library.base.BaseParser;
import com.fccs.library.data.JsonUtils;
import com.fccs.library.log.Logger;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Ethen on 2016/10/24.
 */

public abstract class RequestErrorCallback <T> extends RequestCallback<T> implements Callback {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private Context context;

    public RequestErrorCallback(Context context) {
        super(context);
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        sendFailure(call, "服务器连接失败");
    }

    @Override
    public void onResponse(final Call call, final Response response) {
        try {
            if (response.isSuccessful()) {
                BaseParser baseParser = JsonUtils.getParser(response.body().string());

                Logger.json(baseParser.getData());

                if (baseParser.getRet() == 1) {
                    T type = JsonUtils.parser(baseParser.getData(), getType());
                    sendSuccess(call, type);
                } else {
                    sendFailure(call, baseParser.getMsg(), baseParser.getErrno());
                }
            } else {
                sendFailure(call, "请求失败");
            }
        } catch (Exception e) {
            sendFailure(call, e.getMessage());
        }
    }

    private void sendSuccess(final Call call, final T type) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                onSuccess(context, type);
                call.cancel();
            }

        });
    }

    private void sendFailure(final Call call, final String error, final int... errorno) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                onFailure(context, error, errorno);
                call.cancel();
            }

        });
    }

    public Type getType() {
        return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public abstract void onSuccess(Context context, T result);

    public abstract void onFailure(Context context, String error, int... errorno);

}
