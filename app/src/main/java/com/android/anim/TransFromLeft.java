package com.android.anim;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class TransFromLeft extends Animation {   
    int mCenterX;//��¼View���м�����    
    int mCenterY;    
    public TransFromLeft() {    
    }   
    
    @Override   
    public void initialize(int width, int height, int parentWidth, int parentHeight) {    
        super.initialize(width, height, parentWidth, parentHeight);    
         //��ʼ���м�����ֵ    
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
         //ͨ������任���Ѳο��㣨0,0���ƶ���View�м�    
         matrix.preTranslate(-mCenterX, -mCenterY);   
         //������ɺ����ƻ���    
         matrix.postTranslate(mCenterX, mCenterY);   
     }   
  }
