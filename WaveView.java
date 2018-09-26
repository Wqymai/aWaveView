package com.mm.draw;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 波浪球效果
 */

public class WaveView extends View {
    Path path ;
    Paint paint;

    Path mWaveLimitPath = new Path();

    Path path1 ;
    Paint paint1;

    double φ = 400;
    float y;
    float w;
    ValueAnimator valueAnimator;

    public WaveView(Context context) {
        super(context);
        init();
    }


    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        path =  new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        path1 =  new Path();
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(getResources().getColor(R.color.colorAccent_2));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDarkWave(canvas);
        drawLightWave(canvas);

        Paint paint_circle = new Paint(paint);
        paint_circle.setColor(getResources().getColor(R.color.colorCold));
        paint_circle.setStrokeWidth(10);
        paint_circle.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth()/2,getHeight()/2,200,paint_circle);

        initAnimation();

    }

    private void drawDarkWave(Canvas canvas){
        φ -= 0.1;
        w = (float) (2 * Math.PI / getWidth());

        mWaveLimitPath.reset();
        path.reset();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setAlpha(100);
        path.moveTo(0, getHeight());
        for (float x = 0; x <= getWidth(); x += 20) {
            y = (float) (30 * Math.sin(w * x + φ ) + getHeight()/2);
            path.lineTo(x, getHeight() - y);
        }
        //向下填充
        path.lineTo(getWidth(), getHeight());
        path.close();

        mWaveLimitPath.addCircle(getWidth()/2,getHeight()/2,200, Path.Direction.CW);
        //取该圆与波浪路径的交集，形成波浪在圆内的效果
        mWaveLimitPath.op(path, Path.Op.INTERSECT);
        canvas.drawPath(mWaveLimitPath, paint);
    }

    private void drawLightWave(Canvas canvas) {
        φ -= 0.1;
        w = (float) (2 * Math.PI / getWidth());

        mWaveLimitPath.reset();
        path1.reset();
        paint.setColor(getResources().getColor(R.color.colorCold));
        paint.setAlpha(200);
        path1.moveTo(0, getHeight());
        for (float x = 0; x <= getWidth(); x += 20) {
            y = (float) (30 * Math.cos(w * x + φ ) +getHeight()/2);
            path1.lineTo(x, getHeight() - y);
        }
        path1.lineTo(getWidth(), getHeight());
        path1.close();


        mWaveLimitPath.addCircle(getWidth()/2,getHeight()/2,200, Path.Direction.CW);
        //取该圆与波浪路径的交集，形成波浪在圆内的效果
        mWaveLimitPath.op(path1, Path.Op.INTERSECT);
        canvas.drawPath(mWaveLimitPath, paint);


//        向上填充
//        path1.lineTo(getWidth(), -getHeight());
//        path1.lineTo(0, 0);


    }

    private void initAnimation() {
        valueAnimator = ValueAnimator.ofInt(0, getWidth());
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 刷新页面调取onDraw方法，通过变更φ 达到移动效果
                 */
                invalidate();
            }
        });

        valueAnimator.start();
    }




}
