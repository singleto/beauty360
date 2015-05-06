package com.android.picview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

// ����һ���Լ���ImageView��
public class MyImageView extends ImageView {
	private float scale = 0.03f;
	// ���㴥����֮��ĳ���
	private float beforeLenght;
	private float afterLenght;

	// �����ƶ���ǰ������ֵ
	private float afterX, afterY;
	private float beforeX, beforeY;

	public MyImageView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	// ��������ImageView��λ��
	private void setLocation(int x, int y) {
		this.setFrame(this.getLeft() + x, this.getTop() + y, this.getRight()
				+ x, this.getBottom() + y);
	}

	/*
	 * �����Ŵ���СImageView ��ΪͼƬ�����ImageView�ģ�����Ҳ���зŴ���СͼƬ��Ч�� flagΪ0�ǷŴ�ͼƬ��Ϊ1��С��ͼƬ
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

	// ���Ʊ߿�
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	/*
	 * ��ͼƬ������ָ������λ���ƶ� beforeX��Y����������ǰһλ�õ����� afterX��Y���������浱ǰλ�õ�����
	 * ���ǵĲ�ֵ����ImageView����������ӻ����ֵ
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
	 * ͨ����㴥���Ŵ����Сͼ�� beforeLenght��������ǰһʱ������֮��ľ��� afterLenght�������浱ǰʱ������֮��ľ���
	 */
	public void scaleWithFinger(MotionEvent event) {
		float moveX = event.getX(1) - event.getX(0);
		float moveY = event.getY(1) - event.getY(0);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			beforeLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
			break;
		case MotionEvent.ACTION_MOVE:
			// �õ�������֮��ĳ���
			afterLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));

			float gapLenght = afterLenght - beforeLenght;

			if (gapLenght == 0) {
				break;
			}

			// �����ǰʱ������������ǰһʱ��������룬��0������1
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
