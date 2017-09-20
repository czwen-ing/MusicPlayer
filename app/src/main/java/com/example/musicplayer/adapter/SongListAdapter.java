package com.example.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.Song;

import java.util.List;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {

    private Context context;
    private List<Song> songs;
    public SongListAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(context, R.layout.item_songs,null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.index.setText(String.valueOf(position+1));
        holder.songName.setText(song.getTitle());
        holder.songArtist.setText(song.getArtist() + "-" + song.getAlbum());
        holder.mv.setVisibility(song.getHas_mv() == 0?View.GONE:View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(position,song.getSongId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView index;
        private TextView songName;
        private TextView songArtist;
        private ImageView mv;
        public MyViewHolder(View itemView) {
            super(itemView);
            index = (TextView) itemView.findViewById(R.id.song_index);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            songArtist = (TextView) itemView.findViewById(R.id.song_artist);
            mv = (ImageView) itemView.findViewById(R.id.mvImage);
        }
    }

    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(int position,long id);
    }
}
