package com.example.musicplayer.bean;


import android.app.PendingIntent;
import android.net.Uri;
import android.text.TextUtils;

import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.DataUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络音乐的信息，来自百度API，包含来自推荐API推荐歌曲，排行榜API的歌曲的信息
 */
public class Song extends AbstractMusic implements Serializable{
    /**
     * artist_id : 33936615
     * language : 国语
     * pic_big : http://musicdata.baidu.com/data2/pic/39d6703110744fa2ba12a0c7b48feef8/552784968/552784968.jpg@s_1,w_150,h_150
     * pic_small : http://musicdata.baidu.com/data2/pic/39d6703110744fa2ba12a0c7b48feef8/552784968/552784968.jpg@s_1,w_90,h_90
     * country : 内地
     * area : 0
     * publishtime : 2017-08-28
     * album_no : 1
     * lrclink : http://musicdata.baidu.com/data2/lrc/88a344be9a39245537f956ecf820a8e8/552787982/552787982.lrc
     * copy_type : 1
     * hot : 104759
     * all_artist_ting_uid : 51439659
     * resource_type : 0
     * is_new : 1
     * rank_change : 0
     * rank : 1
     * all_artist_id : 33936615
     * style :
     * del_status : 0
     * relate_status : 0
     * toneid : 0
     * all_rate : 64,128,256,320,flac
     * file_duration : 255
     * has_mv_mobile : 0
     * versions :
     * bitrate_fee : {"0":"0|0","1":"-1|-1"}
     * biaoshi : king,first,lossless,vip
     * info :
     * has_filmtv : 0
     * si_proxycompany : 上海丝芭文化传媒有限公司
     * song_id : 552789647
     * title : 醉清风
     * ting_uid : 51439659
     * author : SNH48
     * album_id : 552789645
     * album_title : 醉清风
     * is_first_publish : 0
     * havehigh : 2
     * charge : 0
     * has_mv : 0
     * learn : 0
     * song_source : web
     * piao_id : 0
     * korean_bb_song : 1
     * resource_type_ext : 1
     * mv_provider : 0000000000
     * artist_name : SNH48
     */
    private String artist_id;
    private String all_artist_id;
    private String all_artist_ting_uid;
    private String language;
    private String publishtime;
    private String album_no;
    private String pic_small;
    private String pic_big;
    private String versions;
    private String info;
    private String song_id;
    private String title;
    private String ting_uid;
    private String author;
    private String album_id;
    private String album_title;
    private int has_mv;
    private String song_source;
    private Bitrate bitrate;


    /**
     * 来自search
     */
    private String songname;
    private String songid;
    private String artistname;

    public void setBitrate(Bitrate bitrate){
        this.bitrate = bitrate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String getDataSource() {
        return bitrate != null?bitrate.getFile_link():Constants.MUSIC_PLAY_URL+song_id;
    }

    @Override
    public Integer getDuration() {
        if (bitrate != null){
            return bitrate.getFile_duration()*1000;
        } else {
            return 0;
        }
    }

    @Override
    public String getTitle() {
        if (!TextUtils.isEmpty(title)){
            return title;
        }else {
            return songname;
        }
    }

    @Override
    public String getArtist() {
        if (!TextUtils.isEmpty(author)){
            return author;
        }else {
            return artistname;
        }
    }

    @Override
    public String getAlbum() {
        return album_title;
    }

    @Override
    public Uri getPictureUri() {
        if (pic_big != null){
            return Uri.parse(pic_big);
        } else {
            return null;
        }
    }

    @Override
    public long getSongId() {
        if (!TextUtils.isEmpty(song_id)){
            return Long.valueOf(song_id);
        } else {
            return Long.valueOf(songid);
        }
    }

    public int getHas_mv() {
        return has_mv;
    }

    public void setHas_mv(int has_mv) {
        this.has_mv = has_mv;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public static List<AbstractMusic> getAbstractMusicList(List<Song> songList){
        List<AbstractMusic> list = new ArrayList<>();
        for(Song Song:songList){
            list.add(Song);
        }
        return list;
    }
}
