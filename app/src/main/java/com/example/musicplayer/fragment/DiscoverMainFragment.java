package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.musicplayer.activity.MusicPlayActivity;
import com.example.musicplayer.bean.Song;
import com.example.musicplayer.utils.CacheUtils;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.RecommendMusicAdapter;
import com.example.musicplayer.bean.RecommendSong;
import com.example.musicplayer.activity.SongsActivity;
import com.example.musicplayer.utils.OkhttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DiscoverMainFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DiscoverMainFragment";
    private RelativeLayout newSongsRank;
    private RelativeLayout hotSongsRank;
    private RelativeLayout rockSongsRank;
    private RelativeLayout foreignSongsRank;
    private RelativeLayout classicalSongsRank;
    private RelativeLayout filmSongsRank;
    private RelativeLayout loveSongsRank;
    private RelativeLayout netWorkSongsRank;
    private ListView recommandListView;
    private RecommendMusicAdapter adapter;
    int type = 0;
    private List<Song> songs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.discover_fragment, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        songs = new ArrayList<>();
        adapter = new RecommendMusicAdapter(getContext(), songs);
        recommandListView.setAdapter(adapter);
        getDataFromNet();
    }

    private void initView(View view) {
        View headerView = View.inflate(getContext(), R.layout.discover_header, null);
        newSongsRank = (RelativeLayout) headerView.findViewById(R.id.new_songs_rank);
        hotSongsRank = (RelativeLayout) headerView.findViewById(R.id.hot_songs_rank);
        rockSongsRank = (RelativeLayout) headerView.findViewById(R.id.rock_rank);
        foreignSongsRank = (RelativeLayout) headerView.findViewById(R.id.europe_america_rank);
        classicalSongsRank = (RelativeLayout) headerView.findViewById(R.id.classical_rank);
        filmSongsRank = (RelativeLayout) headerView.findViewById(R.id.film_songs_rank);
        netWorkSongsRank = (RelativeLayout) headerView.findViewById(R.id.new_work_songs_rank);
        loveSongsRank = (RelativeLayout) headerView.findViewById(R.id.love_songs_rank);
        recommandListView = (ListView) view.findViewById(R.id.lv_recommand_music);
        recommandListView.addHeaderView(headerView);
        newSongsRank.setOnClickListener(this);
        hotSongsRank.setOnClickListener(this);
        rockSongsRank.setOnClickListener(this);
        foreignSongsRank.setOnClickListener(this);
        classicalSongsRank.setOnClickListener(this);
        filmSongsRank.setOnClickListener(this);
        netWorkSongsRank.setOnClickListener(this);
        loveSongsRank.setOnClickListener(this);
        recommandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                gotoMusicPlayActivity(position - 1);
            }
        });
    }

    private void gotoMusicPlayActivity(int position) {
        Constants.musicList = Song.getAbstractMusicList(songs);
        Intent intent = new Intent(getContext(), MusicPlayActivity.class);
        intent.putExtra("online", true);
        intent.putExtra("position", position);
        intent.putExtra("music", songs.get(position));
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.new_songs_rank:
                type = 1;
                break;
            case R.id.hot_songs_rank:
                type = 2;
                break;
            case R.id.rock_rank:
                type = 11;
                break;
            case R.id.classical_rank:
                type = 22;
                break;
            case R.id.europe_america_rank:
                type = 21;
                break;
            case R.id.film_songs_rank:
                type = 24;
                break;
            case R.id.love_songs_rank:
                type = 23;
                break;
            case R.id.new_work_songs_rank:
                type = 25;
                break;
            default:
                Log.e(TAG, "错误");
                return;
        }
        Intent intent = new Intent(getContext(), SongsActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    public void getDataFromNet() {

        OkhttpUtils.request(Constants.MUSIC_RECOMMEND_URL, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e(TAG, "联网失败" + e.getMessage());
                processData(CacheUtils.getData(getContext(), "recommend", ""));
                ;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (!TextUtils.isEmpty(data)) {
                    processData(data);
                    CacheUtils.saveData(getContext(), "recommend", data);
                }
            }
        });
    }

    private void processData(final String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (songs != null && songs.size() > 0) {
                    songs.clear();
                }
                songs.addAll(parseData(data));
                adapter.notifyDataSetChanged();
            }
        });


    }

    private List<Song> parseData(String data) {
        Gson gson = new Gson();
        RecommendSong songList = gson.fromJson(data, RecommendSong.class);
        return songList.getResult().getList();
    }
}
