package com.zd.collectlibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

import com.zd.collectlibrary.BuildConfig;

/**
 * describe: 背景跑一圈的textView  设置textView的宽高需要宽大于高
 *
 * @author zhangdong on 2019/10/17 0017.
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class RunBgTextView extends AppCompatTextView {

    private Paint mPaint;                   //画笔
    private Path allPath, drawPath;         //全路径，//每次绘制的路径  (这里的路径画的是左右半圆 中间横线的路径)
    private PathMeasure pathMeasure;        //动态绘制过它来测量每次绘制的路径
    private boolean startRun = false;       //是否开始画跑
    private boolean isKeep = false;         //画完之后路线是否保持住
    private int vW, vH;                     //控件的宽高
    private float mAnimatorValue;           //动画执行的当前进度
    private float length;                   //路径总长度
    private ValueAnimator valueAnimator;    //动画
    private int runColor = Color.RED;       //周围一圈的颜色 -- default red
    private int runLineWidth = 1;           //环绕的线宽度 -- default 1dp
    private long drawDuration = 2000L;      //绘制一次的时间 -- default 2000毫秒

    public RunBgTextView(Context context) {
        super(context);
    }

    public RunBgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RunBgTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取是否画过跑路了
     *
     * @return .
     */
    public boolean isStartRun() {
        return startRun;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //执行一遍，获取到view的宽高保存，用来计算绘制路径
        //设置textView的宽高需要宽大于高 保证是长方形的view w>H
        vW = w;
        vH = h;
        if (startRun)
            startRunBg();
    }

    //开始动态绘制前景
    public void startRunBg() {
        startRunBg(runColor);
    }

    //开始动态绘制前景 传颜色
    public void startRunBg(int color) {
        startRunBg(color, runLineWidth);
    }

    //开始动态绘制前景 传颜色宽度
    public void startRunBg(int color, int bgWidthDp) {
        //设置初始属性
        startRunBg(color, bgWidthDp, drawDuration);
    }

    // 开始动态绘制前景 传颜色宽度绘制时间
    public void startRunBg(int color, int bgWidthDp, long duration) {
        startRunBg(color, bgWidthDp, duration, isKeep);
    }

    /**
     * 开始动态绘制前景 传颜色宽度绘制时间,最终是否保留最后路径
     *
     * @param color     绘制的颜色
     * @param bgWidthDp 绘制的宽度
     * @param duration  绘制一次的时间
     * @param finalKeep 是否保留最后路径
     */
    public void startRunBg(int color, int bgWidthDp, long duration, boolean finalKeep) {
        //设置初始属性
        this.startRun = true;
        if (color != 0)
            this.runColor = color;
        if (bgWidthDp > 0)
            this.runLineWidth = bgWidthDp;
        if (duration > 0)
            this.drawDuration = duration;
        this.isKeep = finalKeep;

        //此时view的宽高还不确定，在onSizeChange()中再判断 startRun执行
        if (vW == 0 || vH == 0)
            return;
        //设置画笔
        setPaint();
        //计算绘制路径
        if (null == drawPath || null == pathMeasure)
            initPath();
        //开启动画绘制
        startRunAnimation();
    }

    /**
     * 设置绘制的画笔
     */
    private void setPaint() {
        if (null == mPaint) {
            //画笔
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setColor(runColor);
        mPaint.setStrokeWidth(dp2Px(runLineWidth));
    }

    /**
     * 初始化路径等参数
     */
//    @SuppressLint("NewApi")
    private void initPath() {
        //路径
        allPath = new Path();
        //划线的宽度取一半宽度便宜，不然画不全
        int pintW = dp2Px(runLineWidth) >> 1;
        //左边半圆  最后一个参数一定要false不然路径就断了
        RectF leftRectF = new RectF(pintW, pintW, vH - pintW, vH - pintW);
        allPath.arcTo(leftRectF, 90, 180, false);
        //顶部横线
        allPath.lineTo(vW - (vH >> 1), pintW);
        //右边半圆  最后一个参数一定要false不然路径就断了
        RectF rightRectF = new RectF(vW - vH + pintW, pintW, vW - pintW, vH - pintW);
        allPath.arcTo(rightRectF, -90, 180, false);
        //下边横线
        allPath.lineTo(vH >> 1, vH - pintW);

        //测量整个路径的 用它来根据动画进度截取要绘制的路径
        pathMeasure = new PathMeasure(allPath, false);
        //全路径的长度
        length = pathMeasure.getLength();

        //要绘制的路径  用来保存每次pathMeasure测量出来的绘制路径
        drawPath = new Path();

        if (BuildConfig.DEBUG)
            Log.d(">>>>>", "path length: " + length);
    }

    /**
     * 开启路径的绘制动画
     */
    private void startRunAnimation() {
        //初始化动画
        if (null == valueAnimator) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //动画的值，通过这个值来计算绘制的路径长度
                    mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                    if (mAnimatorValue == 1) {
                        //动画完成(是否保持住路线)
                        startRun = isKeep;
                    }
                    //重绘
                    invalidate();
                }
            });
            //动画执行时间
            valueAnimator.setDuration(drawDuration);
        }
        //动画在执行，则返回
        if (valueAnimator.isRunning())
            return;
        //开始动画
        valueAnimator.start();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        // FIXME: 2019/10/18 0018 亮哥模拟器和手机都不走这个方法，因此将画跑路的方法放到onDraw()的super之后执行
        //此方法在onDraw()之后执行，绘制view前景，这里绘制跑的一圈bg
//        if (startRun) {
//            drawPath.reset();
//            drawPath.lineTo(0, 0);
//            //计算绘制的长度
//            float stop = length * mAnimatorValue;
//            pathMeasure.getSegment(0, stop, drawPath, true);
//            canvas.drawPath(drawPath, mPaint);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //此方法在onDraw()之后执行，绘制view前景，这里绘制跑的一圈bg
        if (startRun) {
            drawPath.reset();
            drawPath.lineTo(0, 0);
            //计算绘制的长度
            float stop = length * mAnimatorValue;
            pathMeasure.getSegment(0, stop, drawPath, true);
            canvas.drawPath(drawPath, mPaint);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            //关闭动画资源
            if (valueAnimator != null) {
                valueAnimator.removeAllUpdateListeners();
                valueAnimator = null;
                mPaint = null;
                allPath.reset();
                drawPath.reset();
                pathMeasure = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDetachedFromWindow();
    }

    private int dp2Px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }
}
