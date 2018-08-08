package com.example.administrator.testview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private HistoryDataView historyDataView;
    private ClockView clockView;
    private VerticalScaleView verticalScaleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyDataView = findViewById(R.id.history_detail_hdv);
        historyDataView.setDataBeans(getTestData());

//        clockView = findViewById(R.id.shizhong);

       /* verticalScaleView = findViewById(R.id.verticalScaleView);
        verticalScaleView.setRange(1,100);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
//        clockView.startClock();
    }

    private List<DataBean> dataBeans = new ArrayList<>();

    private List<DataBean> getTestData() {
        for (int i = 0;i<40;i++){
            DataBean dataBean = new DataBean();
            dataBean.setTime(System.currentTimeMillis());
            dataBean.setMoistureValue(20+getRandom(0,10));
            dataBean.setWhiteDegreeValue(40+getRandom(0,10));
            dataBeans.add(dataBean);
        }
        return dataBeans;
    }

    public static int getRandom(int min, int max){
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

}
