package com.example.musicplayer.bean;


import java.util.List;

/**
 * 来自百度推荐API的信息，主要时百度推荐的5个歌曲，得到ResultBean的list歌曲列表
 */
public class RecommendSong {

    /**
     * error_code : 22000
     * result : {"list":[{"artist_id":"87","all_artist_id":"87","all_artist_ting_uid":"1077","language":"国语","publishtime":"2001-07-17","album_no":"3","toneid":"600902000008876101","all_rate":"24,64,128,192,256,320,flac","pic_small":"http://musicdata.baidu.com/data2/pic/c0e4bf6bf170c2fdc7b3c233267520c7/482882/482882.jpg@s_1,w_90,h_90","pic_big":"http://musicdata.baidu.com/data2/pic/c0e4bf6bf170c2fdc7b3c233267520c7/482882/482882.jpg@s_1,w_150,h_150","hot":"6055","has_mv_mobile":1,"versions":"","bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","del_status":"0","info":"","has_filmtv":"0","song_id":"679946","title":"不如这样","ting_uid":"1077","author":"陈奕迅","album_id":"482882","album_title":"反正是我","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":1,"learn":1,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"1100000000","copy_type":"1"},{"artist_id":"15126452","all_artist_id":"15126452","all_artist_ting_uid":"210019436","language":"国语","publishtime":"2014-08-31","album_no":"7","toneid":"0","all_rate":"64,128,256,320,flac","pic_small":"http://musicdata.baidu.com/data2/pic/fe5bd34cce731e6f0261dc15c2e43ad4/288817047/288817047.jpg@s_0,w_90","pic_big":"http://musicdata.baidu.com/data2/pic/fe5bd34cce731e6f0261dc15c2e43ad4/288817047/288817047.jpg@s_0,w_150","hot":"28","has_mv_mobile":0,"versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","del_status":"0","info":"","has_filmtv":"0","song_id":"288405804","title":"没有时间了","ting_uid":"210019436","author":"李晋","album_id":"122663626","album_title":"十月迷城","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":0,"learn":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","copy_type":"1"}]}
     */

    private int error_code;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<Song> list;

        public List<Song> getList() {
            return list;
        }

        public void setList(List<Song> list) {
            this.list = list;
        }
    }
}
