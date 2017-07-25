package com.bokun.bkjcb.chengtou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.chengtou.ChartUtil.MyAxisValueFormatter;
import com.bokun.bkjcb.chengtou.ChartUtil.MyLineChartValueFormatter;
import com.bokun.bkjcb.chengtou.ChartUtil.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rmondjone.locktableview.LockTableView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class MainFragment extends Fragment {
    BarDataSet set1;
    private LockTableView mLockTableView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        init(view);
        return view;
    }

    private void init(View view) {

//        parentView = (LinearLayout) view.findViewById(R.id.parent_view);
        BarChart chart1 = (BarChart) view.findViewById(R.id.chart1);
        BarChart chart2 = (BarChart) view.findViewById(R.id.chart2);
        LineChart chart3 = (LineChart) view.findViewById(R.id.chart3);

        float[] group1 = {2, 10, 0, 0, 0, 0};
        float[] group2 = {0, 1, 0, 0, 0, 0};
        float[] group3 = {4, 6, 1, 1, 0, 0};

        float[] group4 = {1, 4, 0, 0, 0, 0};
        float[] group5 = {0, 1, 0, 0, 0, 0};
        float[] group6 = {4, 3, 0, 0, 0, 0};

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();
        setChartData(group1, group2, group3, entriesGroup1);
        setChartData(group4, group5, group6, entriesGroup2);


        chartSetting(chart1);
        chartSetting(chart2);

        setChart(chart1, entriesGroup1, "");
        setChart(chart2, entriesGroup2, "");
        setLineChart(chart3);

    }

    private void setChart(BarChart chart1, List<BarEntry> entriesGroup1, String lable) {
        if (chart1.getData() != null &&
                chart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart1.getData().getDataSetByIndex(0);
            set1.setValues(entriesGroup1);
            chart1.getData().notifyDataChanged();
            chart1.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entriesGroup1, lable);
            set1.setStackLabels(new String[]{"及时开工", "未及时开工", "未开工"});
            BarData data = new BarData(set1);
            data.setValueTextSize(10f);
            data.setValueFormatter(new MyValueFormatter());
            data.setBarWidth(0.9f); // set custom bar width
            set1.setColors(new int[]{getMyColor(R.color.js), getMyColor(R.color.wjs), getMyColor(R.color.wkg)});
            chart1.setData(data);
        }
        chart1.animateY(500);
        chart1.getDescription().setEnabled(false);
        chart1.setFitBars(true); // make the x-axis fit exactly all bars
        chart1.invalidate(); // refresh
    }

    private void setChartData(float[] group1, float[] group2, float[] group3, List<BarEntry> entriesGroup1) {
        // fill the lists
        for (int i = 0; i < group1.length; i++) {
            entriesGroup1.add(new BarEntry(i, new float[]{group1[i], group2[i], group3[i]}));
        }
    }

    private void chartSetting(BarChart chart) {
        IAxisValueFormatter xAxisFormatter = new MyAxisValueFormatter();

        //x轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6);
        xAxis.setValueFormatter(xAxisFormatter);

        //左边y轴
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(xAxisFormatter);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        chart.setDrawValueAboveBar(true);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(false);
    }

    /* private void showtable(ArrayList<Result> results) {
         ArrayList<ArrayList<String>> mTableDatas = new ArrayList<>();
         ArrayList<String> strings = new ArrayList<>(Arrays.asList("单位名称", "项目数量", "批复总投资", "完成工作量", "财务支用", "计划工作量", "预算支用", "城投承担", "年份"));
         mTableDatas.add(strings);
         for (Result result : results) {
             ArrayList<String> list = new ArrayList<>();
 //            list.add(result.getBiaotiming());
             list.add(result.getDanweimingcheng());
             list.add(result.getXiangmushu());
             list.add(result.getPifuzongtouzi());
             list.add(result.getZsndlj_wanchenggongzuoliang());
             list.add(result.getZsndl_caiwuzhiyong());
             list.add(result.getBennian_jihuagongzuoliang());
             list.add(result.getBennian_yusuanzhiyong());
             list.add(result.getChengtouchengdan());
             list.add(result.getYear());
             mTableDatas.add(list);
         }

         mLockTableView = new LockTableView(this, parentView, mTableDatas);
         mLockTableView.setLockFristColumn(false) //是否锁定第一列
                 .setLockFristRow(true) //是否锁定第一行
                 .setMaxColumnWidth(100) //列最大宽度
                 .setMinColumnWidth(70) //列最小宽度
                 .setMinRowHeight(20)//行最小高度
                 .setMaxRowHeight(60)//行最大高度
                 .setTextViewSize(16) //单元格字体大小
                 .setFristRowBackGroudColor(R.color.table_head)//表头背景色
                 .setTableHeadTextColor(R.color.beijin)//表头字体颜色
                 .setTableContentTextColor(R.color.border_color)//单元格字体颜色
                 .setNullableString("N/A"); //空值替换值


     }
 */
    private int getMyColor(int color) {
        return getResources().getColor(color);
    }

    private void setLineChart(LineChart chart) {

        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        LimitLine ll1 = new LimitLine(44.16f, "44.16");
        ll1.setLineWidth(1f);
        ll1.setLineColor(getMyColor(R.color.line_red));
        ll1.enableDashedLine(10f, 20f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.addLimitLine(ll1);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6);
        xAxis.setAxisMinimum(-.5f);
        xAxis.setAxisMaximum(6.5f);
        xAxis.setValueFormatter(new MyLineChartValueFormatter());
        setData(chart);
    }

    private void setData(LineChart mChart) {
        float[] array = {50.96f, 42.72f, 46.38f, 100.03f, 33.33f, 23.40f, 53.28f};

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < array.length; i++) {
            values.add(new Entry(i, array[i]));
        }

        LineDataSet set;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set = new LineDataSet(values, "完成百分比（%）");

            set.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
            set.enableDashedHighlightLine(10f, 5f, 0f);
            set.setColor(getMyColor(R.color.line_red));
            set.setCircleColor(getMyColor(R.color.border_color));
            set.setLineWidth(1f);
            set.setCircleRadius(3f);
            set.setDrawCircleHole(true);
            set.setValueTextSize(9f);
            set.setDrawFilled(true);
            set.setFormLineWidth(1f);
            //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set.setFormSize(15.f);
            set.setDrawFilled(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set); // add the datasets

            LineData data = new LineData(dataSets);
            mChart.setData(data);
            mChart.setScaleXEnabled(false);
            mChart.setScaleYEnabled(false);
        }
    }
}
