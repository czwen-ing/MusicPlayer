package com.example.musicplayer.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private Context context;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles, Context context) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
        this.context = context;
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, Context context) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public View getTabView(int position) {
        View v = View.inflate(context, R.layout.custom_tablayout, null);
        TextView tabText = (TextView) v.findViewById(R.id.tab_text);
        tabText.setText(titles[position]);

        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
