package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name TestProject
 * @page com.example.administrator.testview
 * @class describe
 * @date 2018/8/10 10:59
 */
public class CircleAndLineView extends View{

    private Context mContext;
    private Paint mSmailCirclePaint;
    private float mViewHeight;
    private float mCircleRadius;
    private float mCenterCircleRadius = 15;
    private float mLineMargin = 10;
    private float mViewWidth;

    public CircleAndLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth/2,0);
        mCircleRadius = mViewHeight;
        canvas.drawCircle(0,0,mCenterCircleRadius,mSmailCirclePaint);
        RectF rectF = new RectF();
        rectF.set(-DpUtil.dp2px(mContext,2),mCenterCircleRadius-1,DpUtil.dp2px(mContext,2),
                mCircleRadius - DpUtil.dp2px(mContext,50));
        canvas.drawRect(rectF,mSmailCirclePaint);
    }

    private void init(Context context){
        this.mContext = context;
        mLineMargin = DpUtil.dp2px(mContext,mLineMargin);
        mCenterCircleRadius = DpUtil.dp2px(mContext,mCenterCircleRadius);
        mCircleRadius = mViewHeight;


        mSmailCirclePaint = new Paint();
        mSmailCirclePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mSmailCirclePaint.setAntiAlias(true);
        mSmailCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

}
