package com.bokun.bkjcb.chengtou.Domain;

/**
 * Created by DengShuai on 2017/7/31.
 */

public class TableResultZD {
    /*  "suoshubankuai": "路桥",
          "zhongdajishi": 1,
        "zhongdaweijishi": 0,
        "zhongdaweikai": 4,
        "year": 2018*/
    private String suoshubankuai;
    private int zhongdajishi;
    private int zhongdaweijishi;
    private int zhongdaweikai;
    private int year;

    public String getSuoshubankuai() {
        return suoshubankuai;
    }

    public void setSuoshubankuai(String suoshubankuai) {
        this.suoshubankuai = suoshubankuai;
    }

    public int getZhongdajishi() {
        return zhongdajishi;
    }

    public void setZhongdajishi(int zhongdajishi) {
        this.zhongdajishi = zhongdajishi;
    }

    public int getZhongdaweijishi() {
        return zhongdaweijishi;
    }

    public void setZhongdaweijishi(int zhongdaweijishi) {
        this.zhongdaweijishi = zhongdaweijishi;
    }

    public int getZhongdaweikai() {
        return zhongdaweikai;
    }

    public void setZhongdaweikai(int zhongdaweikai) {
        this.zhongdaweikai = zhongdaweikai;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
