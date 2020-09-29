package com.zd.collectlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.zd.collectlibrary.R;


/**
 * Package: com.wzyk.viewmodule.view
 * <p>
 * describe: 变换的image  xml可设置属性
 * <p>
 * <!--变化方式  圆角类型  圆形类型-->
 * *****<attr name="TransformType" format="enum">
 * ********<enum name="roundCorner" value="0" />
 * ********<enum name="circle" value="1" />
 * *****</attr>
 * <!--圆角类型的时候设置各圆角的半径大小 !如果设置了allCorner那么会覆盖掉其他单独设置的角-->
 * *****<attr name="allCorner" format="dimension" />
 * *****<attr name="leftTopCorner" format="dimension" />
 * *****<attr name="leftBottomCorner" format="dimension" />
 * *****<attr name="rightTopCorner" format="dimension" />
 * *****<attr name="rightBottomCorner" format="dimension" />
 *
 * @author zhangdong on 2020/9/29
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class TransformImageView extends AppCompatImageView {

    private static final String TAG = ">>>>> Transform";
    private static final int TYPE_ROUND = 0;
    private static final int TYPE_CIRCLE = 1;

    private Paint mPaint;
    private Path clipPath;          //绘制需要内容的路径

    private int leftTopR = 0;       //左上角圆角半径
    private int rightTopR = 0;      //右上角圆角半径
    private int leftBottomR = 0;    //左下角圆角半径
    private int rightBottomR = 0;   //右下角圆角半径

    private int allR = 0;           //四角半径
    private int transformType = TYPE_ROUND;//展示方式，默认圆角类型

    public TransformImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public void setAllCornerPx(int allR) {
        this.allR = allR;
        this.leftTopR = allR;
        this.leftBottomR = allR;
        this.rightTopR = allR;
        this.rightBottomR = allR;
        invalidate();
    }

    /**
     * 设置四角半径
     *
     * @param cornerPx 最多传4个参数 依次 左上 右上 右下 左下
     */
    public void setRoundCornerPx(int... cornerPx) {
        //设置圆角时 样式圆角
        transformType = TYPE_ROUND;
        for (int i = 0; i < cornerPx.length; i++) {
            if (i == 0) {
                leftTopR = cornerPx[0];
            } else if (i == 1) {
                rightTopR = cornerPx[1];
            } else if (i == 2) {
                rightBottomR = cornerPx[2];
            } else if (i == 3) {
                leftBottomR = cornerPx[3];
            } else {
                break;
            }
        }
        invalidate();
    }

    public TransformImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TransformImageView, defStyleAttr, 0);
        allR = (int) typedArray.getDimension(R.styleable.TransformImageView_allCorner, -1);
        if (allR != -1) {//如果设置了allCorner那么会覆盖掉其他单独设置的角
            leftTopR = allR;
            leftBottomR = allR;
            rightTopR = allR;
            rightBottomR = allR;
        } else {
            leftTopR = (int) typedArray.getDimension(R.styleable.TransformImageView_leftTopCorner, 0);
            leftBottomR = (int) typedArray.getDimension(R.styleable.TransformImageView_leftBottomCorner, 0);
            rightTopR = (int) typedArray.getDimension(R.styleable.TransformImageView_rightTopCorner, 0);
            rightBottomR = (int) typedArray.getDimension(R.styleable.TransformImageView_rightBottomCorner, 0);
        }
        transformType = typedArray.getInteger(R.styleable.TransformImageView_TransformType, TYPE_ROUND);
        typedArray.recycle();
        init();
    }

    private void init() {
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        //设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        if (clipPath == null)
            clipPath = new Path();
        clipPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //view的大小确定之后，确定裁剪
        clipPath.reset();
        //考虑padding
        if (transformType == TYPE_ROUND) {
            //圆角
            //圆角的最大值为最短内容边长的一半
            int maxRoundCorner = Math.min(
                    w - getPaddingStart() - getPaddingEnd(),
                    h - getPaddingTop() - getPaddingBottom()) >> 1;

            leftTopR = Math.min(leftTopR, maxRoundCorner);
            rightTopR = Math.min(rightTopR, maxRoundCorner);
            leftBottomR = Math.min(leftBottomR, maxRoundCorner);
            rightBottomR = Math.min(rightBottomR, maxRoundCorner);

            clipPath.moveTo(getPaddingStart(), leftTopR + getPaddingTop());
            //左上角
            clipPath.arcTo(getPaddingStart(), getPaddingTop(),
                    getPaddingStart() + leftTopR * 2,
                    getPaddingTop() + leftTopR * 2,
                    180, 90, false);

            clipPath.lineTo(w - rightTopR - getPaddingEnd(), getPaddingTop());
            //右上角
            clipPath.arcTo(w - rightTopR * 2 - getPaddingEnd(), getPaddingTop(),
                    w - getPaddingEnd(), getPaddingTop() + rightTopR * 2,
                    270, 90, false);

            clipPath.lineTo(w - getPaddingEnd(), h - rightBottomR - getPaddingBottom());
            //右下角
            clipPath.arcTo(w - rightBottomR * 2 - getPaddingEnd(),
                    h - rightBottomR * 2 - getPaddingEnd(),
                    w - getPaddingEnd(), h - getPaddingBottom(),
                    0, 90, false);

            clipPath.lineTo(leftBottomR + getPaddingStart(), h - getPaddingBottom());
            //左下角
            clipPath.arcTo(getPaddingStart(), h - leftBottomR * 2 - getPaddingBottom(),
                    leftBottomR * 2 + getPaddingStart(), h - getPaddingBottom(),
                    90, 90, false);

            clipPath.lineTo(getPaddingStart(), leftTopR + getPaddingTop());

        } else if (transformType == TYPE_CIRCLE) {
            //圆形
            clipPath.addCircle(getPaddingStart() + ((w - getPaddingStart() - getPaddingEnd()) >> 1),
                    getPaddingTop() + ((h - getPaddingTop() - getPaddingBottom()) >> 1),
                    Math.min(w - getPaddingStart() - getPaddingEnd(),
                            h - getPaddingTop() - getPaddingBottom()) >> 1, Path.Direction.CCW);
        } else {
            //默认
            clipPath.addRect(0, 0, w, h, Path.Direction.CCW);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(clipPath, mPaint);
    }
}
