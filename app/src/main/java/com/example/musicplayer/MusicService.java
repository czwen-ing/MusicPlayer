package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.musicplayer.bean.AbstractMusic;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.bean.SongInfo;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.OkhttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MusicService extends Service implements MediaPlayer.OnBufferingUpdateListener{
    public static final String TAG = "MusicService";
    private MediaPlayer mediaPlayer;
    private ActivityReceiver receiver;
    private List<AbstractMusic> musicList;
    private AbstractMusic currentMusic;
    private Timer timer;
    int currentPosition;
    private int playType;
    private boolean fromNet;
    public static final int MUSIC_PLAY = 1;
    private int duration;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MUSIC_PLAY:
                    AbstractMusic music = (AbstractMusic) msg.obj;
                    duration = music.getDuration();
                    updateMusicView(music);
                    playMusic(music);
                    break;
                default:
                    break;
            }
        }
    };

    private void updateMusicView(AbstractMusic music) {
        Intent updateIntent = new Intent(Constants.UPDATE_NET);
        updateIntent.putExtra("duration", music.getDuration());
        updateIntent.putExtra("picture_uri",music.getPictureUri().toString());
        sendBroadcast(updateIntent);
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        receiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CONTROL_ACTION);
        filter.addAction(Constants.SEEKBAR_ACTION);
        registerReceiver(receiver, filter);
        musicList = Constants.musicList;
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Intent intent = new Intent(Constants.UPDATE_BUFFER);
        intent.putExtra("bufferTime",percent);
        sendBroadcast(intent);
    }

    private class ActivityReceiver extends BroadcastReceiver {//接收来自音乐播放活动的广播

        @Override
        public void onReceive(Context context, Intent intent) {
            playType = intent.getIntExtra("playType", Constants.PLAY_TYPE);
            if (intent.getAction() == (Constants.CONTROL_ACTION)) {
                int isNew = intent.getIntExtra("new", -1);
                if (isNew != -1) {
                    musicList = Constants.musicList;
                    currentPosition = intent.getIntExtra("position", 0);
                    fromNet = intent.getBooleanExtra("online",false);
                    currentMusic = musicList.get(currentPosition);
                    prepareMusic(currentMusic);
                } else {
                    int control = intent.getIntExtra("control", -1);
                    if (control == Constants.PLAY) {
                        mediaPlayer.start();
                        startTimer();//开始定时发送更新进度广播
                    } else if (control == Constants.PAUSE) {
                        mediaPlayer.pause();
                        stopTimer();//暂停定时
                    }
                }
            } else if (intent.getAction() == Constants.SEEKBAR_ACTION) {//进度条的变化
                int progress = intent.getIntExtra("progress", 0);
                int position = (int) (duration * progress * 1.0 / 100);
                mediaPlayer.seekTo(position);
            }
        }
    }

    private void startTimer() {//开始定时发送广播使播放页面的进度条更新
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(Constants.UPDATE_ACTION);
                updateIntent.putExtra("position", mediaPlayer.getCurrentPosition());
                sendBroadcast(updateIntent);
            }
        }, 0, 1000);
    }

    public void stopTimer(){
        timer.cancel();
    }

    private void prepareMusic(AbstractMusic music) {//准备并播放音乐，进入页面默认播放
        duration = music.getDuration();
        if (fromNet){
            mediaPlayer.setOnBufferingUpdateListener(this);
            querySongFromNet(music);
        } else {
            playMusic(music);
        }
    }

    private void playMusic(AbstractMusic music) {
        try {
            mediaPlayer.reset();//必须重置播放器，否则会出现多个播放项即多个声音出现
            mediaPlayer.setDataSource(music.getDataSource());//url可以是uri，而uri不一定使url
            mediaPlayer.prepare();
            mediaPlayer.start();
            startTimer();
            sendNotification();
            saveInfo();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (playType == Constants.LOOP){
                        currentPosition = currentPosition + 1;
                        if (currentPosition == musicList.size()){
                            currentPosition = 0;
                        }
                    } else if (playType == Constants.RANDOM){
                        currentPosition = new Random().nextInt(musicList.size());
                    }
                    currentMusic = musicList.get(currentPosition);
                    prepareMusic(currentMusic);
                    Intent intent = new Intent(Constants.COMPLETE_ACTION);
                    intent.putExtra("position",currentPosition);
                    sendBroadcast(intent);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    currentPosition = currentPosition + 1;
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void querySongFromNet(AbstractMusic music) {
        if (fromNet){
            OkhttpUtils.request(Constants.MUSIC_PLAY_URL + music.getSongId(),
                    new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.e(TAG, "联网失败");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String data = response.body().string();
                            if (data != null) {
                                SongInfo songInfo = new Gson().fromJson(data, SongInfo.class);
                                Song song = songInfo.getSong();
                                song.setBitrate(songInfo.getBitrate());
                                Message msg = new Message();
                                msg.obj = song;
                                msg.what = MUSIC_PLAY;
                                handler.sendMessage(msg);
                            }
                        }
                    });

        }
    }

    private void sendNotification() {
        NotificationManager notificationManager=(NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);//获取通知服务器
        Notification.Builder builder=new Notification.Builder(this);//通知构建器
        builder.setAutoCancel(false);//打开通知后自动消除为false
        builder.setTicker("音乐播放");//第一次出现时，显示在状态栏的通知提示信息
        builder.setSmallIcon(R.drawable.music_icon);//设置通知的小图标
        if (mediaPlayer.isPlaying()){
            builder.setContentTitle(currentMusic.getTitle()+ " - " + "正在播放中");//设置通知内容的标题
        } else {
            builder.setContentTitle(currentMusic.getTitle()+ " - " + "已暂停");
        }
        builder.setContentText(currentMusic.getArtist() + " - " + currentMusic.getAlbum());//设置通知的内容
        Intent intent=new Intent(Constants.ACTION_ACTIVITY);//通知启动的页面
        PendingIntent pIntent=PendingIntent.getActivity(this,0, intent, 0);
        builder.setContentIntent(pIntent);//设置通知启动的程序
        notificationManager.notify(0x11, builder.build());//发送通知
    }

    private void saveInfo() {
        SharedPreferences sp = getSharedPreferences("music",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position",currentPosition);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
