package com.zd.collectlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.zd.collectlibrary.R;


/**
 * Package: com.example.zd.baseapi.view
 * <p>
 * describe: 有简单背景图案的textView
 * 简单的背景图可以用这个，省去自定义各种shape资源
 *
 * @author zhangdong on 2019/10/18 0018.
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class ShapeBgTextView extends AppCompatTextView {

    //注意统一尺寸单位 获取到的都是px值  所以设置的都转成px使用

    private Paint mPaint;
    private RectF rectF;
    private int drawShapeColor;     //周围一圈线颜色
    private int lineWidth;          //周围线宽度 单位转换成px
    private int corner;             //圆角大小 单位转换成px
    private int shapeStyle;         //图形样式 0-画线  !=0 填充整个view
    private boolean haveBg;         //是否画背景

    public ShapeBgTextView(Context context) {
        this(context, null);
    }

    public ShapeBgTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeBgTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeBgTextView);
        //是否有背景线
        haveBg = typedArray.getBoolean(R.styleable.ShapeBgTextView_showBgShape, false);
        //线颜色
        drawShapeColor = typedArray.getColor(R.styleable.ShapeBgTextView_shapeColor, Color.GRAY);
        //线宽度 dp 转换成px
        lineWidth = (int) typedArray.getDimension(R.styleable.ShapeBgTextView_shapeBgLineWidth, 0);
        //圆角大小 dp 转换成px
        corner = (int) typedArray.getDimension(R.styleable.ShapeBgTextView_shapeCornerSize, 0);
        //画的样式
        shapeStyle = typedArray.getInt(R.styleable.ShapeBgTextView_shapeStyle, 0);

        typedArray.recycle();

        //初始化画笔
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        if (shapeStyle == 0) {
            //样式空心画线
            mPaint.setStyle(Paint.Style.STROKE);
        } else {
            //填充模式画形状
            mPaint.setStyle(Paint.Style.FILL);
        }
        //抗锯齿
        mPaint.setAntiAlias(true);
        //线条首位样式 平头这样转弯比较圆滑
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        //画笔颜色
        mPaint.setColor(drawShapeColor);
        //画笔宽度
        mPaint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //view的最短边
        int minW = Math.min(w, h);

        //画线的宽度都大于view的短边的一半了 一来一回画出来的线都占满了view
        //那么强制线宽就是短边的一半减一点 不然临界值画不出来
        //即 0 < lineWidth < (minW)/2
        if (lineWidth >= (minW >> 1)) {
            lineWidth = (minW >> 1);
            //重置画笔宽度
            mPaint.setStrokeWidth(lineWidth);
            //此时要画的矩形短边近似为lineWidth
            if (corner > (lineWidth >> 1)) {
                corner = (lineWidth >> 1);
            }
        } else {
            //画笔宽度没有超限
            //检查圆角是否超过当前允许的最大值
            if (corner > ((minW - lineWidth) >> 1)) {
                corner = ((minW - lineWidth) >> 1);
            }
        }

        if (corner <= 0)
            corner = 0;

        //view相似的矩形 这个就行其实是减去了线绘制宽度的内部矩形
//              _________________
//              |  ___________  |
//              |  |         |  |
//              |  |         |  |
//              |  |_________|  |
//              |_______________|
        //画线的矩形
        if (shapeStyle == 0)
            rectF = new RectF(lineWidth >> 1, lineWidth >> 1,
                    w - (lineWidth >> 1), h - (lineWidth >> 1));
        else
            //填充模式的矩形
            rectF = new RectF(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制我们自己要的背景
        drawLineRect(canvas);

        //textView自己的绘制
        super.onDraw(canvas);
    }

    /**
     * 画外圈的线
     *
     * @param canvas .
     */
    private void drawLineRect(Canvas canvas) {
        //画形
        if (haveBg) {
            //清除view原有背景
            setBackgroundResource(0);

            if (corner == 0) {
                //圆角为0dp 则直接画矩形
                canvas.drawRoundRect(rectF, 0, 0, mPaint);
                //补四个角的点  不然画笔线太粗的时候四个角是空白的
                canvas.drawPoint(rectF.left, rectF.bottom, mPaint);
                canvas.drawPoint(rectF.left, rectF.top, mPaint);
                canvas.drawPoint(rectF.right, rectF.top, mPaint);
                canvas.drawPoint(rectF.right, rectF.bottom, mPaint);
            } else {
                //画圆角矩形
                canvas.drawRoundRect(rectF,
                        corner + (lineWidth >> 1),
                        corner + (lineWidth >> 1),
                        mPaint);
            }
        }
    }

    private int dp2Px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }
}
