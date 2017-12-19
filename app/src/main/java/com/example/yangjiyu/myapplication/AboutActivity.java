package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class AboutActivity extends Activity {


   /* TextView mCompany, mCopyRight;*/
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        android.app.ActionBar actionBar = this.getActionBar();

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
