package com.fccs.library.widget;

import com.fccs.library.R;
import com.fccs.library.system.AppUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 类名：LetterSideBar<br>
 * 类描述：字母导航栏<br>
 * 创建人：倪少强<br>
 * 创建日期：2014年10月16日
 */
public class LetterSideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public static String[] letters = { "字母","定位","A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z"};
	
	private int choose = -1;// 选中
	private Paint paint = new Paint();

	private TextView mTextDialog;
	
	private Context context;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}
	
	public LetterSideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public LetterSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public LetterSideBar(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 重写这个方法
	 */
	@SuppressWarnings("deprecation")
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / letters.length;// 获取每一个字母的高度

		for (int i = 0; i < letters.length; i++) {
			paint.setColor(getResources().getColor(R.color.black_87));
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			int screenHeight = AppUtils.getScreenHeight(context);
			if(screenHeight > 1000){
				paint.setTextSize(24);
			} else if(screenHeight >= 800){
				paint.setTextSize(18);
			} else {
				paint.setTextSize(12);
			}
			// 选中的状态
			if (i == choose) {
				paint.setColor(getResources().getColor(R.color.green_500));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(letters[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(letters[i], xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * letters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundColor(0x00000000);
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.color.transparent);
//			setAlpha((float) 0.7);
			if (oldChoose != c) {
//				if (c >= 0 && c < letters.length) {
				if (c >= 2 && c < letters.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(letters[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(letters[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}