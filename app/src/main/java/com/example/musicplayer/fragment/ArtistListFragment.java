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

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MusicInfoActivity;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;


public class ArtistListFragment extends ListFragment {
    private static final String TAG = "ArtistListFragment";
    private List<MusicGroupByArtist> artistList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Music> musicList = MusicUtils.getMusicData(getActivity());
        if (musicList != null){
            musicGroupByArtist(musicList);
        }else{
            Toast.makeText(getActivity(),"内存中没有音乐",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setListAdapter(new ArtistListAdapter());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

     private class MusicGroupByArtist {
        private String singer;
        private int count;
        private List<Music> musics;

        String getSinger() {
            return singer;
        }

        void setSinger(String singer) {
            this.singer = singer;
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

    private class ArtistListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return artistList.size();
        }

        @Override
        public Object getItem(int position) {
            return artistList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(getActivity(), R.layout.item_artist,null);
            }
            ImageView singerImage = (ImageView) convertView.findViewById(R.id.artist_image);
            TextView singerName = (TextView) convertView.findViewById(R.id.singer_name);
            TextView musicCount = (TextView) convertView.findViewById(R.id.music_count);
            Bitmap bitmap = MusicUtils.getPicture(getActivity(),artistList.get(position)
                    .getMusics().get(0).getAlbumId());
            if (bitmap != null){
                singerImage.setImageBitmap(bitmap);
            }
            singerName.setText(artistList.get(position).getSinger());
            musicCount.setText(Html.fromHtml("共有<font color=red><b>" + artistList.get(position).getCount()
                    + "</b></font>首歌曲"));
            return convertView;
        }
    }
    private void musicGroupByArtist(List<Music> musicList){
        for (int i = 0; i < musicList.size(); i++){
            int j = 0;
            for (; j < artistList.size(); j++){
                if (musicList.get(i).getArtist().equals(artistList.get(j).getSinger())){
                    artistList.get(j).setCount(artistList.get(j).getCount()+1);
                    artistList.get(j).getMusics().add(musicList.get(i));
                    break;
                }
            }
            if(j == artistList.size()) {
                MusicGroupByArtist artist = new MusicGroupByArtist();
                artist.setSinger(musicList.get(i).getArtist());
                artist.setCount(1);
                List<Music> musics = new ArrayList<>();
                musics.add(musicList.get(i));
                artist.setMusics(musics);
                artistList.add(artist);
            }
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Constants.itemMusics = artistList.get(position).getMusics();
        Intent intent = new Intent(getActivity(), MusicInfoActivity.class);
        intent.putExtra("title",artistList.get(position).getSinger());
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }

}
