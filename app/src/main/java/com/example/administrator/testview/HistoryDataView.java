package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name GoldenRiceWhiteDegree
 * @page com.shenmou.goldenricewhitedegree.view
 * @class describe 历史数据视图
 * @date 2018/8/7 9:33
 */
public class HistoryDataView extends View {

    private Context mContext;


    public HistoryDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }



   @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private float mViewWidth;
    private float mViewHeight;
    //顶部的tag，类型为两种，白皙度和水分值 默认为白皙度
    private String mTopTag;
    // true 白皙度 false 水分值
    private boolean isBXD = true;
    private float mTopTagMaraginLeft = 15;
    private Paint mTagPaint;
    private Paint mBgLinePaint;
    //白皙度画笔
    private Paint mWhiteDegreePaint;
    //水分值画笔
    private Paint mMoistureValuePaint;
    //第一条线距离文字的距离
    private float mFirstLineMarginTop = 20;
    //从上往下最后一条线到底部的距离
    private float mEndLineMarginButtom = 30;
    //白皙度path
    private Path mWhiteDegreePath;
    //水分值
    private Path mMoistureValuePath;
    //两条线之间的间距
    private float mItemLineMargin = 20;
    //顶部tag文字的高度
    private float mTagHeight;
    //背景线左右两端的间距
    private float mBgLineMargin = 10;
    //两个数据点之间的距离
    private float mItemDataMargin = 60;
    //数据
    private List<DataBean> dataBeans = new ArrayList<>();
    //当前选择的类型 分为三种 0-->日 1-->周 2-->月
    private int timeType = 0;
    //白皙度或者水分值的画笔
    private Paint mValuePaint;

    //手指在屏幕滑动的时候的偏移量
    private float offsetX = 0f;
    //手指按下的x坐标
    private float downX = 0f;
    //实际数据的宽度
    private float dataWidth = 0f;

    //绘制矩形的右下角坐标Y
    private float endY = 0;
    //手指触摸View时的X坐标
    private float touchX = 0;
    //x轴偏移量
    private float offset_x;
    //总的偏移量
    private float offset_x_d;
    //X轴的最大偏移量
    private float offset_x_max = 0f;
    //true 填充下面 false 不填充
    private boolean isFillDownLineColor;
    private Path path;
    private Path path2;

    private Paint mCircleDotPaint;

    /*设置数据*/
    public void setDataBeans(List<DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        invalidate();
    }

    /**
     * 改变模式 true 白皙度  false 水分值
     */
    public void changeModel(boolean isBXD) {
        this.isBXD = isBXD;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        mTopTag = context.getString(R.string.string_history_tag_white_degree);
        mTopTagMaraginLeft = DpUtil.dp2px(mContext, mTopTagMaraginLeft);
        mFirstLineMarginTop = DpUtil.dp2px(mContext, mFirstLineMarginTop);
        mBgLineMargin = DpUtil.dp2px(mContext, mBgLineMargin);
        mEndLineMarginButtom = DpUtil.dp2px(mContext, mEndLineMarginButtom);
        mItemDataMargin = DpUtil.dp2px(mContext, mItemDataMargin);
//        mItemDataMargin = (mViewWidth - DpUtil.dp2px(mContext,20))/6;
        isFillDownLineColor = true;

        mTagPaint = new Paint();
        mTagPaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_2a2a2a));
        mTagPaint.setTextSize(DpUtil.dp2px(mContext, 15));
        mTagPaint.setAntiAlias(true);

        mBgLinePaint = new Paint();
        mBgLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_gray_b0b0b0));
        mBgLinePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1) / 3);
        mBgLinePaint.setAntiAlias(true);

        mWhiteDegreePath = new Path();
        mMoistureValuePath = new Path();
        path = new Path();
        path2 = new Path();

        mWhiteDegreePaint = new Paint();
        mWhiteDegreePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mWhiteDegreePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        mWhiteDegreePaint.setAntiAlias(true);
