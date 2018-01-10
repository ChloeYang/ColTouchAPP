package com.example.yangjiyu.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {


   /* TextView mCompany, mCopyRight;*/
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = this.getActionBar();
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.scene);

        mImageView = (ImageView) findViewById(R.id.iv_logo);
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        if( actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

        /*mCompany = (TextView) findViewById(R.id.tv_company);
        mCopyRight = (TextView) findViewById( R.id.tv_copy_right );*/
    }
/*    //// TODO: 2017/12/18 disable HOME_KEY

    //end todo*/
}
