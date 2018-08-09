package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
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
    private double mItemAngle;
    private double mCurrentAngle;
    //开始角度
    private float mStartAngle;
    //扇形的半径
    private float mCircleRadius;
    private SweepGradient sweepGradient;
    private int[] colors = {0xffa98259, 0xffe9c8a5, 0xffffffff};

    public WhiteDegreeValueShowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //把画布原点移动到屏幕横向中间位置
        canvas.translate(mViewWidth/2,0);
        mCircleRadius = mViewHeight-DpUtil.dp2px(mContext,50);
        Log.d("fff", "半径: "+mCircleRadius+" 宽:"+mViewWidth+" 高:"+mViewHeight);
        final RectF mSectorRect = new RectF(-mCircleRadius, -mCircleRadius, mCircleRadius, mCircleRadius);
        float startAngle = -180;
//        mCirclePaint.setShader(sweepGradient);
        for (int i = 0;i<34;i++){
            if(i == 0){
                mCirclePaint.setColor(Color.parseColor("#a98259"));
            }else if(i == 1){
                mCirclePaint.setColor(Color.parseColor("#a98259"));
            }else if(i == 2){
                mCirclePaint.setColor(Color.parseColor("#ae8b61"));
            }else if(i == 3){
                mCirclePaint.setColor(Color.parseColor("#ae8b61"));
            }else if(i == 4){
                mCirclePaint.setColor(Color.parseColor("#ae8b61"));
            }else if(i == 5){
                mCirclePaint.setColor(Color.parseColor("#be9d74"));
            }else if(i == 6){
                mCirclePaint.setColor(Color.parseColor("#be9d74"));
            }else if(i == 7){
                mCirclePaint.setColor(Color.parseColor("#be9d74"));
            }else if(i == 8){
                mCirclePaint.setColor(Color.parseColor("#c8a77e"));
            }else if(i == 9){
                mCirclePaint.setColor(Color.parseColor("#c8a77e"));
            }else if(i == 10){
                mCirclePaint.setColor(Color.parseColor("#d0b087"));
            }else if(i == 11){
                mCirclePaint.setColor(Color.parseColor("#d0b087"));
            }else if(i == 12){
                mCirclePaint.setColor(Color.parseColor("#daba93"));
            }else if(i == 13){
                mCirclePaint.setColor(Color.parseColor("#daba93"));
            }else if(i == 14){
                mCirclePaint.setColor(Color.parseColor("#e5c59e"));
            }else if(i == 15){
                mCirclePaint.setColor(Color.parseColor("#e5c59e"));
            }else if(i == 16){
                mCirclePaint.setColor(Color.parseColor("#e9c8a5"));
            }else if(i == 17){
                mCirclePaint.setColor(Color.parseColor("#e9c8a5"));
            }else if(i == 18){
                mCirclePaint.setColor(Color.parseColor("#e9d0b1"));
            }else if(i == 19){
                mCirclePaint.setColor(Color.parseColor("#e9d0b1"));
            }else if(i == 20){
                mCirclePaint.setColor(Color.parseColor("#ead4ba"));
            }else if(i == 21){
                mCirclePaint.setColor(Color.parseColor("#ead4ba"));
            }
            canvas.drawArc(mSectorRect, startAngle, (float) mItemAngle, false, mCirclePaint);
            startAngle += mItemAngle;
        }
//        canvas.drawCircle(0,0,DpUtil.dp2px(mContext,3),mCirclePaint);
    }


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        mStartAngle = 270;
        mItemAngle = 360 * 1.0 / mArcNum;
        mCircleCenterY = DpUtil.dp2px(mContext,-5);
        mRectFWidth = DpUtil.dp2px(mContext, mRectFWidth);
        mCircleRadius = mViewHeight;
        sweepGradient = new SweepGradient(0, 0, colors, null);
        sweepGradientInit();

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mCirclePaint.setStrokeWidth(DpUtil.dp2px(mContext,100));


    }

    /**
     * 渐变初始化
     */
    public void sweepGradientInit() {
        //渐变颜色
        sweepGradient = new SweepGradient(this.mViewWidth / 2, this.mViewWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.mViewWidth / 2, this.mViewWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
    }


}
