package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicplayer.bean.Song;

import java.util.List;


public class SearchSongsAdapter extends BaseAdapter {

    private Context context;
    private List<Song> songs;
    public SearchSongsAdapter(Context context, List<Song> songs){
        this.context = context;
        this.songs = songs;
    }
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return songs.get(position).getSongId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null,false);
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(songs.get(position).getTitle() + " - " + songs.get(position).getArtist());
        return convertView;
    }
    private class ViewHolder{
        TextView textView;
    }
}
