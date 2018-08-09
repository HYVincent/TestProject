package com.example.administrator.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;


/**
 * 作者: jack(黄冲)
 * 邮箱: 907755845@qq.com
 * create on 2018/3/30 17:27
 */

public abstract class BaseScrollerView extends View implements GestureActionListener {

    private VelocityTracker mVelocityTracker;
    private ScrollerRunnable mRunnable;
    protected float mOffsetX;
    protected final int MAX_OFFSETX = (int) (40 * WindowsInfo.sAutoScaleX);
    protected final int MIN_OFFSETX = 0;
    protected int mMaxVal;
    public float mDownX, mDownY;

    protected float mMoveXOffsetCount, mMoveYOffsetCount;

    private float mPointerSpacing, mPointerSpacingX, mPointerSpacingY;
    protected ScaleDetector mDetector = new ScaleDetector();
    protected PointF mDownCenter = new PointF();

    protected final int CLICK_OFFSET = (int) (10 * WindowsInfo.sAutoScaleX);
    protected final int CLICK_RADIUS = (int) (60 * WindowsInfo.sAutoScaleX);


    public boolean isMove, isMove2;


    public BaseScrollerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
    }

    public void setMaxVal(int maxVal){
        mMaxVal = maxVal == 0 ? 0 : maxVal + MAX_OFFSETX;
    }


    class ScrollerRunnable implements Runnable{

        private OverScroller mScroller;

        public ScrollerRunnable() {
            mScroller = new OverScroller(getContext());
        }

        public void cancel(){
            mScroller.forceFinished(true);
        }

        public boolean isFinished(){
            return mScroller.isFinished();
        }
        public void fling(int velocityX){
            mScroller.fling((int) -mOffsetX, 0, velocityX, MIN_OFFSETX, 0, mMaxVal, 0, 0);
//            mScroller.springBack((int) -mOffsetX, 0, 0, 0, mMaxVal, 0);
        }

        @Override
        public void run() {

            if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
                mOffsetX = -mScroller.getCurrX();

                onRoll(mScroller);
                postDelayed(this, 10);
                postInvalidateOnAnimation();
            }else {
                onFinishRoll();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & 0xff) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                down(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isMove2 = false;
                pointerDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1)
                    move(event);
                else if(event.getPointerCount() == 2){
                    pointerMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                up(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointerUp(event);
                break;
        }
        invalidate();
        return true;
    }

    public void down(MotionEvent event){
        Log.e("tag", "down   " + event.getX(0) + "   " + event.getY(0));
        mMoveXOffsetCount = 0;
        mMoveYOffsetCount = 0;
        mVelocityTracker.clear();
        mVelocityTracker.addMovement(event);
        if (mRunnable != null){
            if (!mRunnable.isFinished()) {
                mRunnable.cancel();
                onRollCancel();
            }
            mRunnable = null;
        }
        mDownX = event.getX();
        mDownY = event.getY();

    }

    public void move(MotionEvent event){
        mVelocityTracker.addMovement(event);
        float dx = event.getX() - mDownX;
        float dy = event.getY() - mDownY;


        mMoveXOffsetCount += Math.abs(dx);
        mMoveYOffsetCount += Math.abs(dy);
        mDownX = event.getX();
        mDownY = event.getY();

        if (!isMove){
            Log.e("tag", "move   " + mDownX + "   " + mDownY);
            isMove = true;
        }


        if (mMoveXOffsetCount < CLICK_OFFSET){
            return;
        }
        onDrag(event, dx, dy);

        mOffsetX = -(mOffsetX + dx) < 0 ? 0 : -(mOffsetX + dx) > mMaxVal ? -mMaxVal : mOffsetX + dx;
    }

    public void up(MotionEvent event){
        Log.e("tag", "up   " + event.getX(0) + "   " + event.getY(0));
        if (checkClick()){
            onClick(event);
            return;
        }
        if (mOffsetX == 0 || mOffsetX == -mMaxVal){
            return;
        }
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(1000);
        //VelocityTracker.getXVelocity() 获得滑动的速度
        int xV = (int) mVelocityTracker.getXVelocity();
        mRunnable = new ScrollerRunnable();
        mRunnable.fling(-xV);
        post(mRunnable);
    }

    @Override
    public void pointerDown(MotionEvent event) {
        mDownCenter = getCenter(event);
        mPointerSpacing = pointerSpacing(event);
        mPointerSpacingX = pointerSpacingX(event);
        mPointerSpacingY = pointerSpacingY(event);
        mDownX = event.getX(0);
        mDownY = event.getY(0);


        Log.e("tag", "doubleDown   " + event.getX(0) + "   " + event.getY(0) + "    ||    " + event.getX(1) + "  " + event.getY(1));
    }

    @Override
    public void pointerMove(MotionEvent event) {
        float spacing = pointerSpacing(event);
        mDetector.scale = checkScale(spacing / mPointerSpacing - 1);
        mDetector.scaleX = checkScale(pointerSpacingX(event) / mPointerSpacingX - 1);
        mDetector.scaleY = checkScale(pointerSpacingY(event) / mPointerSpacingY - 1);

        mDetector.absScale += mDetector.scale;
        mDetector.absScaleX += mDetector.scaleX;
        mDetector.absScaleY += mDetector.scaleY;
        mDetector.centerX = getCenter(event).x;
        mDetector.centerY = getCenter(event).y;


        if (!isMove2){

            Log.e("tag", "doubleMove   " + event.getX(0) + "   " + event.getY(0) + "    ||    " + event.getX(1) + "  " + event.getY(1)  + "  ||  " + mDetector.scaleX);
            isMove2 = true;

        }


        onScale(mDetector);

        mPointerSpacing = pointerSpacing(event);
        mPointerSpacingX = pointerSpacingX(event);
        mPointerSpacingY = pointerSpacingY(event);
    }

    @Override
    public void pointerUp(MotionEvent event) {
        int index = event.getActionIndex();

        mDownX = event.getX(index == 0 ? 1 : 0);
        mDownY = event.getY(index == 0 ? 1 : 0);

        Log.e("tag", "doubleUp   " + event.getX(0) + "   " + event.getY(0) + "    ||    " + event.getX(1) + "  " + event.getY(1));
    }

    @Override
    public void onScale(ScaleDetector detector) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        onDrawContent(canvas);
    }


    public float checkScale(float scale){
        if (scale < -0.05f) return -0.05f;
        if (scale > 0.05f) return 0.05f;
        return scale;
    }

    /**
     * 计算两指之间的线长
     * @param event
     * @return
     */
    public float pointerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.abs(Math.sqrt(x * x + y * y));
    }

    public float pointerSpacingX(MotionEvent event) {
        return Math.abs(event.getX(0) - event.getX(1));
    }

    public float pointerSpacingY(MotionEvent event) {
        return Math.abs(event.getY(0) - event.getY(1));
    }

    /**
     * 获取两指之间的中心点
     * @param event
     * @return
     */
    public PointF getCenter(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    protected boolean checkClick(){
        return mMoveXOffsetCount < CLICK_OFFSET && mMoveYOffsetCount < CLICK_OFFSET;
    }

    public void onClick(MotionEvent event){}

    public void onDrag(MotionEvent event ,float dx, float dy) {}

    public void onFinishRoll() {}

    protected abstract void onDrawContent(Canvas canvas);

    public void onRoll(OverScroller scroller){}

    public void onRollCancel(){}

}
