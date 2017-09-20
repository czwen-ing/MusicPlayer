package com.example.musicplayer.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * 抽象音乐类，音乐类型有本地音乐和网络音乐，这个类使得两种类型的音乐不用特别区分去显示和播放
 */
public abstract class AbstractMusic implements Serializable{
    public abstract String getDataSource();

    public abstract Integer getDuration();

    public abstract String getTitle();

    public abstract String getArtist();

    public abstract String getAlbum();

    public abstract Uri getPictureUri();

    public abstract long getSongId();

}
