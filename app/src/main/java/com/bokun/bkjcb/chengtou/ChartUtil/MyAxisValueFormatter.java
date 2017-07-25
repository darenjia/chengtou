package com.bokun.bkjcb.chengtou.ChartUtil;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    public MyAxisValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        switch ((int) value) {
            case 0:
                return "路桥";
            case 1:
                return "水务";
            case 2:
                return "置地";
            case 3:
                return "环境";
            case 4:
                return "其他";
            case 5:
                return "航运";

        }
        return "";
    }
}
