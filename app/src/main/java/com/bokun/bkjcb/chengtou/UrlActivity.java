package com.bokun.bkjcb.chengtou;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bokun.bkjcb.chengtou.Util.Constants;
import com.bokun.bkjcb.chengtou.Util.L;
import com.bokun.bkjcb.chengtou.Util.NetUtils;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final int PERMISSIONS_REQUEST = 0;
    private long downId;
    private MyReciver reciver;
    private String[] premissions = new String[]{
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.READ_PHONE_STATE
    };
    private String filename;
    private String mimeType;

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
                webView.loadUrl(Constants.HTTP_DERAIL_URL + id + "&type=" + type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                filename = contentDisposition.substring(contentDisposition.lastIndexOf("=") + 1);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                request.setTitle(filename);
                L.i(filename);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setMimeType(mimetype);
                mimeType = mimetype;
                downId = manager.enqueue(request);
                registReciver();
                Toast.makeText(UrlActivity.this, "开始文件下载", Toast.LENGTH_SHORT).show();
            }
        });
        initData();
        loadPage();
        checkPermissions(premissions);
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        name = intent.getExtras().getString("name");
        L.i(id + name);
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
            L.i(Constants.HTTP_DERAIL_URL + id + "&type=" + type);
            webView.loadUrl(Constants.HTTP_DERAIL_URL + id + "&type=" + type);
        } else {
            layout.showError(R.drawable.vector_drawable_error, "", "无网络，请检查后再试", "点击重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadPage();
                }
            });
        }
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSIONS_REQUEST);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    protected boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    class MyReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downId == completeDownloadId) {
                Snackbar.make(spinner, "下载完成", Snackbar.LENGTH_SHORT).setAction("打开", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(Environment.DIRECTORY_DOWNLOADS + File.separator + filename);
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setDataAndType(uri, mimeType);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            UrlActivity.this.startActivity(intent1);
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(UrlActivity.this, "无法打开文件", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void registReciver() {
        if (reciver == null) {
            reciver = new MyReciver();
            this.registerReceiver(reciver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (reciver != null) {
            unregisterReceiver(reciver);
        }
    }
}
