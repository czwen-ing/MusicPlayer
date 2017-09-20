package com.example.musicplayer.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.AbstractMusic;
import com.example.musicplayer.bean.Music;

import java.util.List;


public class MusicListAdapter extends ArrayAdapter<AbstractMusic> {
    private List<AbstractMusic> musicList;
    private Context context;
    private int resourceId;
    private int currentPosition;

    public MusicListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AbstractMusic> objects) {
        super(context, resource, objects);
        this.context = context;
        musicList = objects;
        resourceId = resource;
    }


    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public AbstractMusic getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        AbstractMusic music = musicList.get(position);
        View view;
        if (currentPosition == position) {
            view = LayoutInflater.from(context).inflate(R.layout.menu_list_item_selected, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
        }
        TextView musicName = (TextView) view.findViewById(R.id.menu_music_name);
        TextView artist = (TextView) view.findViewById(R.id.menu_artist);
        ImageView close = (ImageView) view.findViewById(R.id.menu_item_close);
        musicName.setText(music.getTitle());
        artist.setText(music.getArtist());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == position){
                    Toast.makeText(context,"当前音乐正在播放中",Toast.LENGTH_SHORT).show();
                }else {
                    musicList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return view;
    }


    public void setCurrentItem(int position) {
        currentPosition = position;
    }
}