//        mWhiteDegreePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhiteDegreePaint.setDither(true);
        // 把拐点设置成圆的形式，参数为圆的半径，这样就可以画出曲线了
        PathEffect pe = new CornerPathEffect(120);
        mWhiteDegreePaint.setPathEffect(pe);

        mCircleDotPaint = new Paint();
        mCircleDotPaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mCircleDotPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.DST_OVER));

        mMoistureValuePaint = new Paint();
        mMoistureValuePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mMoistureValuePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        mMoistureValuePaint.setAntiAlias(true);

        mValuePaint = new Paint();
        mValuePaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
        mValuePaint.setAntiAlias(true);
        mValuePaint.setTextSize(DpUtil.dp2px(mContext, 12));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isBXD) {
            mTopTag = mContext.getString(R.string.string_history_tag_white_degree);
            drawWhiteDegree(canvas);
        } else {
            mTopTag = mContext.getString(R.string.string_history_tag_moisture_value);
            drawMoistureValue(canvas);
        }
    }

    private void drawTopTag(Canvas canvas) {
        Rect mTagRect = new Rect();
        mTagPaint.getTextBounds(mTopTag, 0, mTopTag.length(), mTagRect);
        canvas.drawText(mTopTag, DpUtil.dp2px(mContext, 15), mTagRect.height(), mTagPaint);
        mTagHeight = mTagRect.height();
    }

    /**
     * 绘制背景线
     *
     * @param canvas
     */
    private void drawBgLine(Canvas canvas) {
        mItemLineMargin = (mViewHeight - mTagHeight - mFirstLineMarginTop - mEndLineMarginButtom) / 5;
        //绘制虚线
        mBgLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        //关闭硬件加速，否则虚线变实线
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //5个格子，6条线，第一条线为实线，第二条线为虚线
        for (int i = 1; i < 6; i++) {
            canvas.drawLine(mBgLineMargin, mViewHeight - mEndLineMarginButtom - i * mItemLineMargin,
                    mViewWidth - mBgLineMargin, mViewHeight - mEndLineMarginButtom - i * mItemLineMargin, mBgLinePaint);
        }
        mBgLinePaint.setPathEffect(null);
        canvas.drawLine(mBgLineMargin, mViewHeight - mEndLineMarginButtom, mViewWidth - mBgLineMargin, mViewHeight - mEndLineMarginButtom, mBgLinePaint);
    }

    /**
     * 绘制白皙度 0~100 0 2 4 6 8 10 小于20的时候不需要
     *
     * @param canvas
     */
    private void drawWhiteDegree(Canvas canvas) {
        drawTopTag(canvas);
        drawBgLine(canvas);
        canvas.save();
        drawWhiteDegreeDetail(canvas);
    }

    /**
     * 绘制水分值 0~60 0 20 30 40 50 60 小于20的时候就不保存 大于60=60
     *
     * @param canvas
     */
    private void drawMoistureValue(Canvas canvas) {
        drawTopTag(canvas);
        drawBgLine(canvas);
        canvas.save();
        drawMositureDetail(canvas);
    }

    /**
     * 这里绘制白皙度的值
     *
     * @param canvas
     */
    private void drawWhiteDegreeDetail(Canvas canvas) {
        /*绘制白皙度 0~100 0 2 4 6 8 10 小于20的时候不需要*/
        //当值超过屏幕的时候需要可以滑动
        //两个数值之间的距离
        mWhiteDegreePath.reset();
        //绘制地下的文字
        offset_x_d += offset_x;
       /* if(offset_x_d > 0){
            offset_x_d = 0;
        }
        if((-1)*offset_x_d >offset_x_max){
            offset_x_d = (-1)*offset_x_max;
        }*/
        if (dataBeans.size() > 0) {
            /*for (int i = 0;i <dataBeans.size();i++){
                float currentX = DpUtil.dp2px(mContext,20)+i * mItemDataMargin+offset_x_d;
                float currentY = getWhiteDegreeY(dataBeans.get(i).getWhiteDegreeValue());
                if(i == dataBeans.size() - 1 && offset_x_max == 0){
                    //初始化最大偏移量 后面的+20是为了加上末尾距离屏幕左边的边距
                    offset_x_max = currentX  - mViewWidth + DpUtil.dp2px(mContext,20);
                }
                float nextX;
                float nextY;
                //两个点之间的间距
                float dotMargin;
                if(i == dataBeans.size() - 1){
                    //最后一个点
                    nextX = DpUtil.dp2px(mContext,20)+i * mItemDataMargin+offset_x_d;
                    nextY = getWhiteDegreeY(dataBeans.get(1).getWhiteDegreeValue());
                }else {
                    nextX = DpUtil.dp2px(mContext,20)+(i+1) * mItemDataMargin+offset_x_d;
                    nextY = getWhiteDegreeY(dataBeans.get(i+1).getWhiteDegreeValue());
                }
                dotMargin = nextY - currentX;
                if(dotMargin < 0){
                    dotMargin = dotMargin * (-1);
                }
                if(i == 0){
                    mWhiteDegreePath.moveTo(currentX,currentY);
                }
                mWhiteDegreePath.lineTo(currentX,currentY);
//                mWhiteDegreePath.cubicTo(currentX,currentY,currentX+mItemDataMargin/4,currentY - dotMargin/2,nextX,nextY);
                mWhiteDegreePaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(currentX,currentY,DpUtil.dp2px(mContext,3),mWhiteDegreePaint);
                mWhiteDegreePaint.setStyle(Paint.Style.STROKE);
                Rect valueRect = new Rect();
                String valueStr = String.valueOf(dataBeans.get(i).getWhiteDegreeValue());
                mValuePaint.getTextBounds(valueStr,0,valueStr.length(),valueRect);
                canvas.drawText(valueStr,currentX-valueRect.width()/2,
                        currentY - DpUtil.dp2px(mContext,5) - valueRect.height(),mValuePaint);
            }
            //            canvas.drawPath(mWhiteDegreePath,mWhiteDegreePaint);
            */
            offset_x_max = (dataBeans.size() - 1) * mItemDataMargin - (mViewWidth);
            Log.d("偏移量", "------->【" + offset_x_d + "】"+"【"+offset_x_max+"】"+"【"+mViewWidth+"】");
            if (offset_x_d > 0) {
                offset_x_d = 0;
            }
            if ((-1)*offset_x_d > offset_x_max) {
                offset_x_d = (-1)*offset_x_max;
            }
            int startX = (int) (DpUtil.dp2px(mContext, 30)+offset_x_d);
            int startY = (int) (mViewHeight - mEndLineMarginButtom);
            if (!isFillDownLineColor) {
                mWhiteDegreePaint.setStyle(Paint.Style.STROKE);
            }
            path.moveTo(startX, startY);
            int count = dataBeans.size();
            for (int i = 0; i < count - 1; i++) {
                // x,y表示当前点  x4,y4表示下一个点 x2,x3都是属于中间的点
                float x, y, x2, y2, x3, y3, x4, y4;
                x = startX + mItemDataMargin * i;
                x4 = startX + mItemDataMargin * (i + 1);
                x2 = x3 = (x + x4) / 2;
                // 乘以这个fraction是为了添加动画特效
                y = getWhiteDegreeY(dataBeans.get(i).getWhiteDegreeValue());
                y4 = getWhiteDegreeY(dataBeans.get(i + 1).getWhiteDegreeValue());
                y2 = y;
                y3 = y4;
                // 填充颜色
                if (isFillDownLineColor && i == 0) {
                    // 形成封闭的图形
                    path2.moveTo(x, y);
                    path.moveTo(x, startY);
                    path.lineTo(x, y);
                }
                path.cubicTo(x2, y2, x3, y3, x4, y4);
                path2.cubicTo(x2, y2, x3, y3, x4, y4);
                //绘制实心圆和数字
                canvas.drawCircle(x, y, DpUtil.dp2px(mContext, 3), mCircleDotPaint);
                Rect valueRect = new Rect();
                String valueStr = String.valueOf(dataBeans.get(i).getWhiteDegreeValue());
                mValuePaint.getTextBounds(valueStr, 0, valueStr.length(), valueRect);
                canvas.drawText(valueStr, x - valueRect.width() / 2,
                        y - DpUtil.dp2px(mContext, 5) - valueRect.height(), mValuePaint);
            }
            if (isFillDownLineColor) {
                // 形成封闭的图形
                path.lineTo(startX + (dataBeans.size() - 1) * mItemDataMargin, startY);
            }
            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.BLUE);
            float left = startX-DpUtil.dp2px(mContext,3);
            float top = getPaddingTop();
            float right = startX + (dataBeans.size() - 1) * mItemDataMargin;
            float bottom = startY;
            // 渐变的颜色
            LinearGradient lg = new LinearGradient(left, top, left, bottom, Color.parseColor("#00ffffff"),
                    Color.parseColor("#bFffffff"), Shader.TileMode.CLAMP);// CLAMP重复最后一个颜色至最后
            rectPaint.setShader(lg);
            rectPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            if (isFillDownLineColor) {
                canvas.drawPath(path, mWhiteDegreePaint);
            }
            canvas.drawRect(left, top, right, bottom, rectPaint);
        }
    }

    /**
     * 根据白皙度的Y值计算当前数据在视图中的坐标Y
     *
     * @param value 100 为最大值
     * @return
     */
    private float getWhiteDegreeY(float value) {
        return mViewHeight - mEndLineMarginButtom - mItemLineMargin * 5 * value / 100;
    }

    /**
     * 这里绘制水分值
     *
     * @param canvas
     */
    private void drawMositureDetail(Canvas canvas) {
        //绘制水分值 0~60 0 20 30 40 50 60 小于20的时候就不保存 大于60=60


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取手指触摸屏幕的点
                touchX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                offset_x = event.getX() - touchX;
                //降低滚动的速度
//                offset_x = offset_x/20;
                //在当前位置基础加上偏移量
                offset_x_d += offset_x;
                if (offset_x_d > 0) {
                    offset_x_d = 0;
                }
                if(offset_x_max < 0 && offset_x_d *(-1) > offset_x_max){
                    offset_x_d = (-1) * offset_x_max;
                }
                if(offset_x_d < 0 && offset_x_d > offset_x_max * (-1)){
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
//                invalidate();
                break;
            default:
                break;
        }
        return true;
    }


}
