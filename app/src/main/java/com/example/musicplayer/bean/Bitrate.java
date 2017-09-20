package com.example.musicplayer.bean;

import java.io.Serializable;

/**
 * 来自百度API得到的数据，包含播放地址，时长等信息
 */
public class Bitrate implements Serializable{

    /**
     * show_link :
     * free : 1
     * song_file_id : 42783748
     * file_size : 2679447
     * file_extension : mp3
     * file_duration : 322
     * file_bitrate : 64
     * file_link : http://yinyueshiting.baidu.com/data2/music/42783748/42783748.mp3?xcode=a3322fc6bb999ca7e61345ed80ef9cd2
     * hash : 74d926076dc8f2f857ffaa403d79935a65f80dec
     */

    private String show_link;
    private int free;
    private int song_file_id;
    private int file_size;
    private String file_extension;
    private int file_duration;
    private int file_bitrate;
    private String file_link;
    private String hash;

    public String getShow_link() {
        return show_link;
    }

    public void setShow_link(String show_link) {
        this.show_link = show_link;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getSong_file_id() {
        return song_file_id;
    }

    public void setSong_file_id(int song_file_id) {
        this.song_file_id = song_file_id;
    }

    public int getFile_size() {
        return file_size;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public int getFile_duration() {
        return file_duration;
    }

    public void setFile_duration(int file_duration) {
        this.file_duration = file_duration;
    }

    public int getFile_bitrate() {
        return file_bitrate;
    }

    public void setFile_bitrate(int file_bitrate) {
        this.file_bitrate = file_bitrate;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
