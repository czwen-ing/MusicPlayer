package com.example.musicplayer.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.fragment.MusicListFragment;
import com.example.musicplayer.utils.Constants;

import java.util.List;

public class MusicInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView count;
    private List<Music> musics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_info);
        String title = getIntent().getStringExtra("title");
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        count = (TextView) findViewById(R.id.music_count);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
        musics = Constants.itemMusics;
        count.setText("歌曲数：" + musics.size());
        replaceToMusicFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceToMusicFragment(){
        MusicListFragment fragment = new MusicListFragment();
        fragment.setMusicList(musics);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.artist_container,fragment);
        transaction.commit();
    }
}
