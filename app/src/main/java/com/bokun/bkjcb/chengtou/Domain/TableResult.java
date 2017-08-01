package com.bokun.bkjcb.chengtou.Domain;

/**
 * Created by DengShuai on 2017/7/31.
 */

public class TableResult {
    /*  "suoshubankuai": "路桥",
        "jihuashu": 0,
        "weijishikaigong": 1,
        "weikaigong": 1,
        "year": 2018*/
    private String suoshubankuai;
    private int jihuashu;
    private int weijishikaigong;
    private int weikaigong;
    private int year;

    public String getSuoshubankuai() {
        return suoshubankuai;
    }

    public void setSuoshubankuai(String suoshubankuai) {
        this.suoshubankuai = suoshubankuai;
    }

    public int getJihuashu() {
        return jihuashu;
    }

    public void setJihuashu(int jihuashu) {
        this.jihuashu = jihuashu;
    }

    public int getWeijishikaigong() {
        return weijishikaigong;
    }

    public void setWeijishikaigong(int weijishikaigong) {
        this.weijishikaigong = weijishikaigong;
    }

    public int getWeikaigong() {
        return weikaigong;
    }

    public void setWeikaigong(int weikaigong) {
        this.weikaigong = weikaigong;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
