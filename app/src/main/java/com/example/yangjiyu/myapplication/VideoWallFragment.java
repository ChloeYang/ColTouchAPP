package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;



public class VideoWallFragment extends Fragment {

      RelativeLayout mRelativeLayout;
      VideoWallView mVideoWallView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_video_wall, container, false);
        View v = inflater.inflate(R.layout.fragment_video_wall,container,false);

        inflater.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = ((WindowManager)v.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int windowsWidth = display.getWidth();
        int windowsHeight = display.getHeight();
        Log.i("Checkout","display: width = " + display.getWidth() + ", height = " + display.getHeight());

//        Log.i("Checkout","container: width = " + container.getWidth() + ", height = " + container.getHeight());

        Log.i("Checkout","inflater: width = " + v.getMeasuredWidth() + ", height = " + v.getHeight());
        mVideoWallView = new VideoWallView(getContext(), 1450, 740);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.fragment_video_wall_view);
        mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        return v;
    }

}
