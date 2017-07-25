package com.bokun.bkjcb.chengtou;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.chengtou.Event.DefaultEvent;
import com.bokun.bkjcb.chengtou.Http.HttpManager;
import com.bokun.bkjcb.chengtou.Http.HttpRequestVo;
import com.bokun.bkjcb.chengtou.Http.RequestListener;
import com.bokun.bkjcb.chengtou.Util.L;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by DengShuai on 2017/7/24.
 */

public class SelectFragment extends Fragment implements RequestListener {

    private NiceSpinner spinner;
    private ImageView image;
    private EditText text;
    private Button button;
    private RecyclerView recyclerView;
    private ProgressRelativeLayout layout;
    private String[] array;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select, null);
        init(view);
        return view;
    }

    private void init(View view) {
        spinner = (NiceSpinner) view.findViewById(R.id.select_spinner);
        image = (ImageView) view.findViewById(R.id.select_ic);
        text = (EditText) view.findViewById(R.id.select_text);
        button = (Button) view.findViewById(R.id.select_btn);
        layout = (ProgressRelativeLayout) view.findViewById(R.id.select_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.select_recyclerView);
        webView = (WebView) view.findViewById(R.id.select_web);

        array = getResources().getStringArray(R.array.select_items);
        spinner.attachDataSource(Arrays.asList(array));

        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    startRequest();
                    return true;
                }
                return false;
            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    image.setImageResource(R.mipmap.ic_close);
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(text.getText().toString())) {
                    text.setText("");
                    image.setImageResource(R.mipmap.ic_search);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequest();
            }
        });

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
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                layout.showLoading();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void startRequest() {
        String type = array[spinner.getSelectedIndex()];
        String word = text.getText().toString();
        if (TextUtils.isEmpty(word)) {
            recyclerView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl("http://192.168.100.123:8081/JBPM/phone/xinxichaxun?id=fd3e9178a86b4a398eb8bc0e8563f3da");
        } else {
            layout.showLoading();
            sendRequest(type, word);
        }
    }

    private void sendRequest(String type, String word) {
        HashMap<String, String> map = new HashMap<>();
        map.put("", type);
        map.put("xiangmumingcheng", word);
        HttpRequestVo requestVo = new HttpRequestVo(map, "Getxinxichaxun");
        HttpManager manager = new HttpManager(getContext(), this, requestVo);
        manager.postRequest();
    }

    @Override
    public void action(int i, Object object) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DefaultEvent event) {
        if (event.getState_code() == DefaultEvent.GET_DATA_SUCCESS) {
            L.i("O(∩_∩)O~");
            layout.showContent();
        } else if (event.getState_code() == DefaultEvent.GET_DATA_NULL) {
            layout.showEmpty(R.drawable.empty, null, "暂无数据");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
