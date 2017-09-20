package com.example.musicplayer.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.SearchSongsAdapter;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.bean.SongSearchInfo;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.OkhttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.attr.data;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    SearchView searchView;
    ListView lv_search;
    private List<Song> songs;
    SearchSongsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        searchView = (SearchView) findViewById(R.id.search_view);
        lv_search = (ListView) findViewById(R.id.lv_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                OkhttpUtils.request(Constants.MUSIC_SEARCH_URL + query,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e(TAG, "联网失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String data = response.body().string();
                                parseData(data);

                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoMusicPlayActivity(position);
            }
        });
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

    private void initData() {
        songs = new ArrayList<>();
        adapter = new SearchSongsAdapter(this,songs);
        lv_search.setAdapter(adapter);
    }

    private void parseData(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SongSearchInfo searchInfo = new Gson().fromJson(data, SongSearchInfo.class);
                songs.clear();
                songs.addAll(searchInfo.getSong());
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void gotoMusicPlayActivity(int position) {
        Constants.musicList = Song.getAbstractMusicList(songs);
        Intent intent = new Intent(SearchActivity.this, MusicPlayActivity.class);
        intent.putExtra("online", true);
        intent.putExtra("position", position);
        intent.putExtra("music", songs.get(position));
        startActivity(intent);
    }
}
