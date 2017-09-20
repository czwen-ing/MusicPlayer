package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MusicMainFragment extends Fragment {

    private List<Fragment> fragments;
    String[] titles;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        titles = new String[]{"音乐","艺术家","专辑","收藏列表"};
        View view = inflater.inflate(R.layout.music_fragment,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.image_tabLayout);
        initFragments();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),fragments,titles,getActivity());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        resetTabLayout();
        return view;
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new MusicListFragment());
        fragments.add(new ArtistListFragment());
        fragments.add(new AlbumListFragment());
        fragments.add(new CollectListFragment());

    }
    /**
     * 使用tablayout + viewpager时注意 如果设置了setupWithViewPager
     * 则需要重新执行下方对每个条目赋值
     * 否则会出现icon文字不显示的bug
     */
    public void resetTabLayout(){
        for (int i = 0; i < tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setCustomView(viewPagerAdapter.getTabView(i));
        }
    }


}
