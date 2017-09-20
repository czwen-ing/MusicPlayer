package com.example.musicplayer.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
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

import com.example.musicplayer.MusicService;
import com.example.musicplayer.R;
import com.example.musicplayer.activity.MusicPlayActivity;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.MusicUtils;

import java.util.List;

public class MusicListFragment extends ListFragment {
    private List<Music> musicList;
    private Context context;
    public List<Music> getMusicList() {
        return musicList;
    }


    public void setMusicList(List<Music> musicList) {//可以被其他地方调用并设置不同的musicList
        this.musicList = musicList;
    }

    @Override
    public void onStart() {
        registerForContextMenu(getListView());
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicList = getMusicList();
        if (musicList == null || musicList.size() == 0){
            musicList = MusicUtils.getMusicData(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Constants.musicList = Music.getAbstractMusicList(musicList);
        if(musicList != null){
            setListAdapter(new MusicAdapter());
        } else {
            Toast.makeText(getActivity(),"存储卡中没有音乐",Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(getActivity(),MusicService.class);
        getActivity().startService(intent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class MusicAdapter extends BaseAdapter {

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
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_music, null);
            }
            ImageView musicImage = (ImageView) convertView.findViewById(R.id.music_image);
            TextView musicName = (TextView) convertView.findViewById(R.id.music_name);
            TextView musicSinger = (TextView) convertView.findViewById(R.id.music_singer);
            TextView musicTime = (TextView) convertView.findViewById(R.id.music_time);
            musicName.setText(musicList.get(position).getTitle());
            musicSinger.setText(musicList.get(position).getArtist());
            int time = musicList.get(position).getTime();
            musicTime.setText(MusicUtils.timeToString(time));
            Bitmap bitmap = MusicUtils.getPicture(getActivity(),musicList.get(position).getAlbumId());
            musicImage.setImageBitmap(bitmap);

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l,v,position,id);
        Constants.musicList = Music.getAbstractMusicList(musicList);
        Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("music",musicList.get(position));
        startActivity(intent);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.musiclist_context,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//获取上下文信息
        switch (item.getItemId()){
            case R.id.setToBell:
                setRing(musicList.get(info.position));
                break;
            case R.id.addToPlayList:
                Music music = musicList.get(info.position);
                int i = 0;
                for (; i < Constants.collectList.size(); i++){
                    if (Constants.collectList.get(i).getTitle().equals(music.getTitle())){
                        break;
                    }
                }
                if (i == Constants.collectList.size()){
                    Constants.collectList.add(music);
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void setRing(Music music) {//设置为铃声
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA,music.getUrl());
        values.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3");
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE,true);
        values.put(MediaStore.Audio.AudioColumns.IS_ALARM,false);
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION,false);
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC,false);
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(music.getUrl());
        Uri newUri = getActivity().getContentResolver().insert(uri,values);
        RingtoneManager.setActualDefaultRingtoneUri(getActivity(),RingtoneManager.TYPE_RINGTONE,newUri);
    }
}
