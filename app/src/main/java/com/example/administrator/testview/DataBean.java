package com.example.administrator.testview;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name GoldenRiceWhiteDegree
 * @page com.shenmou.goldenricewhitedegree.bean
 * @class describe 白皙度 水分值数据
 * @date 2018/8/7 11:18
 */
public class DataBean {
    /*白皙度值*/
    private float whiteDegreeValue;
    //白皙度标记
    private float whiteDegreeTag;
    /*水分值*/
    private float moistureValue;
    /*水分值标记*/
    private String waterValueTag;
    /*日期*/
    private long time;

    public float getWhiteDegreeTag() {
        return whiteDegreeTag;
    }

    public void setWhiteDegreeTag(float whiteDegreeTag) {
        this.whiteDegreeTag = whiteDegreeTag;
    }

    public String getWaterValueTag() {
        return waterValueTag;
    }

    public void setWaterValueTag(String waterValueTag) {
        this.waterValueTag = waterValueTag;
    }

    public float getWhiteDegreeValue() {
        return whiteDegreeValue;
    }

    public void setWhiteDegreeValue(float whiteDegreeValue) {
        this.whiteDegreeValue = whiteDegreeValue;
    }

    public float getMoistureValue() {
        return moistureValue;
    }

    public void setMoistureValue(float moistureValue) {
        this.moistureValue = moistureValue;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
