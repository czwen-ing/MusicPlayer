package com.example.musicplayer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.Song;

import java.util.List;



public class RecommendMusicAdapter extends BaseAdapter {

    private List<Song> songs;
    private Context context;
    public RecommendMusicAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position + 1);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(songs.get(position).getSong_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = songs.get(position);
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recommand_music,parent,false);
            holder.songImage = (ImageView) convertView.findViewById(R.id.song_image);
            holder.songTitle = (TextView) convertView.findViewById(R.id.song_title);
            holder.songArtist = (TextView) convertView.findViewById(R.id.song_artist);
            holder.mvImage = (ImageView) convertView.findViewById(R.id.has_mv);
            holder.infoText = (TextView) convertView.findViewById(R.id.song_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(song.getPictureUri()).into(holder.songImage);
        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getArtist() + " - " + song.getAlbum());
        holder.mvImage.setVisibility(song.getHas_mv() == 0?View.GONE:View.VISIBLE);
        Log.e("TAG","info"+ song.getInfo());
        if (TextUtils.isEmpty(song.getInfo())){
            if (TextUtils.isEmpty(song.getVersions())){
                holder.infoText.setText(song.getLanguage());
            } else {
                holder.infoText.setText(song.getVersions());
            }
        } else {
            holder.infoText.setText(song.getInfo());
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView songImage;
        private TextView songTitle;
        private TextView songArtist;
        private ImageView mvImage;
        private TextView infoText;
    }

}
