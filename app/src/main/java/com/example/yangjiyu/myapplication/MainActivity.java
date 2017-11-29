package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private final static int MAX_SUPPORT_CELL = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences ipShared = getSharedPreferences(getString(R.string.pref_setting),Context.MODE_PRIVATE);
        SharedPreferences.Editor ipEditor = ipShared.edit();
        ipEditor.putString(getString(R.string.pref_data_vclordip), "172.16.129.152");

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_define1_flag),0);
        editor.putInt(getString(R.string.pref_define1_num),0);
        for (int index=0;index<MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(getString(R.string.pref_define1_startX_),0);
            editor.putInt(getString(R.string.pref_define1_startY_),0);
            editor.putInt(getString(R.string.pref_define1_endX_),0);
            editor.putInt(getString(R.string.pref_define1_endY_),0);
            editor.putInt(getString(R.string.pref_define1_signal_),0);
        }
        editor.putInt(getString(R.string.pref_define2_flag),0);
        editor.putInt(getString(R.string.pref_define2_num),0);
        for (int index=0;index<MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(getString(R.string.pref_define2_startX_),0);
            editor.putInt(getString(R.string.pref_define2_startY_),0);
            editor.putInt(getString(R.string.pref_define2_endX_),0);
            editor.putInt(getString(R.string.pref_define2_endY_),0);
            editor.putInt(getString(R.string.pref_define2_signal_),0);
        }

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

