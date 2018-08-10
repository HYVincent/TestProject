package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name GoldenRiceWhiteDegree
 * @page com.shenmou.goldenricewhitedegree.view
 * @class describe 显示白皙度数值转盘
 * @date 2018/8/6 14:29
 */
public class WhiteDegreeValueShowView extends View {

    private Context mContext;
    private float mViewWidth;
    private float mViewHeight;
    private Paint mCirclePaint;
    private float mRectFWidth = 4;
    //扇形圆的X坐标
    private float mCircleCenterX = 0;
    //扇形圆的Y坐标
    private float mCircleCenterY;
    //扇形的个数
    private float mArcNum = 34;
    //每一份扇形的角度 一共有34小份
    private float mItemAngle;
    private double mCurrentAngle;
    //开始角度
    private float mStartAngle;
    //扇形的半径
    private float mCircleRadius;
    private int[] colors;
    private Paint mLinePaint;
    //短的线段的长度
    private float mShortLineLength = 15;
    //线段到圆边线的距离
    private float mLineMargin = 10;
    private Paint mNumberPaint;
    private Rect rect;
    private RectF mSectorRect;
    private int[] numbs;
    private Matrix matrix;

    public WhiteDegreeValueShowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h*2;
        mBitmap = Bitmap.createBitmap((int) mViewWidth,(int) mViewHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(mBitmap);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private Bitmap mBitmap;
    private Canvas canvas;

    @Override
    protected void onDraw(Canvas rootCanvas) {
        canvas.translate(mViewWidth / 2, mViewHeight/2);
        canvas.drawCircle(0, 0,DpUtil.dp2px(mContext,3),mCirclePaint);
        mCircleRadius = mViewHeight/2;
        mSectorRect.set(-mCircleRadius, -mCircleRadius, mCircleRadius, mCircleRadius);
        mStartAngle = 90;
        for (int i = 0; i < 34; i++) {
            mStartAngle = mStartAngle - mItemAngle;
            mCirclePaint.setColor(colors[i]);
            canvas.drawArc(mSectorRect, mStartAngle, mItemAngle, true, mCirclePaint);
        }
        canvas.save();
        canvas.restore();
        mStartAngle = 0;
        for (int i = 0; i < 34; i++) {
            if (i == 33) {
                Log.d("旋转", "onDraw: 角度--》" + mStartAngle);
            }
            mStartAngle = mStartAngle - mItemAngle;
            mCirclePaint.setColor(colors[i]);
            canvas.drawArc(mSectorRect, mStartAngle, mItemAngle, false, mCirclePaint);
            //绘制线段
            canvas.save();
            canvas.rotate(mStartAngle, 0, 0);
            if (i < 18) {
                mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_white_ffffff));
            } else {
                mLinePaint.setColor(colors[0]);
            }
            if (i % 2 != 0) {
                float mLineEndY = mCircleRadius - mLineMargin - mShortLineLength * 2;
                //绘制文字
                String tegStr = String.valueOf(numbs[i / 2]);
                mNumberPaint.getTextBounds(tegStr, 0, tegStr.length(), rect);
                //绘制
                canvas.drawText(tegStr, 0, mLineEndY - rect.height() - DpUtil.dp2px(mContext, 10), mNumberPaint);
                //长线段
                canvas.drawLine(0, mCircleRadius - mLineMargin, 0,
                        mLineEndY, mLinePaint);
            } else {
                //短线段
                canvas.drawLine(0, mCircleRadius - (mShortLineLength * 2 + mLineMargin - mShortLineLength) / 2, 0,
                        mCircleRadius - (mShortLineLength * 2 + mLineMargin - mShortLineLength) / 2 - mShortLineLength, mLinePaint);
            }
            canvas.restore();
        }
        mCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.color_white_ffffff));
        canvas.drawCircle(0, 0, mCircleRadius / 2, mCirclePaint);
        canvas.save();
        canvas.restore();
        matrix.preTranslate(0,-mCircleRadius);
