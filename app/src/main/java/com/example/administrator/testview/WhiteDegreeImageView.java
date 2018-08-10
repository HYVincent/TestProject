package com.example.administrator.testview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name TestProject
 * @page com.example.administrator.testview
 * @class describe
 * @date 2018/8/10 15:52
 */
@SuppressLint("AppCompatCustomView")
public class WhiteDegreeImageView extends ImageView {

    private  Bitmap bitmap;
    private Bitmap newBitmap;
    private  Matrix matrix ;

    public WhiteDegreeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_skin_type_bg);
        matrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height =  h;
//        height = h;
        mBitmap = Bitmap.createBitmap((int) width,(int) height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    private float width;
    private float height;
    private Paint mPaint;

    private float angle;

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private float currentStartAngle = 0;
    private float animatedValue;
    private boolean isAnimFinish = true;
    private Context mContext;

    public void setAngle(float newAngle) {
        currentStartAngle = angle;
        this.angle = newAngle;
        ValueAnimator anim = ValueAnimator.ofFloat(currentStartAngle, angle);
        Log.d("fffff", "setAngle: "+currentStartAngle);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimFinish = false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimFinish = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas rootCanvas) {
        matrix.reset();
        newBitmap = zoomImg(bitmap,(int)(width*1.2),(int)height*2-DpUtil.dp2px(mContext,2));
//        mCanvas.translate(width/2,0);
        matrix.preTranslate(-(int)(width*0.1),-newBitmap.getHeight()/2);
//        matrix.postRotate(angle,width/2,-1);
        matrix.postRotate(animatedValue,width/2,-1);
        mCanvas.drawCircle(newBitmap.getWidth()/2,getHeight()/2,10,mPaint);
        mCanvas.drawBitmap(newBitmap,matrix,mPaint);
        mCanvas.drawCircle(0,0,10,mPaint);
        rootCanvas.drawBitmap(newBitmap,matrix,mPaint);
    }

    // 等比缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}
