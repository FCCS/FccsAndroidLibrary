package com.fccs.library.image;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

public class BitmapUtils {

	/**
	 * 获取图片的旋转角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照指定的角度进行旋转
	 *
	 * @param bitmap
	 *            需要旋转的图片
	 * @param degree
	 *            指定的旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bitmap, float degree) {
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		return newBitmap;
	}

	public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
		if (!TextUtils.isEmpty(imagePath)) {
			if (requestWidth <= 0 || requestHeight <= 0) {
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
				return bitmap;
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;// 不加载图片到内存，仅获得图片宽高
			BitmapFactory.decodeFile(imagePath, options);
			if (options.outHeight == -1 || options.outWidth == -1) {
				try {
					ExifInterface exifInterface = new ExifInterface(imagePath);
					int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,
							ExifInterface.ORIENTATION_NORMAL);// 获取图片的高度
					int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,
							ExifInterface.ORIENTATION_NORMAL);// 获取图片的宽度
					options.outWidth = width;
					options.outHeight = height;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); // 计算获取新的采样率
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(imagePath, options);
		} else {
			return null;
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
			long totalPixels = width * height / inSampleSize;
			final long totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels > totalReqPixelsCap) {
				inSampleSize *= 2;
				totalPixels /= 2;
			}
		}
		return inSampleSize;
	}
	
	public static float getDegrees(String fileName) {
        try {
            float degrees = 0.0f;
            ExifInterface eif = new ExifInterface(fileName);
            int orientation = eif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90.0f;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180.0f;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270.0f;
                    break;
                default:
                    degrees = 0.0f;
                    break;
            }
            return degrees;
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }
	
	public static Bitmap decodeBitmap(File f, int reqWidth, int reqHeight) {
        try {
            Options options = new Options();
            options.inPreferredConfig = Config.RGB_565;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static void saveBitmap(Bitmap bitmap, String path) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			File file = new File(path);
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.compress(CompressFormat.JPEG, 65, baos);
			}
			bos.write(baos.toByteArray());
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

    public static Bitmap zoomImage(Bitmap srcBm, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = srcBm.getWidth();
        float height = srcBm.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(srcBm, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

	// 有些系统厂商的 ROM 会给自带相机应用做优化，当某个 app 通过 intent 进入相机拍照界面时，系统会把这个 app 当前最上层的
	// Activity 销毁回收。（注意：我遇到的情况是有时候很快就回收掉，有时候怎么等也不回收，没有什么必现规律）
	// 为了验证一下，便在启动相机的Activity 中对 onDestory 方法进行加 log 。
	// 果不其然，终于发现进入拍照界面的时候 onDestory方法被执行了。
	// 所以，前面提到的闪退基本可以推测是 Activity被回收导致某些非UI控件的成员变量为空导致的。
	// 有些机子会报出空异常错误日志，但是有些机子闪退了什么都不报，是不是觉得很奇葩！）
	//
	// 既然涉及到 Activity 被回收的问题，自然要想起 onSaveInstanceState 和 onRestoreInstanceState
	// 这对方法。去到 onSaveInstanceState 把数据保存，并在 onRestoreInstanceState
	// 方法中进行恢复即可。大体代码思路如下：
	//
	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// mRestorePhotoFile = mCapturePhotoHelper.getPhoto();
	// if (mRestorePhotoFile != null) {
	// outState.putSerializable(EXTRA_RESTORE_PHOTO, mRestorePhotoFile);
	// }
	//
	// }
	//
	// @Override
	// protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// super.onRestoreInstanceState(savedInstanceState);
	// mRestorePhotoFile = (File)
	// savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
	// mCapturePhotoHelper.setPhoto(mRestorePhotoFile);
	// }
	// 
	// 这种闪退并不能保证复现，我要怎么知道问题所在和是否修复了呢？我们可以去到开发者选项里开启不保留活动这一项进行调试验证

}
