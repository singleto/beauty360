package com.android.picview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

// 创建一个自己的ImageView类
public class MyImageView extends ImageView {
	private float scale = 0.03f;
	// 两点触屏后之间的长度
	private float beforeLenght;
	private float afterLenght;

	// 单点移动的前后坐标值
	private float afterX, afterY;
	private float beforeX, beforeY;

	public MyImageView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	// 用来设置ImageView的位置
	private void setLocation(int x, int y) {
		this.setFrame(this.getLeft() + x, this.getTop() + y, this.getRight()
				+ x, this.getBottom() + y);
	}

	/*
	 * 用来放大缩小ImageView 因为图片是填充ImageView的，所以也就有放大缩小图片的效果 flag为0是放大图片，为1是小于图片
	 */
	private void setScale(float temp, int flag) {

		if (flag == 0) {
			this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
					this.getTop() - (int) (temp * this.getHeight()),
					this.getRight() + (int) (temp * this.getWidth()),
					this.getBottom() + (int) (temp * this.getHeight()));
		} else {
			this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
					this.getTop() + (int) (temp * this.getHeight()),
					this.getRight() - (int) (temp * this.getWidth()),
					this.getBottom() - (int) (temp * this.getHeight()));
		}
	}

	// 绘制边框
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	/*
	 * 让图片跟随手指触屏的位置移动 beforeX、Y是用来保存前一位置的坐标 afterX、Y是用来保存当前位置的坐标
	 * 它们的差值就是ImageView各坐标的增加或减少值
	 */
	public void moveWithFinger(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			beforeX = event.getX();
			beforeY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			afterX = event.getX();
			afterY = event.getY();
			this.setLocation((int) (afterX - beforeX), (int) (afterY - beforeY));
			beforeX = afterX;
			beforeY = afterY;
			break;

		case MotionEvent.ACTION_UP:
			break;
		}
	}

	/*
	 * 通过多点触屏放大或缩小图像 beforeLenght用来保存前一时间两点之间的距离 afterLenght用来保存当前时间两点之间的距离
	 */
	public void scaleWithFinger(MotionEvent event) {
		float moveX = event.getX(1) - event.getX(0);
		float moveY = event.getY(1) - event.getY(0);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			beforeLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
			break;
		case MotionEvent.ACTION_MOVE:
			// 得到两个点之间的长度
			afterLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));

			float gapLenght = afterLenght - beforeLenght;

			if (gapLenght == 0) {
				break;
			}

			// 如果当前时间两点距离大于前一时间两点距离，则传0，否则传1
			if (gapLenght > 0) {
				this.setScale(scale, 0);
			} else {
				this.setScale(scale, 1);
			}

			beforeLenght = afterLenght;
			break;
		}
	}

}
