package com.example.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by KKOO on 2017/9/13.
 */

public class CacheUtils {


    public static void saveData(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }

    public static String getData(Context context,String key,String defaultValue){
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }
}
