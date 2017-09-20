package com.example.musicplayer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.activity.MusicInfoActivity;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.MusicUtils;
import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumListFragment extends ListFragment {

    private List<MusicGroupByAlbum> albumList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Music> musicList = MusicUtils.getMusicData(getActivity());
        if (musicList != null){
            musicGroupByAlbum(musicList);
        }else{
            Toast.makeText(getActivity(),"内存中没有音乐",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(new AlbumListAdapter());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class AlbumListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return albumList.size();
        }

        @Override
        public Object getItem(int position) {
            return albumList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(getActivity(), R.layout.item_album,null);
            }
            ImageView albumImage = (ImageView) convertView.findViewById(R.id.album_image);
            TextView albumName = (TextView) convertView.findViewById(R.id.album_name);
            TextView musicCount = (TextView) convertView.findViewById(R.id.music_count);
            Bitmap bitmap = MusicUtils.getPicture(getActivity(),albumList.get(position)
                    .getMusics().get(0).getAlbumId());
            if (bitmap != null){
                albumImage.setImageBitmap(bitmap);
            }
            albumName.setText(albumList.get(position).getAlbum());
            musicCount.setText(Html.fromHtml("共有<font color=red><b>" + albumList.get(position).getCount()
                    + "</b></font>首歌曲"));
            return convertView;
        }
    }

    private void musicGroupByAlbum(List<Music> musicList){
        for (int i = 0; i < musicList.size(); i++){
            int j = 0;
            for (; j < albumList.size(); j++){
                if (musicList.get(i).getArtist().equals(albumList.get(j).getAlbum())){
                    albumList.get(j).setCount(albumList.get(j).getCount()+1);
                    albumList.get(j).getMusics().add(musicList.get(i));
                    break;
                }
            }
            if(j == albumList.size()) {
                MusicGroupByAlbum album = new MusicGroupByAlbum();
                album.setAlbum(musicList.get(i).getAlbum_title());
                album.setCount(1);
                List<Music> musics = new ArrayList<>();
                musics.add(musicList.get(i));
                album.setMusics(musics);
                albumList.add(album);
            }
        }
    }

    private class MusicGroupByAlbum {
        private String album;
        private int count;
        private List<Music> musics;

        String getAlbum() {
            return album;
        }

        void setAlbum(String album) {
            this.album = album;
        }

        public int getCount() {
            return count;
        }

        void setCount(int count) {
            this.count = count;
        }

        List<Music> getMusics() {
            return musics;
        }

        void setMusics(List<Music> musics) {
            this.musics = musics;
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Constants.itemMusics = albumList.get(position).getMusics();
        Intent intent = new Intent(getActivity(), MusicInfoActivity.class);
        intent.putExtra("title",albumList.get(position).getAlbum());
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }
}
