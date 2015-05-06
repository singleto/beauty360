package com.android.anim;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class TransFromLeft extends Animation {   
    int mCenterX;//记录View的中间坐标    
    int mCenterY;    
    public TransFromLeft() {    
    }   
    
    @Override   
    public void initialize(int width, int height, int parentWidth, int parentHeight) {    
        super.initialize(width, height, parentWidth, parentHeight);    
         //初始化中间坐标值    
         mCenterX = -100;     
         mCenterY = height/2;    
         setDuration(500);    
         setFillAfter(true);    
         setInterpolator(new LinearInterpolator());    
     }   
  
     @Override   
     protected void applyTransformation(float interpolatedTime,    
            Transformation t) {   
         final Matrix matrix = t.getMatrix();    
         matrix.setScale(interpolatedTime, interpolatedTime);   
         //通过坐标变换，把参考点（0,0）移动到View中间    
         matrix.preTranslate(-mCenterX, -mCenterY);   
         //动画完成后再移回来    
         matrix.postTranslate(mCenterX, mCenterY);   
     }   
  }
