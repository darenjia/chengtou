package com.bokun.bkjcb.chengtou.ChartUtil;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by DengShuai on 2017/8/1.
 */

public class MyLineValueFormatter implements IValueFormatter {
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return new DecimalFormat("###,###,###.##").format(value);
    }
}
