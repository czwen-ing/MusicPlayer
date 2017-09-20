package com.example.musicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.bean.AbstractMusic;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.bean.Song;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MusicUtils {

    public static List<Music> getMusicData(Context context){//获取本地的音乐
        ContentResolver resolver = context.getContentResolver();
        if (resolver != null){
            Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            return cursorToList(cursor,context);
        }
        return null;
    }

    public static List<Music> cursorToList(Cursor cursor, Context context) {//将音乐相关信息以Music输出List
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        List<Music> musicList = new ArrayList<>();
        while(cursor.moveToNext()){
            Music music = new Music();
            String musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            int time = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            int artistId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//获取音乐的保存路径
            String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            //获取音乐格式
            String musicType = fileName.substring(fileName.lastIndexOf(".")+1);
            if ("mp3".equals(musicType) && time > 50000){
                music.setTitle(musicName);
                music.setArtist(artist);
                music.setArtist_id(artistId);
                music.setAlbum_title(album);
                music.setTime(time);
                music.setUrl(url);
                music.setAlbumId(albumId);
                musicList.add(music);
            }
        }
        cursor.close();
        return musicList;
    }

    public static Bitmap getPicture(Context context, long id) {//获取专辑的图片
        ContentResolver resolver = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),
                id);
        try {
            InputStream is = resolver.openInputStream(uri);
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            try {
                ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri,"r");
                if (pfd != null){
                    FileDescriptor fd = pfd.getFileDescriptor();
                    return BitmapFactory.decodeFileDescriptor(fd);
                }
            } catch (Exception e1) {
                return  null;
            }
            return null;
        }
    }

    public static String timeToString(int time){//将事件转换为分秒格式并以字符串返回
        int temp = time/1000;//毫秒化为秒
        int minute = temp/60;
        int second = temp%60;
        return String.format("%02d:%02d",minute,second);
    }

    public static List<Music> getDataFromDB(SQLiteDatabase db){
        List<Music> musics = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from music_tb",null);
        if (cursor == null || cursor.getCount() ==0){
            return musics;
        }
        while (cursor.moveToNext()){
            Music music = new Music();
            music.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            Log.e("TAG","MUSIC TITLE"+music.getTitle());
            music.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
            music.setAlbum_title(cursor.getString(cursor.getColumnIndex("album")));
            music.setAlbumId(cursor.getInt(cursor.getColumnIndex("album_id")));
            music.setTime(cursor.getInt(cursor.getColumnIndex("time")));
            music.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            musics.add(music);
        }
        cursor.close();
        return musics;
    }
}
