package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Howie on 2017/7/25.
 */

public class VideoCellAdapter extends BaseAdapter {

    private Context mContext;

    public VideoCellAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if( convertView == null ){
            // if it's not recycled, initialize some attributes
            imageView = (ImageView) convertView.findViewById(R.id.iv_test);
            //imageView = new ImageView(mContext);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return null;
    }

    private Integer[] mThumbIds = {
            /*R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,
            R.drawable.rectangle,*/
            R.drawable.desk_256pale_,
            R.drawable.desktop_black_,
            R.drawable.desktop_hor8ple_,
            R.drawable.desktop_pane_,
            R.drawable.desktop_pane_,
    };

}
