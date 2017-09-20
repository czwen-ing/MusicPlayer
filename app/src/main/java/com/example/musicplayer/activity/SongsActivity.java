package com.example.musicplayer.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.bean.Song;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.DataUtils;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.SongList;
import com.example.musicplayer.adapter.SongListAdapter;
import com.example.musicplayer.utils.OkhttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SongsActivity extends AppCompatActivity {

    private static final String TAG = "SongsActivity";
    private ImageView topImageView;
    private Toolbar toolbar;
    private RecyclerView lv_songs;
    private int type;
    private List<Song> songs;
    private SongListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getIntExtra("type",0);
        }
        initView();
        initData();
    }

    private void initData() {
        songs = new ArrayList<>();
        adapter = new SongListAdapter(SongsActivity.this,songs);
        adapter.setOnItemClickListener(new SongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long id) {
                gotoMusicPlayActivity(position);
            }
        });
        lv_songs.setAdapter(adapter);
        getDataFromNet();
        topImageView.setImageResource(DataUtils.getImage(type));
    }

    private void gotoMusicPlayActivity(int position) {
        Constants.musicList = Song.getAbstractMusicList(songs);
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("online", true);
        intent.putExtra("position", position);
        intent.putExtra("music", songs.get(position));
        startActivity(intent);
    }
    private void processData(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (songs != null) {
                    songs.clear();
                }
                songs.addAll(parseData(data));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<Song> parseData(String data) {
        Gson gson = new Gson();
        SongList songList = gson.fromJson(data, SongList.class);
        return songList.getSong_list();
    }

    private void initView() {
        topImageView = (ImageView) findViewById(R.id.top_image_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar_songs);
        lv_songs = (RecyclerView) findViewById(R.id.rv_songs);
        GridLayoutManager manager = new GridLayoutManager(this,1);
        lv_songs.setLayoutManager(manager);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void getDataFromNet() {
        OkhttpUtils.request(Constants.getMusicListURL(type, 10), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"联网失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                if (!TextUtils.isEmpty(data)){
                    processData(data);
                } else {
                    Toast.makeText(SongsActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
