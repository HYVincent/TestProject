package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

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
public class HistoryDataView extends BaseScrollerView {

    private Context mContext;


    public HistoryDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
//        Log.d(TAG, "onSizeChanged111: 【"+mViewWidth+"】"+"【"+mViewHeight+"】");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
//        Log.d(TAG, "onSizeChanged222: 【"+mViewWidth+"】"+"【"+mViewHeight+"】");
//        mBitmap = Bitmap.createBitmap((int) mViewWidth,(int) mViewHeight, Bitmap.Config.ARGB_8888);
//        mCanvas = new Canvas(mBitmap);
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
    private float mFirstLineMarginTop = 40;
    //从上往下最后一条线到底部的距离
    private float mEndLineMarginButtom = 30;
    //白皙度path
//    private Path mWhiteDegreePath;
    //两条线之间的间距
    private float mItemLineMargin;
    //顶部tag文字的高度
    private float mTagHeight;
    //背景线左右两端的间距
    private float mBgLineMargin = 10;
    //两个数据点之间的距离
    private float mItemDataMargin = 60;
    //数据
    private List<DataBean> dataBeans;
    //当前选择的类型 分为三种 0-->日 1-->周 2-->月 一周或者一月中的某天有多个数据时取平均值
    //类型为 1或者2 的时候都是显示为 08/06 格式，否则显示 14：55
    private int timeType = 0;
    //白皙度或者水分值的画笔
    private Paint mValuePaint;
    //x轴上的最大偏移量
    private float mMaxOffsetX = 0;
    //第一个数据距离屏幕左边的距离
    private float mStartDataX = 30;
    //最后一个数据距离屏幕右边的距离
    private float mEndDataMaginRight = 30;
    //百分比
    private float mPercentage;
    //true 填充下面 false 不填充
//    private boolean isFillDownLineColor;
//    private Path path;
//    private Path path2;
    private Paint mCircleDotPaint;
    private Paint rectPaint;
    private Point[] circlePoint;

    private Matrix matrix;
    private Paint mLinePaint;

    /*设置数据*/
    public void setDataBeans(List<DataBean> datas) {
        this.dataBeans.clear();
        if (datas != null && datas.size() > 0) {
            this.dataBeans.addAll(datas);
        }
        invalidate();
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
        invalidate();
    }


    /**
     * 改变模式 true 白皙度  false 水分值
     */
    public void changeModel(boolean isBXD) {
        this.isBXD = isBXD;
        mOffsetX = 0;
        mPercentage = 0;
        mMaxOffsetX = 0;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        dataBeans = new ArrayList<>();
        matrix = new Matrix();
        mTopTag = context.getString(R.string.string_history_tag_white_degree);
        mTopTagMaraginLeft = DpUtil.dp2px(mContext, mTopTagMaraginLeft);
        mFirstLineMarginTop = DpUtil.dp2px(mContext, mFirstLineMarginTop);
        mBgLineMargin = DpUtil.dp2px(mContext, mBgLineMargin);
        mEndLineMarginButtom = DpUtil.dp2px(mContext, mEndLineMarginButtom);
        mItemDataMargin = DpUtil.dp2px(mContext, mItemDataMargin);
        mStartDataX = DpUtil.dp2px(mContext, mStartDataX);
        mEndDataMaginRight = DpUtil.dp2px(mContext, mEndDataMaginRight);

        mTagPaint = new Paint();
        mTagPaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_2a2a2a));
        mTagPaint.setTextSize(DpUtil.dp2px(mContext, 15));
        mTagPaint.setAntiAlias(true);

        mBgLinePaint = new Paint();
        mBgLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_gray_b0b0b0));
        mBgLinePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1) / 3);
        mBgLinePaint.setAntiAlias(true);


        mWhiteDegreePaint = new Paint();
        mWhiteDegreePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mWhiteDegreePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        mWhiteDegreePaint.setAntiAlias(true);
        mWhiteDegreePaint.setDither(true);
        // 把拐点设置成圆的形式，参数为圆的半径，这样就可以画出曲线了
        PathEffect pe = new CornerPathEffect(120);
        mWhiteDegreePaint.setPathEffect(pe);

        mCircleDotPaint = new Paint();
        mCircleDotPaint.setStrokeWidth(DpUtil.dp2px(mContext, 2));
        mCircleDotPaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_FF6347));
        mCircleDotPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_OVER));

        mMoistureValuePaint = new Paint();
        mMoistureValuePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fa73a2));
        mMoistureValuePaint.setStrokeWidth(DpUtil.dp2px(mContext, 1));
        mMoistureValuePaint.setAntiAlias(true);

        mValuePaint = new Paint();
        mValuePaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
        mValuePaint.setAntiAlias(true);
        mValuePaint.setTextSize(DpUtil.dp2px(mContext, 12));

        rectPaint = new Paint();
