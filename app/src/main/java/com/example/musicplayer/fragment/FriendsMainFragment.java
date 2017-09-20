package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.utils.CacheUtils;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.bean.FriendsInfo;
import com.example.musicplayer.adapter.FriendsListAdapter;
import com.example.musicplayer.R;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FriendsMainFragment extends Fragment {
    public static final String TAG = "FriendsMainFragment";

    ListView listView;
    TwinklingRefreshLayout refreshLayout;
    ProgressBar progressBar;
    TextView textNoNet;
    List<FriendsInfo.ListBean> list;
    FriendsListAdapter adapter;
    private boolean isLoadMore = false;
    private String lastItemId;
    private static int count = 20;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        initView(view);
        initData();
        return view;
    }


    private void initData() {
        getDataFromNet();

    }

    private void getDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Constants.getMoreURL(count)).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, "联网失败,请检查网络" + e.getMessage());
                        String data = CacheUtils.getData(getContext(),"friends","");
                        setData(data);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String data = response.body().string();
                        setData(data);
                        CacheUtils.saveData(getContext(),"friends",data);
                    }
                });

            }
        }).start();

    }

    private List<FriendsInfo.ListBean> parseJson(String data) {
        FriendsInfo friendsInfo = new Gson().fromJson(data, FriendsInfo.class);
        return friendsInfo.getList();
    }

    private void setData(final String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.clear();
                list.addAll(parseJson(data));
                if (!isLoadMore) {
                    if (list != null && list.size() > 0) {
                        textNoNet.setVisibility(View.GONE);
                        lastItemId = list.get(list.size() - 1).getId();
                        adapter.notifyDataSetChanged();
                    } else {
                        textNoNet.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setEnableRefresh(true);
                } else {
                    isLoadMore = false;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.friends_list_view);
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        textNoNet = (TextView) view.findViewById(R.id.tv_nonet);
        refreshLayout.setEnableRefresh(false);
        list = new ArrayList<>();
        adapter = new FriendsListAdapter(getContext(), list);
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                getDataFromNet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                getMoreDataFromNet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
    }

    public void getMoreDataFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoadMore = true;
                if (count < 90) {
                    count += 10;
                } else {
                    count = 99;
                }
                OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder().url(Constants.getMoreURL(count)).build();
                try {
                    Response response = client.newCall(request).execute();
                    String data = response.body().string();
                    setData(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
