package com.fccs.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import com.fccs.library.R;
import com.fccs.library.notice.DialogUtils;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * 类名：ProgressWebView<br>
 * 类描述：带进度条的webview<br>
 * 创建人：倪少强<br>
 * 创建日期：2016年4月13日 下午6:35:39
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends com.tencent.smtt.sdk.WebView {

    private ProgressBar progressbar;

    private Context context;

    private WebSettings settings;
    
    private WebCallBack callback;

	@SuppressLint("SetJavaScriptEnabled")
	public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
		setBackgroundColor(0);
		settings = getSettings();
		settings.setJavaScriptEnabled(true);
		requestFocus();
		requestFocusFromTouch();
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.layer_list_web_progress));
        progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.MATCH_PARENT, 6, 0, 0));

        addView(progressbar);

        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        setWebViewClient(new WebViewClient());

        setWebChromeClient(new WebChromeClient());

        setOnKeyListener(new BackListener());
    }

    private class WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            DialogUtils.getInstance().alert(context, message, null);
            result.confirm();
            return true;
        }

    }

	@Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
		AbsoluteLayout.LayoutParams layoutParams = (android.widget.AbsoluteLayout.LayoutParams) progressbar.getLayoutParams();
//        layoutParams.leftMargin = left;
//        layoutParams.topMargin = top;
        progressbar.setLayoutParams(layoutParams);
        super.onScrollChanged(left, top, oldLeft, oldTop);
    }

    private class WebViewClient extends com.tencent.smtt.sdk.WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            url = url.replace("wtai://wp/mc;", "tel:");
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
                return true;
            } else {
                return false;
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (callback != null) {
                callback.onPageFinished(getTitle());
            }
        }
    }

    private class BackListener implements OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && ProgressWebView.this.canGoBack()) {
                    ProgressWebView.this.goBack();   //后退
                    return true;    //已处理
                }
            }
            return false;
        }

    }

    public void setUserAgent(String userAgent) {
        getSettings().setUserAgent(userAgent);
    }
    
    public void setOnWebCallBack(WebCallBack callBack) {
    	this.callback = callBack;
    }

    public WebSettings getWebSettings() {
        return settings;
    }
    
    public interface WebCallBack {
        void onPageFinished(String title);
    }
}