//        rectPaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
        rectPaint.setStrokeWidth(DpUtil.dp2px(mContext, 8));
        rectPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_ATOP));

        mLinePaint = new Paint();
        mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_fe728c));
        mLinePaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    private static final String TAG = "历史数据";


    private Canvas canvas;

    @Override
    protected void onDrawContent(Canvas mCanvas) {
        mCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_OVER);
        if (mMaxOffsetX == 0 && dataBeans.size() > 0) {
            mMaxOffsetX = mStartDataX + (dataBeans.size() - 1) * mItemDataMargin - mViewWidth + mEndDataMaginRight;
//            Log.d(TAG, "onSizeChanged333:【"+mItemDataMargin+"】"+"【"+mViewWidth+"】"+"【"+mStartDataX+"】"+"【"+mMaxOffsetX+"】"+"【"+mEndDataMaginRight+"】");
            setMaxVal((int) mMaxOffsetX);
        }
        if (isBXD) {
            mTopTag = mContext.getString(R.string.string_history_tag_white_degree);
        } else {
            mTopTag = mContext.getString(R.string.string_history_tag_moisture_value);
//            绘制水分值 0~60 0 20 30 40 50 60 小于20的时候就不保存 大于60=60
        }
        //不需要移动的
        drawTopTag(mCanvas);
        drawBgLine(mCanvas);
        //保存和平移
        mCanvas.save();
        if (dataBeans.size() > 0) {
            if (mStartDataX + (dataBeans.size() - 1) * mItemDataMargin > mViewWidth) {
                mCanvas.translate(mOffsetX, 0);
            }
//        Log.d(TAG, "drawWhiteDegree: 滑动偏移量-->【"+mOffsetX+"】");
            // 绘制白皙度 0~100 0 2 4 6 8 10 小于20的时候不需要
            //绘制数据内容
            if (dataBeans.size() == 1) {
                Rect valueRect = new Rect();
                String valueStr;
                if (isBXD) {
                    valueStr = String.valueOf(dataBeans.get(0).getWhiteDegreeValue());
                } else {
                    valueStr = String.valueOf(dataBeans.get(0).getMoistureValue());
                }
                mValuePaint.getTextBounds(valueStr, 0, valueStr.length(), valueRect);
                mCanvas.drawText(valueStr, mStartDataX - valueRect.width() / 2,
                        getWhiteDegreeY(dataBeans.get(0)) - DpUtil.dp2px(mContext, 5) - valueRect.height(), mValuePaint);
                mCanvas.drawCircle(mStartDataX, getWhiteDegreeY(dataBeans.get(0)), DpUtil.dp2px(mContext, 3), mCircleDotPaint);
            } else {
                drawWhiteDegreeDetail(mCanvas);
            }
        } else {
            Log.e(TAG, "onDrawContent: dataBeas is null");
        }
        mCanvas.restore();
        this.canvas = mCanvas;
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
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //5个格子，6条线，第一条线为实线，第二条线为虚线
        for (int i = 1; i < 6; i++) {
            canvas.drawLine(mBgLineMargin, mViewHeight - mEndLineMarginButtom - i * mItemLineMargin,
                    mViewWidth - mBgLineMargin, mViewHeight - mEndLineMarginButtom - i * mItemLineMargin, mBgLinePaint);
        }
        mBgLinePaint.setPathEffect(null);
        canvas.drawLine(mBgLineMargin, mViewHeight - mEndLineMarginButtom, mViewWidth - mBgLineMargin, mViewHeight - mEndLineMarginButtom, mBgLinePaint);
    }


    private float minY = 0;


    /**
     * 这里绘制白皙度的值
     *
     * @param canvas
     */
    private void drawWhiteDegreeDetail(Canvas canvas) {
        /*绘制白皙度 0~100 0 2 4 6 8 10 小于20的时候不需要*/
        //当值超过屏幕的时候需要可以滑动
        //两个数值之间的距离
        //绘制地下的文字
        Path path = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        if (dataBeans.size() > 0) {
            float startX = mStartDataX;
            float startY = mViewHeight - mEndLineMarginButtom;
            path.moveTo(startX, startY);
            int count = dataBeans.size();
            circlePoint = new Point[count];
//            Log.d(TAG, "drawWhiteDegreeDetail: 共【"+count+"】个数据");
            float x, y, x2, y2, x3, y3, x4, y4;
            for (int i = 0; i < count; i++) {
                // x,y表示当前点  x4,y4表示下一个点 x2,x3都是属于中间的点
                x = startX + mItemDataMargin * i;
                y = getWhiteDegreeY(dataBeans.get(i));
                if (y > minY) {
                    minY = y;
                }
                if (i == count - 1) {
                    x4 = x;
                    y4 = y;
                } else {
                    x4 = startX + mItemDataMargin * (i + 1);
                    y4 = getWhiteDegreeY(dataBeans.get(i + 1));
                }
                x2 = x3 = (x + x4) / 2;
                y2 = y;
                y3 = y4;
                // 填充颜色
                if (i == 0) {
                    // 形成封闭的图形
                    path2.moveTo(x, y);
                    path3.moveTo(x,y);
                    path.moveTo(x, startY);
                    path.lineTo(x, startY);
                    path.lineTo(x, y);
                    //下面这一行不写，则表的左上角有个空白，不知道为啥
                    path.lineTo(x, y);
                }
                if (i == count - 1) {
                    path.lineTo(x, startY);
                    path2.lineTo(x, startY);
                } else {
                    path.cubicTo(x2, y2, x3, y3, x4, y4);
                    path2.cubicTo(x2, y2, x3, y3, x4, y4);
                    path3.cubicTo(x2, y2, x3, y3, x4, y4);
                    Log.d(TAG, "drawWhiteDegreeDetail: 白皙度y2-->" + y2 + "   y3-->" + y3 + "  y4-->" + y4);
                }
//                canvas.drawCircle(x, y, DpUtil.dp2px(mContext, 3), mCircleDotPaint);
                circlePoint[i] = new Point((int) x, (int) y);
                //绘制时间
                drawButtomTime(dataBeans.get(i), canvas, circlePoint[i]);
            }
            // 形成封闭的图形
            path.lineTo(startX + (dataBeans.size() - 1) * mItemDataMargin, startY);
            path.close();

            float left = startX - DpUtil.dp2px(mContext, 3);
            float top = getPaddingTop();
            float right = startX + dataBeans.size() * mItemDataMargin;
            float bottom = startY;
            // 渐变的颜色
            LinearGradient lg = new LinearGradient(left, top, left, bottom, Color.parseColor("#00ffffff"),
                    Color.parseColor("#ffffff"), Shader.TileMode.CLAMP);// CLAMP重复最后一个颜色至最后
            rectPaint.setShader(lg);
            canvas.drawPath(path, mWhiteDegreePaint);
            canvas.drawRect(left, top, right, bottom, rectPaint);
            //注意绘制顺序，顺序错误将无法显示粗线
            canvas.drawPath(path3,mLinePaint);
            for (int i = 0; i < circlePoint.length; i++) {
                Rect valueRect = new Rect();
                String valueStr;
                if (isBXD) {
                    valueStr = String.valueOf(dataBeans.get(i).getWhiteDegreeValue());
                } else {
                    valueStr = String.valueOf(dataBeans.get(i).getMoistureValue());
                }
                mValuePaint.getTextBounds(valueStr, 0, valueStr.length(), valueRect);
                canvas.drawText(valueStr, circlePoint[i].x - valueRect.width() / 2,
                        circlePoint[i].y - DpUtil.dp2px(mContext, 5) - valueRect.height(), mValuePaint);
                canvas.drawCircle(circlePoint[i].x, circlePoint[i].y, DpUtil.dp2px(mContext, 3), mCircleDotPaint);
            }
        }
    }

    /**
     * 绘制底部的文字
     *
     * @param dataBean
     * @param canvas
     * @param point
     */
    private void drawButtomTime(DataBean dataBean, Canvas canvas, Point point) {
        String dateStr;
        if (timeType != 0) {
            //按日期绘制
            dateStr = DateUtil.getDateString(DateUtil.DATA_FORMAT_MONTH_DAY, dataBean.getTime());
        } else {
            //按时间绘制
            dateStr = DateUtil.getDateString(DateUtil.DATE_FORMAT_HM, dataBean.getTime());
        }
        Rect rect = new Rect();
        mValuePaint.getTextBounds(dateStr, 0, dateStr.length(), rect);
        mValuePaint.setColor(ContextCompat.getColor(mContext, R.color.color_gray_9fa1a9));
        canvas.drawText(dateStr, point.x - rect.width() / 2, mViewHeight - mEndLineMarginButtom / 2 + rect.height() / 2, mValuePaint);
        mValuePaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
    }

    /**
     * 根据白皙度的Y值计算当前数据在视图中的坐标Y
     *
     * @param dataBean
     * @return 白皙度最大值是 100 为最大值
     */
    private float getWhiteDegreeY(DataBean dataBean) {
        float y = 0;
        if (isBXD) {
            y = mViewHeight - mEndLineMarginButtom - mItemLineMargin * 5 * dataBean.getWhiteDegreeValue() / 100;
            return y > mFirstLineMarginTop ? y : mFirstLineMarginTop;
        } else {
            y = mViewHeight - mEndLineMarginButtom - mItemLineMargin * 5 * dataBean.getMoistureValue() / 60;
            return y > mFirstLineMarginTop ? y : mFirstLineMarginTop;
        }
    }



}