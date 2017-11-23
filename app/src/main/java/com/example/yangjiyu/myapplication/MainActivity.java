package com.example.yangjiyu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mData;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ///mImg=(ImageView)findViewById(R.id.id_content);

        initData();
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //--GridLayoutManager gridLayoutManager = new GridLayoutManager(this,9);

        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);//--gridLayoutManager
        //设置适配器
        mAdapter = new GalleryAdapter(this, mData);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
/*        mRecyclerView.setOnScrollChangeListener(new setOnItemScrollChangeListener()
        {
            @Override
            public void onChange(View view, int position)
            {
                mImg.setImageResource(mData.get(position));
            };
        });*/
    }
    private void initData()
    {
        mData = new ArrayList<>(Arrays.asList(R.drawable.a,
                R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
                R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.l));
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

