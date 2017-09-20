package com.example.musicplayer.bean;

import android.content.ContentUris;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐的数据，专辑图片，歌手，市场，uri，音乐名，专辑名。继承抽象类的AbstractMusic，便于在播放时要区分网路音乐
 */
public class Music extends AbstractMusic implements Serializable {
    private static final long serialVersionUID = 1;
    private int albumId;//专辑图片
    private String title;//音乐名
    private String artist;//艺术家
    private int artist_id;
    private int time;//音乐总长/ms
    private String album_title;//专辑
    private String url;//url可以是uri，而uri不一定使url

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    @Override
    public String getDataSource() {
        return url;
    }

    @Override
    public Integer getDuration() {
        return time;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }


    public void setTitle(String title) {
        this.title = title;
    }



    @Override
    public String getAlbum() {
        return album_title;
    }

    @Override
    public Uri getPictureUri() {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

    @Override
    public long getSongId() {
        return 0;
    }


    public void setArtist(String artist) {
        this.artist = artist;
    }


    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public static List<AbstractMusic> getAbstractMusicList(List<Music> musicList){
        List<AbstractMusic> list = new ArrayList<>();
        for(Music music:musicList){
            list.add(music);
        }
        return list;
    }

}
