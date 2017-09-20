package com.example.musicplayer.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by KKOO on 2017/9/13.
 */

public class OkhttpUtils {
    public static void request(final String url, final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("Content-Type","text/html; charset=utf-8")
                        .addHeader("User-Agent","User-Agent:Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;")
                        .url(url).build();
                Call call = client.newCall(request);
                call.enqueue(callback);
            }
        }).start();
    }
}
