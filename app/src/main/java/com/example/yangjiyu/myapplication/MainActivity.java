package com.example.yangjiyu.myapplication;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.yangjiyu.myapplication.utils.PollingUtils;


public class MainActivity extends AppCompatActivity implements SourceItemListFragment.OnSourceListSelectedListener ,
        SourceItemFragment.OnSourceSelectedListener,
        VideoWallView.onCleanDefineSceneClickListener,
        VideoWallFragment.onSetNextItemListClickListener,
        SourceItemFragment.onSetNextItemClickListener{

    private Fragment sourceList;
    private Fragment sourceItem;
    private Fragment video;
    private FragmentManager mFragmentManager;

    private SourceItemFragment sf;
    private VideoWallFragment vf;

    private SharedAppData sharedAppData=null;
    public final static int MAX_SUPPORT_CELL = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.scene);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        int Define1Flag=sharedPref.getInt(getString(R.string.pref_define1_flag),0);
        int Define2Flag=sharedPref.getInt(getString(R.string.pref_define2_flag),0);
        if (Define1Flag==0){
            initDefine1Scene();
        }
        if (Define2Flag==0){
            initDefine2Scene();
        }


        sharedAppData=SharedAppData.newInstance(this);
        sharedAppData.saveModelGuideFlag(0);
        sharedAppData.saveModelGuideStep(0);

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

        //// TODO: 2017/12/13 disable HOME_KEY  在onCreate设置，此时home被拦截
        this.getWindow().setFlags(LoginActivity.FLAG_HOMEKEY_DISPATCHED, LoginActivity.FLAG_HOMEKEY_DISPATCHED);//关键代码

        //PollingUtils.startPollingService(this, 5, PollingService.class, PollingService.ACTION);
        SharedAppData sharedAppData = SharedAppData.newInstance(this);
        MonitorThread monitorThread = MonitorThread.newInstance( sharedAppData.getVCLordIP(),VclordActivity.PORT, this);
        boolean bIsStarted = monitorThread.getIsStarted();
        if (!bIsStarted) {
            monitorThread.start();
        }
        /*ReceiveAlarmThread receiveAlarmThread= ReceiveAlarmThread.newInstance(sharedAppData.getVCLordIP(),this);
        bIsStarted = receiveAlarmThread.getIsStarted();
        if (!bIsStarted) {
            receiveAlarmThread.start();
        }*/
    }
/*    //// TODO: 2017/12/18 disable HOME_KEY

    //end todo*/

    public void initDefine1Scene()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_define1_flag),0);
        editor.putInt(getString(R.string.pref_define1_num),0);
        for (int index=0;index<MainActivity.MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(getString(R.string.pref_define1_startX_)+index,0);
            editor.putInt(getString(R.string.pref_define1_startY_)+index,0);
            editor.putInt(getString(R.string.pref_define1_endX_)+index,0);
            editor.putInt(getString(R.string.pref_define1_endY_)+index,0);
            editor.putInt(getString(R.string.pref_define1_signal_)+index,0);
        }
        editor.commit();
    }
    public void initDefine2Scene()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_define2_flag),0);
        editor.putInt(getString(R.string.pref_define2_num),0);
        for (int index=0;index<MainActivity.MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(getString(R.string.pref_define2_startX_)+index,0);
            editor.putInt(getString(R.string.pref_define2_startY_)+index,0);
            editor.putInt(getString(R.string.pref_define2_endX_)+index,0);
            editor.putInt(getString(R.string.pref_define2_endY_)+index,0);
            editor.putInt(getString(R.string.pref_define2_signal_)+index,0);
        }
        editor.commit();
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

    public void onCleanDefineScene(int pos)
    {
        sf.setCurrentItem(pos);
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
        if( id == R.id.menu_item_about ){
            intent = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menu_item_set_network){
            if(android.os.Build.VERSION.SDK_INT > 10 ){
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }else {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        }
        if( id == R.id.menu_item_system_info ){
            DialogList dialogList = new DialogList(this);
            dialogList.AllInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Stop polling service
        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }

    @Override
    public void onSetNextItemList(int itemList) {
        sf.onShow(itemList);
        vf.upDataList(itemList);
    }

    @Override
    public void onSetNextItem(int item) {
        sf.onShow(item);
        vf.upDataList(item);
    }
}

