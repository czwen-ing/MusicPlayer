package com.example.musicplayer.utils;

import com.example.musicplayer.bean.AbstractMusic;
import com.example.musicplayer.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static List<AbstractMusic> musicList = new ArrayList<>();//所有音乐列表
    public static List<AbstractMusic> collectList = new ArrayList<>();//收藏列表
    public static List<Music> itemMusics = new ArrayList<>();
    public static final int PLAY = 7;//播放
    public static final int PAUSE = 8;//暂停
    public static final int NEW = 6;//新歌曲

    public static final int LOOP =0;//顺序循环
    public static final int ONE = 1;//单曲循环
    public static final int RANDOM = 2;//随机播放
    public static final int OVER_FINISH = 3;//结束后停止
    public static final int NEXT = 3;//下一首
    public static final int PREVIOUS = 4;//上一首
    public static int PLAY_TYPE = LOOP;//循环播放的类型

    public static final String CONTROL_ACTION = "com.example.musicplayer.control";//播放暂停的Intent动作
    public static final String UPDATE_NET = "com.example.musicplayer.net";//用于存放音乐的列表，播放方式，开始暂停，动作等的常量值
    public static final String UPDATE_ACTION = "com.example.musicplayer.update";//更新进度
    public static final String SEEKBAR_ACTION = "com.example.musicplayer.seekbar";//进度条的进度动作
    public static final String COMPLETE_ACTION = "com.example.musicplayer.complete";//音乐结束的动作
    public static final String UPDATE_STYLE="com.example.musicplayer.style";//更新播放形式
    public static final String UPDATE_BUFFER="com.example.musicplayer.buffer";//更新播放形式

    public static final String ACTION_ACTIVITY = "com.example.music_play";

    public static final int ALL_MUSIC = 0;
    public static final int COLLECTION_MUSIC = 1;

    public static final String FRIEND_URL = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";


    public static String getMoreURL(int count){
        String net = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-";
        String str = ".json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";
        return net + count + str;
    }

    public  static final String MUSIC_URI = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&";

    public static String getMusicListURL(int type ,int size ,int offset){
        return MUSIC_URI +  "method=baidu.ting.billboard.billList&type=" + type
                + "&size=" + size + "&offset=" + offset;
    }

    public static String getMusicListURL(int type ,int size ){
        return getMusicListURL(type,size,0);
    }

    public static final String MUSIC_LIST = "method=baidu.ting.billboard.billList&type=1&size=10&offset=0";

    public static final String MUSIC_SEARCH_URL = MUSIC_URI + "method=baidu.ting.search.catalogSug&query=";

    public static final String MUSIC_PLAY_URL = MUSIC_URI + "method=baidu.ting.song.play&songid=";

    public static final String MUSIC_RECOMMEND_URL = MUSIC_URI + "method=baidu.ting.song.getRecommandSongList&song_id=877578&num=5";

    public static final String DEFAULT_RECOMMEND = "{\"error_code\":22000,\"result\":{\"list\":[{\"artist_id\":\"64279851\",\"all_artist_id\":\"64279851\",\"all_artist_ting_uid\":\"92456597\",\"language\":\"\\u56fd\\u8bed\",\"publishtime\":\"2016-06-30\",\"album_no\":\"1\",\"toneid\":\"0\",\"all_rate\":\"64,128,256,320,flac\",\"pic_small\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/20993d5f05c2c5dba256a129ec3b60d7\\/267160903\\/267160903.jpg@s_0,w_90\",\"pic_big\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/20993d5f05c2c5dba256a129ec3b60d7\\/267160903\\/267160903.jpg@s_0,w_150\",\"hot\":\"1844\",\"has_mv_mobile\":0,\"versions\":\"\",\"bitrate_fee\":\"{\\\"0\\\":\\\"0|0\\\",\\\"1\\\":\\\"0|0\\\"}\",\"del_status\":\"0\",\"info\":\"\",\"has_filmtv\":\"0\",\"song_id\":\"267160928\",\"title\":\"\\u9020\\u7269\\u8005\",\"ting_uid\":\"92456597\",\"author\":\"\\u534e\\u6668\\u5b87\",\"album_id\":\"267160902\",\"album_title\":\"\\u9020\\u7269\\u8005\",\"is_first_publish\":0,\"havehigh\":2,\"charge\":0,\"has_mv\":0,\"learn\":0,\"song_source\":\"web\",\"piao_id\":\"0\",\"korean_bb_song\":\"0\",\"resource_type_ext\":\"0\",\"mv_provider\":\"0000000000\",\"copy_type\":\"1\"},{\"artist_id\":\"27470809\",\"all_artist_id\":\"27470809\",\"all_artist_ting_uid\":\"152384430\",\"language\":\"\\u82f1\\u8bed\",\"publishtime\":\"2016-04-11\",\"album_no\":\"0\",\"toneid\":\"0\",\"all_rate\":\"64,128,256,320,flac\",\"pic_small\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/205e1e4a38950fe27c43b6f34738d690\\/545962308\\/545962308.jpg@s_0,w_90\",\"pic_big\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/205e1e4a38950fe27c43b6f34738d690\\/545962308\\/545962308.jpg@s_0,w_150\",\"hot\":\"576\",\"has_mv_mobile\":0,\"versions\":\"\",\"bitrate_fee\":\"{\\\"0\\\":\\\"0|0\\\",\\\"1\\\":\\\"0|0\\\"}\",\"del_status\":\"0\",\"info\":\"\",\"has_filmtv\":\"0\",\"song_id\":\"545962712\",\"title\":\"In The Morning\",\"ting_uid\":\"152384430\",\"author\":\"ZHU\",\"album_id\":\"545962708\",\"album_title\":\"In The Morning\",\"is_first_publish\":0,\"havehigh\":2,\"charge\":0,\"has_mv\":0,\"learn\":0,\"song_source\":\"web\",\"piao_id\":\"0\",\"korean_bb_song\":\"0\",\"resource_type_ext\":\"0\",\"mv_provider\":\"0000000000\",\"copy_type\":\"1\"},{\"artist_id\":\"3480\",\"all_artist_id\":\"3480\",\"all_artist_ting_uid\":\"2673\",\"language\":\"\\u56fd\\u8bed\",\"publishtime\":\"2001-01-08\",\"album_no\":\"1\",\"toneid\":\"0\",\"all_rate\":\"24,64,128,192,256,320,flac\",\"pic_small\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/music\\/096B45413D4B4EEFA3E31E20DC5E57E8\\/253379443\\/253379443.jpg@s_0,w_90\",\"pic_big\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/music\\/096B45413D4B4EEFA3E31E20DC5E57E8\\/253379443\\/253379443.jpg@s_0,w_150\",\"hot\":\"724\",\"has_mv_mobile\":1,\"versions\":\"\",\"bitrate_fee\":\"{\\\"0\\\":\\\"0|0\\\",\\\"1\\\":\\\"0|0\\\"}\",\"del_status\":\"0\",\"info\":\"\",\"has_filmtv\":\"0\",\"song_id\":\"21254928\",\"title\":\"\\u8d85\\u8d8a\\u68a6\\u60f3\",\"ting_uid\":\"2673\",\"author\":\"\\u6c6a\\u6b63\\u6b63\",\"album_id\":\"21254910\",\"album_title\":\"\\u8d85\\u8d8a\\u68a6\\u60f3 \",\"is_first_publish\":0,\"havehigh\":2,\"charge\":0,\"has_mv\":1,\"learn\":0,\"song_source\":\"web\",\"piao_id\":\"0\",\"korean_bb_song\":\"0\",\"resource_type_ext\":\"0\",\"mv_provider\":\"0100000000\",\"copy_type\":\"1\"},{\"artist_id\":\"38113\",\"all_artist_id\":\"38113\",\"all_artist_ting_uid\":\"2847\",\"language\":\"\\u82f1\\u8bed\",\"publishtime\":\"2010-02-10\",\"album_no\":\"1\",\"toneid\":\"0\",\"all_rate\":\"24,64,128,192,256,320\",\"pic_small\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/music\\/4891F97609596932A6486F5FA8AFB036\\/252527142\\/252527142.jpg@s_0,w_90\",\"pic_big\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/music\\/4891F97609596932A6486F5FA8AFB036\\/252527142\\/252527142.jpg@s_0,w_150\",\"hot\":\"27\",\"has_mv_mobile\":0,\"versions\":\"\",\"bitrate_fee\":\"{\\\"0\\\":\\\"129|-1\\\",\\\"1\\\":\\\"-1|-1\\\"}\",\"del_status\":\"0\",\"info\":\"\",\"has_filmtv\":\"0\",\"song_id\":\"7455836\",\"title\":\"I Walk The Line\",\"ting_uid\":\"2847\",\"author\":\"Johnny Cash\",\"album_id\":\"7399748\",\"album_title\":\"Wanted Man: The Johnny Cash Collection\",\"is_first_publish\":0,\"havehigh\":2,\"charge\":0,\"has_mv\":0,\"learn\":0,\"song_source\":\"web\",\"piao_id\":\"0\",\"korean_bb_song\":\"0\",\"resource_type_ext\":\"0\",\"mv_provider\":\"0000000000\",\"copy_type\":\"1\"},{\"artist_id\":\"913\",\"all_artist_id\":\"913\",\"all_artist_ting_uid\":\"1420\",\"language\":\"\\u7ca4\\u8bed\",\"publishtime\":\"0000-00-00\",\"album_no\":\"4\",\"toneid\":\"600902000008880509\",\"all_rate\":\"24,64,128,192,256,320,flac\",\"pic_small\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/2b45379deb7abff2512e92cd1d940668\\/261950870\\/261950870.jpg@s_0,w_90\",\"pic_big\":\"http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/2b45379deb7abff2512e92cd1d940668\\/261950870\\/261950870.jpg@s_0,w_150\",\"hot\":\"9\",\"has_mv_mobile\":0,\"versions\":\"\",\"bitrate_fee\":\"{\\\"0\\\":\\\"129|-1\\\",\\\"1\\\":\\\"-1|-1\\\"}\",\"del_status\":\"0\",\"info\":\"\",\"has_filmtv\":\"0\",\"song_id\":\"23203917\",\"title\":\"\\u649e\\u65e5\\u5206\\u624b\",\"ting_uid\":\"1420\",\"author\":\"\\u6d2a\\u5353\\u7acb\",\"album_id\":\"107934\",\"album_title\":\"Precious Moment\",\"is_first_publish\":0,\"havehigh\":2,\"charge\":0,\"has_mv\":0,\"learn\":0,\"song_source\":\"web\",\"piao_id\":\"0\",\"korean_bb_song\":\"0\",\"resource_type_ext\":\"0\",\"mv_provider\":\"0000000000\",\"copy_type\":\"1\"}]}}";

    public static List<AbstractMusic> list = new ArrayList<>();
}
