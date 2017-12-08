package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class VideoWallFragment extends Fragment {

    RelativeLayout mRelativeLayout;
    VideoWallView mVideoWallView;
    View v;

    int windowsWidth = 1585;//1585//2100
    int windowsHeight = 610;//610//840

    private int mListIndex=-1;
    private int mSceneIndex=-1;
    private int mSignalIndex=-1;
    private int mLastIndex =-1;

    public void upDataList(int pos)
    {
        mListIndex=pos;
        mVideoWallView. mListIndex=mListIndex;
    }
    public void upDataItem(int pos)
    {
        if (mListIndex==0 || mListIndex==-1){
            /*if (mLastIndex ==1){
                mVideoWallView.closeWindow((byte)0,(byte)0,true);
            }*/
            mSceneIndex=pos;
            mVideoWallView.mSceneIndex = mSceneIndex;
            mVideoWallView = new VideoWallView(getContext(), windowsWidth, windowsHeight, mListIndex, mSceneIndex, mSignalIndex);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.fragment_video_wall_view);
            mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
            ));
        }
        else if (mListIndex == 1){
            mSignalIndex = pos;
            mVideoWallView. mSignalIndex = mSignalIndex;
            if (mSignalIndex==16){
                mVideoWallView.closeWindow((byte)0,(byte)0,true);
                Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
            }
        }
        mLastIndex =mListIndex;
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_video_wall, container, false);
        v = inflater.inflate(R.layout.fragment_video_wall,container,false);

        inflater.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = ((WindowManager)v.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        //windowsWidth=v.getMeasuredWidth();
        //windowsHeight=v.getMeasuredHeight();
        Log.i("Checkout","display: width = " + display.getWidth() + ", height = " + display.getHeight());

//        Log.i("Checkout","container: width = " + container.getWidth() + ", height = " + container.getHeight());

        Log.i("Checkout","inflater: width = " + v.getMeasuredWidth() + ", height = " + v.getHeight());
        mVideoWallView = new VideoWallView(getContext(), windowsWidth, windowsHeight,mListIndex,mSceneIndex,mSignalIndex);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.fragment_video_wall_view);
        mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        return v;
    }


}
