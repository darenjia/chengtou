package com.bokun.bkjcb.chengtou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;

import com.bokun.bkjcb.chengtou.Util.Constants;
import com.bokun.bkjcb.chengtou.Util.L;
import com.bokun.bkjcb.chengtou.Util.NetUtils;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;

import qiu.niorgai.StatusBarCompat;

/**
 * Created by DengShuai on 2017/7/26.
 */

public class UrlActivity extends AppCompatActivity {

    private NiceSpinner spinner;
    private String[] array;
    private WebView webView;
    private String type = "基本信息";
    private String id;
    private String name;
    private ProgressRelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_url);
        StatusBarCompat.setStatusBarColor(UrlActivity.this, getResources().getColor(R.color.colorPrimary));
        spinner = (NiceSpinner) findViewById(R.id.select_spinner);
        webView = (WebView) findViewById(R.id.url_web);
        layout = (ProgressRelativeLayout) findViewById(R.id.layout_url);
        array = getResources().getStringArray(R.array.select_items);
        spinner.attachDataSource(Arrays.asList(array));
        type = array[spinner.getSelectedIndex()];
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                layout.showContent();
                L.i("页面加载完成");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                layout.showLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                loadError = true;
                layout.showEmpty(R.drawable.empty, null, "网络错误");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                loadError = true;
//                layout.showEmpty(R.drawable.empty, null, "暂无数据");
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long pos) {
                type = array[position];
                webView.loadUrl(Constants.GET_DETAIL_URL + id + "&type=" + type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        initData();
        loadPage();
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        name = intent.getExtras().getString("name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.back_aa);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadPage() {
        if (NetUtils.isConnected(this)) {
            webView.loadUrl(Constants.GET_DETAIL_URL + id + "&type=" + type);
        } else {
            layout.showError(R.drawable.vector_drawable_error, "", "无网络，请检查后再试", "点击重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadPage();
                }
            });
        }
    }
}
