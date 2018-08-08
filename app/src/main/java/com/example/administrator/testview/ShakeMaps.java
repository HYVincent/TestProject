package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import java.util.ArrayList;
import java.util.List;
/**
 * 1000个点,震荡图
 * Created by GreendaMi on 2017/5/8.
 */
public class ShakeMaps extends View {
    Context mContext;
    int max;
    int min;
    //两种线的颜色
    int mColor1 = 0xff159461;
    int mColor2 = 0xffeb2e28;
    Paint mPaint;
    int gap = 10;//点与点之间的间距
    int startX = 10;
    int borderTopAndBottom = 20;//上下留白
    int botderLeft = 10;//左边留白
    int botderLefttep = botderLeft;
    int lastStartX = startX;//抬起手指后,当前控件最左边X的坐标
    int mXDown;
    int mLastX;
    //最短滑动距离
    int a = 0;
    public void setmData(List<dataObj> mData, int max, int min) {
        this.mData = mData;
        this.max = max;
        this.min = min;
        postInvalidate();
    }
    public void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
        } else {
            mPaint.reset();
        }
        mPaint.setAntiAlias(true);
//文字大小
        mPaint.setTextSize(getWidth() / 32);
    }
    List<dataObj> mData = new ArrayList<>();
    public ShakeMaps(Context context) {
        super(context);
        mContext = context;
        a = DpUtil.px2dp(context, ViewConfiguration.get(context).getScaledDoubleTapSlop());
        setClickable(true);
        initializeTheUnit();
        initPaint();
    }
    public ShakeMaps(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        a = DpUtil.px2dp(context, ViewConfiguration.get(context).getScaledDoubleTapSlop());
        setClickable(true);
        initializeTheUnit();
        initPaint();
    }
    //初单位
    public void initializeTheUnit() {
        gap = DpUtil.px2dp(mContext, 5);
        startX = DpUtil.px2dp(mContext, 5);
        borderTopAndBottom = DpUtil.px2dp(mContext, 10);
        botderLeft = DpUtil.px2dp(mContext, 10);
        botderLefttep = botderLeft;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//画背景
        drawTheBackground(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
//画y轴
        drawTheY(canvas);
//画虚线,关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
//画x轴横线
        drawTheX(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
//画数据
        drawDatas(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }
    private void drawTheY(Canvas canvas) {
        initPaint();
        mPaint.setColor(0xff92dac4);
        mPaint.setStrokeWidth(DpUtil.px2dp(mContext, 1));
//留出文字距离
        botderLeft = botderLefttep + (int) (mPaint.measureText(min + "") * 1.2f);
//画出纵坐标线
        canvas.drawLine(botderLeft, borderTopAndBottom, botderLeft, getHeight() - borderTopAndBottom, mPaint);
    }
    private void drawDatas(Canvas canvas) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        initPaint();
        mPaint.setColor(mColor1);
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
//画y1的线
        for (int i = 0; i < mData.size() - 1; i++) {
//超过屏幕范围,不再绘制
            if (startX + botderLeft + gap * i < botderLeft) {
                continue;
            }
            if (startX + botderLeft + gap * (i + 1) > getWidth()) {
                break;
            }
            canvas.drawLine(startX + botderLeft + gap * i, getHByValue(mData.get(i).y1), startX + botderLeft + gap * (i + 1), getHByValue(mData.get(i + 1).y1), mPaint);
//画开始小球和结束小球
            if (i == 0) {
                canvas.drawCircle(startX + botderLeft + gap * i, getHByValue(mData.get(i).y1), 8, mPaint);
                mPaint.setColor(0xffffffff);
                canvas.drawCircle(startX + botderLeft + gap * i, getHByValue(mData.get(i).y1), 4, mPaint);
                mPaint.setColor(mColor1);
            }
            if (i == mData.size() - 2) {
                canvas.drawCircle(startX + botderLeft + gap * (i + 1), getHByValue(mData.get(i + 1).y1), 8, mPaint);
                mPaint.setColor(0xffffffff);
                canvas.drawCircle(startX + botderLeft + gap * (i + 1), getHByValue(mData.get(i + 1).y1), 4, mPaint);
                mPaint.setColor(mColor1);
            }
        }
//画y2的线
        initPaint();
        mPaint.setColor(mColor2);
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        for (int i = 0; i < mData.size() - 1; i++) {
//超过屏幕范围,不再绘制
            if (startX + botderLeft + gap * i < botderLeft) {
                continue;
            }
            if (startX + botderLeft + gap * (i + 1) > getWidth()) {
                break;
            }
            canvas.drawLine(startX + botderLeft + gap * i, getHByValue(mData.get(i).y2), startX + botderLeft + gap * (i + 1), getHByValue(mData.get(i + 1).y2), mPaint);
//画开始小球和结束小球
            if (i == 0) {
                canvas.drawCircle(startX + botderLeft + gap * i, getHByValue(mData.get(i).y2), 8, mPaint);
                mPaint.setColor(0xffffffff);
                canvas.drawCircle(startX + botderLeft + gap * i, getHByValue(mData.get(i).y2), 4, mPaint);
                mPaint.setColor(mColor2);
            }
            if (i == mData.size() - 2) {
                canvas.drawCircle(startX + botderLeft + gap * (i + 1), getHByValue(mData.get(i + 1).y2), 8, mPaint);
                mPaint.setColor(0xffffffff);
                canvas.drawCircle(startX + botderLeft + gap * (i + 1), getHByValue(mData.get(i + 1).y2), 4, mPaint);
                mPaint.setColor(mColor2);
            }
        }
    }

    private void drawTheX(Canvas canvas) {
//画中间的线
        initPaint();
//纵坐标文字距离Y轴线的距离
        int textLeftBorder = DpUtil.dp2px(mContext, 2);
        mPaint.setColor(0xff92dac4);
//0度线
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        canvas.drawLine(botderLeft - textLeftBorder, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
        mPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
//每条横线的上下间隔
        float step = (getHeight() / 2 - borderTopAndBottom) / 5;
        int stepInt = (max - min) / 10;
//纵坐标文字大小
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        mPaint.setPathEffect(new DashPathEffect(new float[]{15, 10, 3, 10}, 0));
        for (int i = 0; i < 11; i++) {
//写纵坐标文字
            mPaint.setColor(0xff159461);
            canvas.drawText(max - i * stepInt + "",
                    botderLeft - mPaint.measureText(max - i * stepInt + "") - textLeftBorder,
                    borderTopAndBottom + i * step + (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) / 2 - mPaint.getFontMetrics().bottom,
                    mPaint);
            if (i == 5) {
                continue;
            }
            mPaint.setColor(0xdd92dac4);
            mPaint.setStrokeWidth(DpUtil.dp2px(mContext, 0.5f));
            canvas.drawLine(botderLeft, borderTopAndBottom + i * step, getWidth(), borderTopAndBottom + i * step, mPaint);
        }
    }
    private void drawTheBackground(Canvas canvas) {
    }
    //触摸处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mData == null || mData.size() == 0) {
            return super.onTouchEvent(event);
        }
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
// 按下
                mXDown = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
// 移动
                mLastX = (int) event.getRawX();
//1.5是加速滑动
                int tempx = (int) (lastStartX + (mLastX - mXDown) * 1.5);
// if (Math.abs(lastStartX - mXDown) < a) {
// break;
// }
//滑动限制
                if (tempx > botderLefttep) {
                    tempx = botderLefttep;
                }
                if (tempx < -((mData.size() + 1) * gap + botderLeft - getWidth())) {
                    tempx = -((mData.size() + 1) * gap + botderLeft - getWidth());
                }
                if(startX == tempx){
//说明已经绘制过,不再绘制
                    break;
                }
//1.5是加速滑动
                startX = tempx;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
// 抬起
                lastStartX = startX;
                postInvalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
    //通过Y的值获取Y轴坐标
    private float getHByValue(int y) {
        return (((float) (max - y) / (float) (max - min))) * (getHeight() - borderTopAndBottom * 2f) + borderTopAndBottom;
    }
    public static class dataObj {
        int x;
        int y1;
        int y2;
        public void setX(int x) {
            this.x = x;
        }
        public void setY1(int y1) {
            this.y1 = y1;
        }
        public void setY2(int y2) {
            this.y2 = y2;
        }
    }
}
/*在布局文件中使用。

<top.greendami.greendami.ShakeMaps
        android:layout_width="match_parent"
        android:layout_height="500dp"
        android:id="@+id/shakemaps"/>
        在Activity中添加数据

        List<ShakeMaps.dataObj> mData = new ArrayList<>();
        ShakeMaps.dataObj obj;
        for(int i = 0;i < 1000 ; i++){
        obj = new ShakeMaps.dataObj();
        obj.setX(i);
        obj.setY1((int)(Math.random()* -60) + 30);
        obj.setY2((int)(Math.random()* 60) - 30);
        mData.add(obj);
        } */
