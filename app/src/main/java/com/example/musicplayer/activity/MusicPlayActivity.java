package com.example.musicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.bean.AbstractMusic;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.adapter.MusicListAdapter;
import com.example.musicplayer.utils.MusicUtils;
import com.example.musicplayer.R;

import java.util.List;

import javax.security.auth.login.LoginException;

public class MusicPlayActivity extends AppCompatActivity {

    public static final String TAG = "MusicPlayActivity";
    private List<AbstractMusic> musicList;
    private List list;
    private TextView title;//音乐名
    private TextView singer;//歌手名
    private TextView currentTime;
    private TextView totalTime;
    private ImageView singerView;//歌手图片
    private ImageView controlImage;
    private ImageView playTypeImage;
    private ImageView musicListMenu;
    private ListView listView;
    private SeekBar seekBar;
    private AbstractMusic currentMusic;//从MusicListFragment获取的当前音乐
    private int currentPosition;
    private boolean isPause = false;//判断播放暂停按钮
    private int playType = Constants.PLAY_TYPE;
    private ServerReceiver receiver;
    private MusicListAdapter adapter;
    private boolean userSeek = false;//判断是否手动的滑动seekbar
    private boolean fromNet;
    int duration;

    private int[] playTypeImages = {R.drawable.widget_loop, R.drawable.widget_repeatone, R.drawable.widget_shuffle};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        initView();
        initData();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.musicplay_name);
        singer = (TextView) findViewById(R.id.musicplay_singer);
        currentTime = (TextView) findViewById(R.id.currentTime);
        totalTime = (TextView) findViewById(R.id.totalTime);
        singerView = (ImageView) findViewById(R.id.musicplay_image);
        controlImage = (ImageView) findViewById(R.id.control);
        playTypeImage = (ImageView) findViewById(R.id.loop_type);
        musicListMenu = (ImageView) findViewById(R.id.music_menu);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//进度发送变化时调用
                userSeek = fromUser;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//开始拖动时调用

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//结束拖动时调用
                Intent seekIntent = new Intent(Constants.SEEKBAR_ACTION);
                seekIntent.putExtra("progress", seekBar.getProgress());
                sendBroadcast(seekIntent);
                currentTime.setText(MusicUtils.timeToString(seekBar.getProgress() * duration / 100));
                userSeek = false;
            }
        });
    }

    private void initData() {
        receiver = new ServerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.UPDATE_ACTION);
        filter.addAction(Constants.COMPLETE_ACTION);
        filter.addAction(Constants.UPDATE_NET);
        filter.addAction(Constants.UPDATE_BUFFER);
        registerReceiver(receiver, filter);

        fromNet = getIntent().getBooleanExtra("online", false);

        int listType = getIntent().getIntExtra("listType", -1);
        if (listType == Constants.COLLECTION_MUSIC) {
            musicList = Constants.collectList;
        } else {
            musicList = Constants.musicList;
        }
        currentMusic = (AbstractMusic) getIntent().getSerializableExtra("music");
        if (currentMusic == null) {
            SharedPreferences sp = getSharedPreferences("music", Context.MODE_PRIVATE);
            currentPosition = sp.getInt("position", 0);
            currentMusic = musicList.get(currentPosition);
            showInfo();
            isPause = true;
            control(null);
        } else {
            currentPosition = getIntent().getIntExtra("position", 0);
            playMusic();
        }
        adapter = new MusicListAdapter(this, R.layout.menu_list_item_normal, musicList);
    }

    private void showInfo() {
        title.setText(currentMusic.getTitle());
        singer.setText(currentMusic.getArtist());
        playTypeImage.setImageResource(playTypeImages[playType]);
        duration = currentMusic.getDuration();
        totalTime.setText(MusicUtils.timeToString(currentMusic.getDuration()));
        if (currentMusic.getPictureUri() != null){
            Glide.with(this).load(currentMusic.getPictureUri()).into(singerView);
        }
        seekBar.setProgress(0);
        controlImage.setImageResource(R.drawable.widget_pause_selector);
    }

    private void playMusic() {
        showInfo();
        Intent controlIntent = new Intent(Constants.CONTROL_ACTION);
        controlIntent.putExtra("position", currentPosition);
        controlIntent.putExtra("new", Constants.NEW);
        if (fromNet) {
            controlIntent.putExtra("online", true);
        }
        controlIntent.putExtra("playType", playType);
        sendBroadcast(controlIntent);
        isPause = false;
    }

    public void back(View view) {
        finish();
    }

    public void control(View view) {
        Intent controlIntent = new Intent(Constants.CONTROL_ACTION);
        isPause = !isPause;
        if (!isPause) {
            controlImage.setImageResource(R.drawable.widget_pause_selector);
            controlIntent.putExtra("control", Constants.PLAY);
        } else {
            controlImage.setImageResource(R.drawable.widget_play_selector);
            controlIntent.putExtra("control", Constants.PAUSE);
        }
        sendBroadcast(controlIntent);
    }

    public void changePlayType(View view) {//改变播放循环类型：顺序循环，单曲循环，随机播放
        if (playType == (Constants.LOOP)) {
            playTypeImage.setImageResource(R.drawable.widget_repeatone);
            playType = Constants.ONE;
        } else if (playType == Constants.ONE) {
            playTypeImage.setImageResource(R.drawable.widget_shuffle);
            playType = Constants.RANDOM;
        } else if (playType == Constants.RANDOM) {
            playTypeImage.setImageResource(R.drawable.widget_loop);
            playType = Constants.LOOP;
        }
        Constants.PLAY_TYPE = playType;
        Intent styleIntent = new Intent(Constants.UPDATE_STYLE);
        styleIntent.putExtra("playType", playType);
        sendBroadcast(styleIntent);
    }

    public void previous(View view) {//上一首按钮
        currentPosition = currentPosition - 1;
        if (currentPosition == -1) {//第一首时则跳到最后一首
            currentPosition = musicList.size() - 1;
        }
        currentMusic = musicList.get(currentPosition);
        playMusic();
    }


    public void next(View view) {//下一首按钮
        currentPosition = currentPosition + 1;
        if (currentPosition >= musicList.size()) {//最后一首时调到第一首
            currentPosition = 0;
        }
        currentMusic = musicList.get(currentPosition);
        playMusic();
    }

    WindowManager.LayoutParams params;

    public void showMusicListMenu(View view) {
        final Window window = getWindow();
        params = window.getAttributes();
        params.alpha = 0.5f;
        window.setAttributes(params);
        View v = View.inflate(this, R.layout.music_list_menu, null);
        listView = (ListView) v.findViewById(R.id.music_list);
        TextView collect = (TextView) v.findViewById(R.id.tv_collect);
        if (fromNet){
            collect.setEnabled(false);
        }
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbstractMusic music = musicList.get(currentPosition);
                int i = 0;
                for (; i < Constants.collectList.size(); i++) {
                    if (Constants.collectList.get(i).getTitle().equals(music.getTitle())) {
                        Toast.makeText(MusicPlayActivity.this, "已收藏此歌曲", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (i == Constants.collectList.size()) {
                    Constants.collectList.add(music);
                }
            }
        });
        TextView musicCount = (TextView) v.findViewById(R.id.music_count);
        musicCount.setText(String.valueOf(musicList.size()));
        adapter.setCurrentItem(currentPosition);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMusicToPlay(position);
                adapter.setCurrentItem(currentPosition);
                adapter.notifyDataSetChanged();
            }
        });
        PopupWindow popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(v);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                window.setAttributes(params);
            }
        });
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void selectMusicToPlay(int position) {
        currentPosition = position;
        currentMusic = musicList.get(position);
        playMusic();
    }

    private class ServerReceiver extends BroadcastReceiver {//接收来自服务的广播

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.UPDATE_ACTION) {
                int position = intent.getIntExtra("position", 0);//服务中播放器的进度时间/ms
                currentTime.setText(MusicUtils.timeToString(position));
                if (!userSeek) {
                    seekBar.setProgress((int) (position * 1.0 / duration*100));
                }

            } else if (intent.getAction() == Constants.COMPLETE_ACTION) {
                currentPosition = intent.getIntExtra("position", 0);
                currentMusic = musicList.get(currentPosition);
                showInfo();
            } else if (intent.getAction() == Constants.UPDATE_NET){
                    duration = intent.getIntExtra("duration", currentMusic.getDuration() * 100);
                    String pictureUri = intent.getStringExtra("picture_uri");
                    if (duration > 0) {
                        totalTime.setText(MusicUtils.timeToString(duration));
                        if (pictureUri !=null){
                            Glide.with(MusicPlayActivity.this).load(pictureUri).into(singerView);
                        }
                    }
            } else if (intent.getAction() == Constants.UPDATE_BUFFER){
                int bufferTime = intent.getIntExtra("bufferTime",0);
                seekBar.setSecondaryProgress(bufferTime);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
