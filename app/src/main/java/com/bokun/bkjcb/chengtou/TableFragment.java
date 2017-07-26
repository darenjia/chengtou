package com.bokun.bkjcb.chengtou;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bokun.bkjcb.chengtou.Event.DefaultEvent;
import com.bokun.bkjcb.chengtou.Http.HttpManager;
import com.bokun.bkjcb.chengtou.Http.HttpRequestVo;
import com.bokun.bkjcb.chengtou.Http.JsonParser;
import com.bokun.bkjcb.chengtou.Http.RequestListener;
import com.bokun.bkjcb.chengtou.Http.XmlParser;
import com.bokun.bkjcb.chengtou.Util.Constants;
import com.bokun.bkjcb.chengtou.Util.L;
import com.bokun.bkjcb.chengtou.Util.NetUtils;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class TableFragment extends Fragment implements View.OnClickListener, RequestListener {
    private WebView webView;
    private NiceSpinner spinner;
    private NiceSpinner spinner1;
    private ArrayAdapter<String> adapter;
    private ProgressBar bar;
    private String[] array;
    private TextView textView;
    private ProgressRelativeLayout layout, layout1;
    private String url;
    private ArrayList<String> results;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, null);
        webView = (WebView) view.findViewById(R.id.web);
        spinner = (NiceSpinner) view.findViewById(R.id.xiangmu);
        spinner1 = (NiceSpinner) view.findViewById(R.id.year);
        bar = (ProgressBar) view.findViewById(R.id.progress);
        textView = (TextView) view.findViewById(R.id.on);
        layout = (ProgressRelativeLayout) view.findViewById(R.id.progressLayout);
        layout1 = (ProgressRelativeLayout) view.findViewById(R.id.progressLayout1);

        textView.setText("不可查看");
        textView.setBackgroundColor(getResources().getColor(R.color.l_black));
        textView.setClickable(false);
        getNetState(true);
        textView.setOnClickListener(this);
        initWebView();
        return view;
    }

    private void initWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                layout1.showLoading(Arrays.asList(R.id.title, R.id.spinner, R.id.on));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                layout1.showContent();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                layout.showEmpty(R.drawable.empty, null, "网络错误",Arrays.asList(R.id.title, R.id.spinner, R.id.on));
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                layout.showEmpty(R.drawable.empty, null, "暂无数据",Arrays.asList(R.id.title, R.id.spinner, R.id.on));
            }
        });
    }

    @Override
    public void onClick(View v) {
//        String[] years = getResources().getStringArray(R.array.year);
        if (results == null || results.size() == 0) {
            return;
        }
        String year = results.get(spinner1.getSelectedIndex());
        String mc = array[spinner.getSelectedIndex()];
        url = Constants.GET_TABLT_DATA_URL + mc + "&year=" + year;
        L.i(url);
        getNetState(false);
//        webView.loadUrl(url);
    }

    private void getData() {
        array = getResources().getStringArray(R.array.xuanxiang);
        spinner.attachDataSource(Arrays.asList(array));
        HashMap<String, String> map = new HashMap<>();
        HttpRequestVo requestVo = new HttpRequestVo(map, "Getyear");
        HttpManager manager = new HttpManager(getContext(), this, requestVo);
        manager.postRequest();

    }

    @Override
    public void action(int i, Object object) {
        String s = XmlParser.parseSoapObject((SoapObject) object);
        results = JsonParser.getYear(s);
        if (results == null || results.size() == 0) {
            EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
            return;
        }
        boolean b = EventBus.getDefault().isRegistered(this);
        if (!b) {
            EventBus.getDefault().register(this);
        }
        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_SUCCESS));
        L.i(results.size() + "获取到年份" + b);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DefaultEvent event) {
        if (event.getState_code() == DefaultEvent.GET_DATA_SUCCESS) {
//            spinner1.setAdapter(adapter);
            spinner1.attachDataSource(results);
            spinner1.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
            textView.setText("查看");
            textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            textView.setClickable(true);
        } else if (event.getState_code() == DefaultEvent.GET_DATA_NULL) {
            textView.setText("不可查看");
            textView.setBackgroundColor(getResources().getColor(R.color.l_black));
            textView.setClickable(false);
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

    private void getNetState(final boolean flag) {
        if (NetUtils.isConnected(getContext())) {
            layout.showContent();
            if (flag) {
                getData();
            } else {
                webView.loadUrl(url);
            }
        } else {
//            textView.setText("不可查看");
//            textView.setBackgroundColor(getResources().getColor(R.color.l_black));
//            textView.setClickable(false);
            if (flag) {
                layout.showError(R.drawable.vector_drawable_error, "", "无网络，请检查后再试", "点击重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNetState(flag);
                    }
                });
            } else {
                layout1.showError(R.drawable.vector_drawable_error, "", "无网络，请检查后再试", "点击重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNetState(flag);
                    }
                });
            }
        }
    }
}