//        canvas.save();
//        canvas.restore();
        matrix.postRotate(angle,mViewWidth/2,mCircleRadius);
        rootCanvas.drawBitmap(mBitmap,matrix,mCirclePaint);
    }

    private float angle = 0;

    public void setAngle(float angle){
        this.angle = angle;
        invalidate();
    }


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        rect = new Rect();
        mSectorRect = new RectF();
        matrix = new Matrix();
        mShortLineLength = DpUtil.dp2px(mContext, mShortLineLength);
        mLineMargin = DpUtil.dp2px(mContext, mLineMargin);
        mItemAngle = 360 * 1.0f / mArcNum;
        mCircleCenterY = DpUtil.dp2px(mContext, -5);
        mRectFWidth = DpUtil.dp2px(mContext, mRectFWidth);
        mCircleRadius = mViewHeight;


        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
//        mCirclePaint.setStrokeWidth(DpUtil.dp2px(mContext,100));

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_white_ffffff));
        mLinePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        mLinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setTextSize(DpUtil.dp2px(mContext, 14));
        mNumberPaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
        mNumberPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));


        colors = new int[34];
        numbs = new int[17];
        initColors(colors);
        initNumbers(numbs);
    }

    private void initNumbers(int[] numbs) {
        numbs[0] = 24;
        for (int i = 1; i < 16; i++) {
            numbs[i] = 24 + i * 4;
        }
        numbs[16] = 20;
        /*for (int i =0;i<17;i++){
            numbs[i] = 20 +i * 4;
        }*/
    }

    private void initColors(int[] colors) {
        colors[0] = Color.parseColor("#a98259");
        colors[1] = Color.parseColor("#a98259");

        colors[2] = Color.parseColor("#ae8b61");
        colors[3] = Color.parseColor("#ae8b61");

        colors[4] = Color.parseColor("#b69369");
        colors[5] = Color.parseColor("#b69369");

        colors[6] = Color.parseColor("#be9d74");
        colors[7] = Color.parseColor("#be9d74");

        colors[8] = Color.parseColor("#c8a77e");
        colors[9] = Color.parseColor("#c8a77e");

        colors[10] = Color.parseColor("#d0b087");
        colors[11] = Color.parseColor("#d0b087");

        colors[12] = Color.parseColor("#daba93");
        colors[13] = Color.parseColor("#daba93");


        colors[14] = Color.parseColor("#e5c59e");
        colors[15] = Color.parseColor("#e5c59e");


        colors[16] = Color.parseColor("#e9c8a5");
        colors[17] = Color.parseColor("#e9c8a5");


        colors[18] = Color.parseColor("#e9d0b1");
        colors[19] = Color.parseColor("#e9d0b1");


        colors[20] = Color.parseColor("#ead4ba");
        colors[21] = Color.parseColor("#ead4ba");


        colors[22] = Color.parseColor("#ebd8ba");
        colors[23] = Color.parseColor("#ebd8ba");

        colors[24] = Color.parseColor("#eddbc3");
        colors[25] = Color.parseColor("#eddbc3");

        colors[26] = Color.parseColor("#f3e2d0");
        colors[27] = Color.parseColor("#f3e2d0");

        colors[28] = Color.parseColor("#f5e8d8");
        colors[29] = Color.parseColor("#f5e8d8");

        colors[30] = Color.parseColor("#faf0e4");
        colors[31] = Color.parseColor("#faf0e4");

        colors[32] = Color.parseColor("#fff6ed");
        colors[33] = Color.parseColor("#fff6ed");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("liuyz-", "onMeasure");

        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == MeasureSpec.AT_MOST
                && measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mViewWidth, (int) mViewHeight);
        } else if (measureWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mViewWidth, measureHeightSize);
        } else if (measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidthSize, (int) mViewHeight);
        }
    }

}
