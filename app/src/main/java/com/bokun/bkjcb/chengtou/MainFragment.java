package com.bokun.bkjcb.chengtou;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.chengtou.ChartUtil.MyAxisValueFormatter;
import com.bokun.bkjcb.chengtou.ChartUtil.MyLineChartValueFormatter;
import com.bokun.bkjcb.chengtou.ChartUtil.MyLineValueFormatter;
import com.bokun.bkjcb.chengtou.ChartUtil.MyValueFormatter;
import com.bokun.bkjcb.chengtou.Domain.TableResult;
import com.bokun.bkjcb.chengtou.Domain.TableResultZD;
import com.bokun.bkjcb.chengtou.Domain.TableResultZDXM;
import com.bokun.bkjcb.chengtou.Event.DefaultEvent;
import com.bokun.bkjcb.chengtou.Http.HttpManager;
import com.bokun.bkjcb.chengtou.Http.HttpRequestVo;
import com.bokun.bkjcb.chengtou.Http.JsonParser;
import com.bokun.bkjcb.chengtou.Http.RequestListener;
import com.bokun.bkjcb.chengtou.Http.XmlParser;
import com.bokun.bkjcb.chengtou.Util.CacheUtil;
import com.bokun.bkjcb.chengtou.Util.L;
import com.bokun.bkjcb.chengtou.Util.NetUtils;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class MainFragment extends Fragment {
    BarDataSet set1;
    private BarChart chart1;
    private ArrayList<TableResult> results1;
    private ArrayList<TableResultZD> results2;
    private ArrayList<TableResultZDXM> results3;
    private BarChart chart2;
    private LineChart chart3;
    private String str_data1;
    private String str_data2;
    private String str_data3;
    private TextView title1, title2, title3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        init(view);
        getDataFromCache();
        return view;
    }

    private void init(View view) {

//        parentView = (LinearLayout) view.findViewById(R.id.parent_view);
        chart1 = (BarChart) view.findViewById(R.id.chart1);
        chart2 = (BarChart) view.findViewById(R.id.chart2);
        chart3 = (LineChart) view.findViewById(R.id.chart3);
        title1 = (TextView) view.findViewById(R.id.title1);
        title2 = (TextView) view.findViewById(R.id.title2);
        title3 = (TextView) view.findViewById(R.id.title3);

        chartSetting(chart1);
        chartSetting(chart2);
    }

    private void setChart(BarChart chart, List<BarEntry> entriesGroup1, String lable) {
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(entriesGroup1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entriesGroup1, lable);
            set1.setStackLabels(new String[]{"及时开工", "未及时开工", "未开工"});
            BarData data = new BarData(set1);
            data.setValueTextSize(10f);
            data.setValueFormatter(new MyValueFormatter());
            data.setBarWidth(0.9f); // set custom bar width
            set1.setColors(new int[]{getMyColor(R.color.js), getMyColor(R.color.wjs), getMyColor(R.color.wkg)});
            chart.setData(data);
        }
        chart.animateY(500);
        chart.getDescription().setEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
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

    private int getMyColor(int color) {
        return getResources().getColor(color);
    }

    private void setLineChart(LineChart chart, TableResultZDXM result) {

        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        LimitLine ll = new LimitLine(Float.valueOf(result.getWanchengqingkuangbaifenbi().replace("%", "")), result.getWanchengqingkuangbaifenbi());
        ll.setLineWidth(1f);
        ll.setLineColor(getMyColor(R.color.line_red));
        ll.enableDashedLine(10f, 20f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.setTextSize(10f);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.addLimitLine(ll);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6);
        xAxis.setAxisMinimum(-.5f);
        xAxis.setAxisMaximum(6.5f);
        xAxis.setValueFormatter(new MyLineChartValueFormatter());
        chart.invalidate();
    }

    private void setData(LineChart mChart, ArrayList<TableResultZDXM> list) {
        ArrayList<Entry> values = new ArrayList<>();
        int flag = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            TableResultZDXM result = list.get(i);
            flag = getLineFlag(result.getLeixing());
            values.add(new Entry(flag, Float.valueOf(result.getWanchengqingkuangbaifenbi().replace("%", ""))));
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
            set.setCircleColor(getMyColor(R.color.font_color));
            set.setLineWidth(1f);
            set.setCircleRadius(3f);
            set.setDrawCircleHole(true);
            set.setValueTextSize(9f);
            set.setValueFormatter(new MyLineValueFormatter());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DefaultEvent event) {
        if (event.getState_code() == DefaultEvent.GET_DATA_NULL) {
            Snackbar.make(chart1, "无网络连接，请检查网络", Snackbar.LENGTH_LONG).setAction("设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    getContext().startActivity(intent);
                }
            }).show();
        } else {
            if (event.getType() == 0) {
                List<BarEntry> entriesGroup = new ArrayList<>();
                setChartData(results1, entriesGroup);
                setChart(chart1, entriesGroup, "");
                title1.setText(results1.get(0).getYear() + "年全部项目及时率");
                Toast.makeText(getContext(), "图表一数据已更新", Toast.LENGTH_SHORT).show();
            } else if (event.getType() == 1) {
                List<BarEntry> entriesGroup = new ArrayList<>();
                setChartDataZD(results2, entriesGroup);
                setChart(chart2, entriesGroup, "");
                title2.setText(results2.get(0).getYear() + "年重大项目及时率");
                Toast.makeText(getContext(), "图表二数据已更新", Toast.LENGTH_SHORT).show();
            } else {
                setData(chart3, results3);
                setLineChart(chart3, results3.get(results3.size() - 1));
                Toast.makeText(getContext(), "图表三数据已更新", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getData(String type, RequestListener listener) {
        HashMap<String, String> map = new HashMap<>();
        HttpRequestVo requestVo = new HttpRequestVo(map, type);
        HttpManager manager = new HttpManager(getContext(), listener, requestVo);
        manager.postRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetUtils.isConnected(getContext())) {
            getData("Getjishilv", new RequestListener() {
                @Override
                public void action(int i, Object object) {
                    String s = XmlParser.parseSoapObject((SoapObject) object);
                    if (str_data1 != null && str_data1.equals(s)) {
                        return;
                    }
                    saveData(s, "data1");
                    results1 = JsonParser.getTableData(s);
                    L.i(results1.size());
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_SUCCESS, 0));
                }
            });
            getData("Getzhongdajishilv ", new RequestListener() {
                @Override
                public void action(int i, Object object) {
                    String s = XmlParser.parseSoapObject((SoapObject) object);
                    if (str_data2 != null && str_data2.equals(s)) {
                        return;
                    }
                    saveData(s, "data2");
                    results2 = JsonParser.getTableDataZD(s);
                    L.i(results2.size());
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_SUCCESS, 1));
                }
            });
            getData("Getzhongdaxiangmuwanchengqingkuang ", new RequestListener() {
                @Override
                public void action(int i, Object object) {
                    String s = XmlParser.parseSoapObject((SoapObject) object);
                    if (str_data3 != null && str_data3.equals(s)) {
                        return;
                    }
                    saveData(s, "data3");
                    results3 = JsonParser.getTableDataZDXM(s);
                    L.i(results3.size());
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_SUCCESS, 2));
                }
            });
        } /*else {

        }*/
    }

    private void setChartData(ArrayList<TableResult> data1, List<BarEntry> entriesGroup) {
        TableResult result = null;
        int flag = 0;
        // fill the lists
        for (int i = 0; i < data1.size(); i++) {
            result = data1.get(i);
            flag = getFlag(result.getSuoshubankuai());
            entriesGroup.add(new BarEntry(flag, new float[]{result.getJihuashu(), result.getWeijishikaigong(), result.getWeikaigong()}));
        }
    }

    private void setChartDataZD(ArrayList<TableResultZD> data, List<BarEntry> entriesGroup) {
        TableResultZD result = null;
        int flag = 0;
        for (int i = 0; i < data.size(); i++) {
            result = data.get(i);
            flag = getFlag(result.getSuoshubankuai());
            entriesGroup.add(new BarEntry(flag, new float[]{result.getZhongdajishi(), result.getZhongdaweijishi(), result.getZhongdaweikai()}));
        }
    }

    private int getFlag(String s) {
        int flag;
        switch (s) {
            case "路桥":
                flag = 0;
                break;
            case "水务":
                flag = 1;
                break;
            case "置地":
                flag = 2;
                break;
            case "环境":
                flag = 3;
                break;
            case "其它":
                flag = 4;
                break;
            case "航运":
                flag = 5;
                break;
            default:
                flag = 6;
                break;

        }
        return flag;
    }


    private int getLineFlag(String s) {
        int flag;
        switch (s) {
            case "道路":
                flag = 0;
                break;
            case "公路":
                flag = 1;
                break;
            case "内河航道":
                flag = 2;
                break;
            case "原水":
                flag = 3;
                break;
            case "上水":
                flag = 4;
                break;
            case "雨污水":
                flag = 5;
                break;
            case "固废":
                flag = 6;
                break;
            default:
                flag = 7;
                break;

        }
        return flag;
    }

    private void getDataFromCache() {
        CacheUtil cacheUtil = new CacheUtil();
        str_data1 = cacheUtil.getData("data1");
        str_data2 = cacheUtil.getData("data2");
        str_data3 = cacheUtil.getData("data3");
        cacheUtil.close();

        if (str_data1 != null) {
            results1 = JsonParser.getTableData(str_data1);
            List<BarEntry> entriesGroup = new ArrayList<>();
            setChartData(results1, entriesGroup);
            setChart(chart1, entriesGroup, "");
            title1.setText(results1.get(0).getYear() + "年全部项目及时率");
        }
        if (str_data2 != null) {
            results2 = JsonParser.getTableDataZD(str_data2);
            List<BarEntry> entriesGroup1 = new ArrayList<>();
            setChartDataZD(results2, entriesGroup1);
            setChart(chart2, entriesGroup1, "");
            title2.setText(results2.get(0).getYear() + "年重大项目及时率");
        }
        if (str_data3 != null) {
            results3 = JsonParser.getTableDataZDXM(str_data3);
            setData(chart3, results3);
            setLineChart(chart3, results3.get(results3.size() - 1));
        }
    }

    private void saveData(String content, String name) {
        CacheUtil cacheUtil = new CacheUtil();
        cacheUtil.saveData(name, content);
        cacheUtil.close();
    }

    private void setTitle(TextView tv, String title) {
        tv.setText(title);
    }
}
