package com.bokun.bkjcb.chengtou.ChartUtil;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class MyLineChartValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        switch ((int) value) {
            case 0:
                return "道路";
            case 1:
                return "公路";
            case 2:
                return "内河航道";
            case 3:
                return "原水";
            case 4:
                return "上水";
            case 5:
                return "雨污水";
            case 6:
                return "固废";


        }
        return "";
    }
}
