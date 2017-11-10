package com.example.wxj;

/**
 * Created by hwt on 2016/3/18.
 */
public class BottomNavigationBean {

    public String text;
    public int resId;
    public int color;

    private int a;

    public BottomNavigationBean(String text, int resId, int color) {
        this.text = text;
        this.resId = resId;
        this.color = color;
    }
}
