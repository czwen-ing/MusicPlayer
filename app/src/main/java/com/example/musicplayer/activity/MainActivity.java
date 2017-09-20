package com.example.musicplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.utils.Constants;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.utils.MusicUtils;
import com.example.musicplayer.MySQLHelper;
import com.example.musicplayer.fragment.DiscoverMainFragment;
import com.example.musicplayer.fragment.FriendsMainFragment;
import com.example.musicplayer.fragment.MusicMainFragment;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private List<Fragment> fragments;
    ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private ImageView iv_discover;
    private ImageView iv_music;
    private ImageView iv_friends;
    private DrawerLayout drawerLayout;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        MySQLHelper helper = new MySQLHelper(this,"music",null,1);
        mDatabase = helper.getWritableDatabase();
        Constants.collectList = Music.getAbstractMusicList(MusicUtils.getDataFromDB(mDatabase));
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        iv_discover = (ImageView) findViewById(R.id.toolbar_discover);
        iv_music = (ImageView) findViewById(R.id.toolbar_music);
        iv_friends = (ImageView) findViewById(R.id.toolbar_friends);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        initFragments();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        iv_discover.setOnClickListener(this);
        iv_music.setOnClickListener(this);
        iv_friends.setOnClickListener(this);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        iv_music.setSelected(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.titlebar_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new DiscoverMainFragment());
        fragments.add(new MusicMainFragment());
        fragments.add(new FriendsMainFragment());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                iv_discover.setSelected(true);
                iv_music.setSelected(false);
                iv_friends.setSelected(false);
                break;
            case 1:
                iv_discover.setSelected(false);
                iv_music.setSelected(true);
                iv_friends.setSelected(false);
                break;
            case 2:
                iv_discover.setSelected(false);
                iv_music.setSelected(false);
                iv_friends.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_discover:
                if (viewPager.getCurrentItem() != 0) {//不然cpu会有损耗
                    iv_discover.setSelected(true);
                    iv_music.setSelected(false);
                    iv_friends.setSelected(false);
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.toolbar_music:
                if (viewPager.getCurrentItem() != 1) {
                    iv_discover.setSelected(false);
                    iv_music.setSelected(true);
                    iv_friends.setSelected(false);
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.toolbar_friends:
                if (viewPager.getCurrentItem() != 2) {
                    iv_discover.setSelected(false);
                    iv_music.setSelected(false);
                    iv_friends.setSelected(true);
                    viewPager.setCurrentItem(2);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {//关闭的时候将播放列表中的数据保存到数据库中
        mDatabase.execSQL("delete from music_tb");//删除已有的所有数据
        for(int i=0;i< Constants.collectList.size();i++){//循环遍历播放列表中的音乐
            Music music= (Music) Constants.collectList.get(i);//获取音乐
            mDatabase.execSQL("insert into music_tb (title,artist,album,album_id,time,url)values(?,?,?,?,?,?)",new String[]{
                    music.getTitle(),music.getArtist(),music.getAlbum_title(),music.getAlbumId()+"",music.getTime()+"",music.getUrl()});//将音乐信息保存到数据库
        }
        super.onDestroy();
    }

}
