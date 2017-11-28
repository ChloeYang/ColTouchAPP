package com.example.yangjiyu.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity implements SourceItemListFragment.OnSourceListSelectedListener ,SourceItemFragment.OnSourceSelectedListener{

    private Fragment sourceList;
    private Fragment sourceItem;
    private Fragment video;
    private FragmentManager mFragmentManager;

    private SourceItemFragment sf;
    private VideoWallFragment vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFragmentManager=getSupportFragmentManager();

        sourceList =mFragmentManager.findFragmentById(R.id.fragment_source_list);
        sourceList =new SourceItemListFragment();
        mFragmentManager.beginTransaction().add(R.id.fragment_source_list,sourceList).commit();

        sourceItem=mFragmentManager.findFragmentById(R.id.fragment_source_item);
        sourceItem=new SourceItemFragment();
        mFragmentManager.beginTransaction().add(R.id.fragment_source_item,sourceItem).commit();

        video=mFragmentManager.findFragmentById(R.id.fragment_video_wall);
        video=new VideoWallFragment();
        mFragmentManager.beginTransaction().add(R.id.fragment_video_wall,video).commit();

        sf=(SourceItemFragment)sourceItem;
        vf=(VideoWallFragment)video;

    }

    public  void onSourceListSelected(int pos)
    {
        sf.onShow(pos);
        vf.upDataList(pos);
    }
    public void onSourceSelected(int pos)
    {
        vf.upDataItem(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_item, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if( id == R.id.menu_item_settings ){
            intent = new Intent(getApplicationContext(),SettingPreference.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

