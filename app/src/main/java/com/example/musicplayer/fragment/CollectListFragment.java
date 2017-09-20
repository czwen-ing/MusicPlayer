package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.bean.AbstractMusic;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.utils.MusicUtils;
import com.example.musicplayer.R;
import com.example.musicplayer.activity.MusicPlayActivity;

import java.util.List;

public class CollectListFragment extends ListFragment {
    public List<AbstractMusic> musicList = Constants.collectList;
    private CollectListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (musicList != null){
            adapter = new CollectListAdapter();
            setListAdapter(adapter);
        }else {
            Toast.makeText(getActivity(),"收藏列表中没有音乐",Toast.LENGTH_SHORT).show();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onStart() {
        registerForContextMenu(getListView());// 为音乐列表注册上下文菜单
        super.onStart();
    }

    private class CollectListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public Object getItem(int position) {
            return musicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(getActivity(), R.layout.item_music,null);
            }
            Music music = (Music) musicList.get(position);
            ImageView musicImage = (ImageView) convertView.findViewById(R.id.music_image);
            TextView musicName = (TextView) convertView.findViewById(R.id.music_name);
            TextView musicSinger = (TextView) convertView.findViewById(R.id.music_singer);
            TextView musicTime = (TextView) convertView.findViewById(R.id.music_time);
            musicName.setText(music.getTitle());
            musicSinger.setText(music.getArtist());
            int time = music.getDuration();
            musicTime.setText(MusicUtils.timeToString(time));
            Glide.with(getContext()).load(music.getPictureUri()).into(musicImage);
            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l,v,position,id);
        Constants.musicList = musicList;
        Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("listType",Constants.COLLECTION_MUSIC);
//        intent.putExtra("music",musicList.get(position));
        startActivity(intent);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {// 创建上下文菜单
        getActivity().getMenuInflater().inflate(R.menu.collection_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.deleteAll://清空播放列表
                musicList.clear();
                System.out.println(musicList);
                break;
            case R.id.deleteFromList://从播放列表中删除
                musicList.remove(info.position);
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
        Constants.collectList = musicList;
        return super.onContextItemSelected(item);
    }
}
