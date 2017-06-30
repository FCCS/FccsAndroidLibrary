package com.fccs.library.image;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.fccs.library.callback.Callback;
import com.fccs.library.data.LocalDataUtils;

public class ImageUtils {

	private static RequestManager glide;

	private static LocalDataUtils localDataUtils;

	private int ingResId = -1;
	private int failResId = -1;

	private int tag = -1;

	public static ImageUtils getInstance(Context context) {
		localDataUtils = LocalDataUtils.getInstance(context, ImageMode.class);
		glide = Glide.with(context.getApplicationContext());
		return new ImageUtils();
	}

	public static ImageUtils getInstance(Fragment fragment) {
		localDataUtils = LocalDataUtils.getInstance(fragment.getActivity(), ImageMode.class);
		glide = Glide.with(fragment);
		return new ImageUtils();
	}

	public ImageUtils setIngResId(int ingResId) {
		this.ingResId = ingResId;
		return this;
	}

	public ImageUtils setFailResId(int failResId) {
		this.failResId = failResId;
		return this;
	}

	public ImageUtils setTag(int tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * 总是展示
	 * @param url
	 * @param imageView
	 */
	public void loadAlways(String url, ImageView imageView) {
		if (TextUtils.isEmpty(url)) {
			glide.load(failResId).dontAnimate().into(imageView);
		} else {
			glide.load(url).dontAnimate().into(imageView);
		}
	}

	/**
	 * 总是展示
	 * @param drawableId
	 * @param imageView
	 */
	public void loadAlways(int drawableId, ImageView imageView) {
		if (drawableId == 0) {
			glide.load(failResId).dontAnimate().into(imageView);
		} else {
			glide.load(drawableId).dontAnimate().into(imageView);
		}
	}
	
	/**
	 * 总是展示（有error和placeholder）
	 * @param url
	 * @param imageView
	 */
	public void loadAlwaysEP(String url, ImageView imageView) {
		if (TextUtils.isEmpty(url)) {
			glide.load(failResId).dontAnimate().into(imageView);
		} else {
			glide.load(url).error(failResId).dontAnimate().placeholder(ingResId).into(imageView);
		}
	}

	public void load(String url, ImageView imageView) {
		load(url, imageView, null);
	}

	public void load(String url, ImageView imageView, final Callback callback) {
		if (!TextUtils.isEmpty(url) && !localDataUtils.getBooleanDefaulFalse(ImageMode.NO_IMAGE_MODE)) {
			if (ingResId > 0) {
				glide.load(url).error(failResId).placeholder(ingResId).dontAnimate().into(new GlideDrawableImageViewTarget(imageView){
					@Override
					public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
						super.onResourceReady(drawable, anim);
						if (callback != null) {
							callback.onSuccess(drawable, anim);
						}
					}
				});
			} else {
				glide.load(url).error(failResId).dontAnimate().into(new GlideDrawableImageViewTarget(imageView){
					@Override
					public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
						super.onResourceReady(drawable, anim);
						if (callback != null) {
							callback.onSuccess(drawable, anim);
						}
					}
				});
			}
		} else {
			glide.load(failResId).dontAnimate().into(new GlideDrawableImageViewTarget(imageView){
				@Override
				public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
					super.onResourceReady(drawable, anim);
					if (callback != null) {
						callback.onSuccess(drawable, anim);
					}
				}
			});
		}
	}

}
