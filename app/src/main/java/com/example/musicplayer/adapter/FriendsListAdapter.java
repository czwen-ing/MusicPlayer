package com.example.musicplayer.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.musicplayer.R;
import com.example.musicplayer.bean.FriendsInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pl.droidsonroids.gif.GifImageView;


public class FriendsListAdapter extends BaseAdapter {

    /**
     * 视频
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;

    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;

    private Context context;
    private List<FriendsInfo.ListBean> datas;

    public FriendsListAdapter(Context context, List<FriendsInfo.ListBean> datas){
        this.context = context;
        this.datas = datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (itemViewType) {
                case TYPE_VIDEO://视频
                    convertView = View.inflate(context, R.layout.item_video, null);
                    //在这里实例化特有的
                    viewHolder.tv_play_count = (TextView) convertView.findViewById(R.id.tv_play_count);
                    viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                    viewHolder.video_player = (JCVideoPlayer) convertView.findViewById(R.id.video_player);
                    break;
                case TYPE_IMAGE://图片
                    convertView = View.inflate(context, R.layout.item_image, null);
                    viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.content_image);
                    break;
                case TYPE_TEXT://文字
                    convertView = View.inflate(context, R.layout.item_text, null);
                    break;
                case TYPE_GIF://gif
                    convertView = View.inflate(context, R.layout.item_gif, null);
                    viewHolder.iv_gif = (GifImageView) convertView.findViewById(R.id.gif);
                    break;
//                case TYPE_AD://软件广告
//                    convertView = View.inflate(context, R.layout.all_ad_item, null);
//                    viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
//                    viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
//                    break;
            }

            initCommonView(convertView, viewHolder);
            if (convertView != null) {
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FriendsInfo.ListBean mediaItem = datas.get(position);
        bindData(itemViewType, viewHolder,mediaItem);

        return convertView;
    }

    private void bindData(int itemViewType, ViewHolder viewHolder, FriendsInfo.ListBean mediaItem) {

        switch (itemViewType) {
            case TYPE_VIDEO://视频
                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                if (mediaItem.getVideo().getVideo()!= null){
                    viewHolder.video_player.setUp(mediaItem.getVideo().getVideo().get(0),null);
//                    viewHolder.video_player.thumbImageView.setImageURI(Uri.parse( mediaItem.getVideo().getThumbnail().get(0)));
                    Glide.with(context).load(mediaItem.getVideo().getThumbnail().get(0))
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(viewHolder.video_player.ivThumb);
                    viewHolder.tv_play_count.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                    viewHolder.tv_video_duration.setText(timeToString(mediaItem.getVideo().getDuration() * 1000));
                }

                break;
            case TYPE_IMAGE://图片
                if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
//                    x.image().bind(viewHolder.iv_image_icon, mediaItem.getImage().getBig().get(0));
                    Glide.with(context).load(mediaItem.getImage().getBig().get(0)).placeholder(R.drawable.loading_bg)
                            .error(R.drawable.loading_bg)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .centerCrop()
                            .into(viewHolder.iv_image);
                }
//                if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
////                    x.image().bind(viewHolder.iv_image_icon, mediaItem.getImage().getBig().get(0));
//                    Glide.with(context).load(mediaItem.getImage().getBig().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image);
//                }

                break;
            case TYPE_TEXT://文字
                break;
            case TYPE_GIF://gif
                if (mediaItem.getGif().getImages().get(0)!= null){
                    Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_gif);
                }
                break;
        }

        viewHolder.tv_passtime.setText(mediaItem.getPasstime());
        viewHolder.iv_vip.setVisibility(mediaItem.getU().isIs_vip()?View.VISIBLE:View.GONE);
        if(mediaItem.getU()!=null&& mediaItem.getU().getHeader()!=null&&mediaItem.getU().getHeader().get(0)!=null){
            Glide.with(context).load(mediaItem.getU().getHeader().get(0)).into(viewHolder.iv_head_pic);
        }
        if(mediaItem.getU() != null&&mediaItem.getU().getName()!= null){
            viewHolder.tv_name.setText(mediaItem.getU().getName());
        }
        //设置文本
        viewHolder.tv_content.setText(mediaItem.getText());

        viewHolder.tv_up_count.setText(String.valueOf(mediaItem.getUp()));
        viewHolder.tv_down_count.setText(String.valueOf(mediaItem.getDown()));
        viewHolder.tv_share_count.setText(String.valueOf(mediaItem.getForward()));
        viewHolder.tv_comment_count.setText(mediaItem.getComment());
        if (mediaItem.getTop_comments() != null){
            viewHolder.lv_top_comment.setAdapter(new TopCommentAdapter(context, mediaItem.getTop_comments()) );
        }
    }

    private void initCommonView(View convertView, ViewHolder viewHolder) {
        viewHolder.tv_content = (TextView) convertView.findViewById(R.id.text_content);
        //top
        viewHolder.iv_head_pic = (CircleImageView) convertView.findViewById(R.id.user_header);
        viewHolder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
        viewHolder.tv_passtime = (TextView) convertView.findViewById(R.id.pass_time);
        viewHolder.iv_vip = (ImageView) convertView.findViewById(R.id.vip);
        //bottom
        viewHolder.tv_up_count = (TextView) convertView.findViewById(R.id.up_count);
        viewHolder.tv_down_count = (TextView) convertView.findViewById(R.id.down_count);
        viewHolder.tv_share_count = (TextView) convertView.findViewById(R.id.share_count);
        viewHolder.tv_comment_count = (TextView) convertView.findViewById(R.id.comment_count);
        viewHolder.lv_top_comment = (ListView) convertView.findViewById(R.id.top_comment_list);
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        String type = datas.get(position).getType();
        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_TEXT;
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    private static class ViewHolder {
        CircleImageView iv_head_pic;
        TextView tv_name;
        TextView tv_passtime;
        ImageView iv_vip;
        //bottom
        TextView tv_up_count;
        TextView tv_down_count;
        TextView tv_share_count;
        TextView tv_comment_count;
        ListView lv_top_comment;
        //中间公共部分
        TextView tv_content;
        //Video
        TextView tv_play_count;
        TextView tv_video_duration;
        JCVideoPlayer video_player;
        //Image
        ImageView iv_image;
        //Gif
        GifImageView iv_gif;
    }
    public String timeToString(long duration) {
        long totalTime = duration / 1000;
        long seconds = totalTime % 60;
        long minutes = totalTime / 60;
        long hours = totalTime / 3600;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
    private class TopCommentAdapter extends BaseAdapter{
        List<FriendsInfo.ListBean.TopCommentsBean> comments;
        Context context;
        TopCommentAdapter(Context context, List<FriendsInfo.ListBean.TopCommentsBean> comments){
            this.context = context;
            this.comments= comments;
        }
        @Override
        public int getCount() {
            return comments.size();
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(context,R.layout.top_comment_item,null);
            TextView topComment = (TextView) convertView.findViewById(R.id.top_comment);
            topComment.setText(Html.fromHtml("<font color=#4674e7><b>" + comments.get(position).getU().getName()
                    + "</b></font>: "+comments.get(position).getContent()));
            return convertView;
        }
    }
}
