package com.example.administrator.testview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private HistoryDataView historyDataView;
    private WhiteDegreeValueShowView whiteDegreeValueShowView;
    private WhiteDegreeImageView whiteDegreeImageView;

    private boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyDataView = findViewById(R.id.history_detail_hdv);
        historyDataView.setDataBeans(getTestData());
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b) {
                    b = false;
                } else {
                    b = true;
                }
                historyDataView.changeModel(b);
            }
        });
        whiteDegreeValueShowView = findViewById(R.id.cav);
        findViewById(R.id.btn_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                angle +=5;
                /* whiteDegreeValueShowView.setAngle(angle);*/
//               rotateView(3);
//                rotateView(30);
//                angle += 90;
                whiteDegreeImageView.setAngle(angle);
            }
        });
        whiteDegreeImageView = findViewById(R.id.testImageView);

    }

    private int angle = 0;

    private void rotateView(int i) {
        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);//设置动画结束后保留当前状态
        animation.setDuration(500);//动画持续时间
//        animation.setRepeatMode(ValueAnimation.RESTART);//重复类型有两个值，取值为ValueAnimation.RESTART时,表示正序重新开始，当取值为ValueAnimation.REVERSE表示倒序重新开始。
//        animation.setStartOffset(2000);//调用start函数之后等待开始运行的时间，单位为毫秒
//        animation.setZAdjustment(300);//表示被设置动画的内容运行时在Z轴上的位置（top/bottom/normal），默认为normal
        animation.startNow();
//        animation.setRepeatCount(1);//设置重复次数  这里的次数是从0开始计数的  即设置为2时执行了3次 设置为INFINITE表示无限循环
        whiteDegreeValueShowView.setAnimation(animation);
//        whiteDegreeValueShowView.rotateView(23);
//        testImageView.setAnimation(animation);
    }

    private List<DataBean> dataBeans = new ArrayList<>();

    private List<DataBean> getTestData() {
        for (int i = 0; i < 40; i++) {
            DataBean dataBean = new DataBean();
            dataBean.setTime(System.currentTimeMillis());
            dataBean.setMoistureValue(20 + getRandom(0, 10));
            dataBean.setWhiteDegreeValue(40 + getRandom(0, 10));
            dataBeans.add(dataBean);
        }
        return dataBeans;
    }

    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

}
