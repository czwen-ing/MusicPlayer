package com.example.musicplayer.utils;


import com.example.musicplayer.R;

import java.util.HashMap;
import java.util.Map;

public class DataUtils {

    public static Map<Integer,Integer> images = new HashMap<>();
    static {
        images.put(1,R.drawable.new_songs);
        images.put(2,R.drawable.hot_songs);
        images.put(11,R.drawable.rock_songs);
        images.put(22,R.drawable.classical_songs);
        images.put(21,R.drawable.ea_songs);
        images.put(24,R.drawable.film_songs);
        images.put(23,R.drawable.love_songs);
        images.put(25,R.drawable.network_songs);
    }

    public static final int[] IMAGE = new int[]{
            R.drawable.music,
            R.drawable.new_songs,
            R.drawable.hot_songs,
            R.drawable.rock_songs,
            R.drawable.classical_songs,
            R.drawable.ea_songs,
            R.drawable.film_songs,
            R.drawable.love_songs,
            R.drawable.network_songs
    };

    public static int getImage(int type){
        return images.get(type);
    }
}
