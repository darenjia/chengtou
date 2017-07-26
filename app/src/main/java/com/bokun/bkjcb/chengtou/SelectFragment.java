package com.bokun.bkjcb.chengtou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.chengtou.Adapter.RecAdapter;
import com.bokun.bkjcb.chengtou.Adapter.RecItemCallback;
import com.bokun.bkjcb.chengtou.Domain.Detail;
import com.bokun.bkjcb.chengtou.Event.DefaultEvent;
import com.bokun.bkjcb.chengtou.Http.HttpManager;
import com.bokun.bkjcb.chengtou.Http.HttpRequestVo;
import com.bokun.bkjcb.chengtou.Http.JsonParser;
import com.bokun.bkjcb.chengtou.Http.RequestListener;
import com.bokun.bkjcb.chengtou.Http.XmlParser;
import com.bokun.bkjcb.chengtou.Util.L;
import com.bokun.bkjcb.chengtou.Util.NetUtils;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DengShuai on 2017/7/24.
 */

public class SelectFragment extends Fragment implements RequestListener {

    private ImageView image;
    private EditText text;
    private Button button;
    private RecyclerView recyclerView;
    private ProgressRelativeLayout layout;
    private HttpManager manager;
    private ArrayList<Detail> details;
    private ArrayList<Detail> list;
    private RecAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select, null);
        init(view);
        return view;
    }

    private void init(View view) {

        image = (ImageView) view.findViewById(R.id.select_ic);
        text = (EditText) view.findViewById(R.id.select_text);
        button = (Button) view.findViewById(R.id.select_btn);
        layout = (ProgressRelativeLayout) view.findViewById(R.id.select_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.select_recyclerView);


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
                } else {
                    image.setImageResource(R.mipmap.ic_search);
                    adapter.setData(list);
                    layout.showContent();
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(text.getText().toString())) {
                    text.setText("");
                    image.setImageResource(R.mipmap.ic_search);
                    adapter.setData(list);
                    layout.showContent();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequest();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        sendRequest("");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecAdapter(getContext());
        adapter.setItemClick(new RecItemCallback() {
            @Override
            public void onItemClick(int position, Detail model, int tag, RecAdapter.MyViewHolder holder) {
                L.i("点击" + position);
//                        webView.loadUrl(Constants.GET_DETAIL_URL + details.get(position).getId() + "&type=" + type);
//                        openUrl(model.getXiangmumingcheng(), Constants.GET_DETAIL_URL + details.get(position).getId() + "&type=" + type);
                ToUrl(model.getId(), model.getXiangmumingcheng());
            }

            @Override
            public void onItemLongClick(int position, Detail model, int tag, RecAdapter.MyViewHolder holder) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void startRequest() {
        L.i("发送请求");
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String word = text.getText().toString();
        layout.showLoading();
        sendRequest(word);

    }

    private void sendRequest(final String word) {
        if (!NetUtils.isConnected(getContext())) {
            layout.showError(R.drawable.vector_drawable_error, "", "无网络，请检查后再试", "点击重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest(word);
                }
            });
            return;
        }
        if (manager != null && manager.isRunning()) {
            manager.cancelHttpRequest();
        }
       /* HashMap<String, String> map = new HashMap<>();
        HttpRequestVo requestVo = new HttpRequestVo(map, "Getxinxichaxunleixing");
        manager = new HttpManager(getContext(), this, requestVo);
        manager.postRequest(); */

        HashMap<String, String> map = new HashMap<>();
        map.put("xiangmumingcheng", word);
        HttpRequestVo requestVo = new HttpRequestVo(map, "Getxinxichaxun");
        manager = new HttpManager(getContext(), this, requestVo);
        manager.postRequest();
        layout.showLoading();
    }

    @Override
    public void action(int i, Object object) {
        if (object != null) {
//            L.i(object.toString());
            String s = XmlParser.parseSoapObject((SoapObject) object);
            details = JsonParser.getResultDetail(s);
            if (list == null) {
                list = new ArrayList<>(details);
            }
            EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_SUCCESS));
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DefaultEvent event) {
        if (event.getState_code() == DefaultEvent.GET_DATA_SUCCESS) {
            L.i("O(∩_∩)O~");
            if (details != null && details.size() > 0) {
                layout.showContent();
                adapter.setData(details);
            } else {
                layout.showEmpty(R.drawable.empty, null, "暂无数据");
            }
        } else if (event.getState_code() == DefaultEvent.GET_DATA_NULL) {
            layout.showEmpty(R.drawable.empty, null, "暂无数据");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void ToUrl(String id, String name) {
        Intent intent = new Intent(getActivity(), UrlActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        getContext().startActivity(intent);
    }

}
