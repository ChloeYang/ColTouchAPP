package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by Howie on 2017/7/25.
 */

public class VideoWall {

    private final static String TAG = "vtron.VideoWall";
    private final static int MAX_SUPPORT_CELL = 12;
    private final static int MAX_SUPPORT_WALL = 1;

    private final static float sVideoCellSizeWidthToHeightRatioSmall = 4 / 3;
    private final static float sVideoCellSizeWidthToHeightRatioLarge = 16 / 9;
    public final static int sVideoCellGap = 2;

    public final static int sSuccess = 0;
    public final static int sFailure = -1;

    private static VideoWall sVideoWall;
    private static ArrayList<VideoCell> sVideoCellCollections;

    private int mWallWidth;
    private int mWallHeight;
    private VideoCell mVideoCell;

    public static VideoWall newInstance(int wallWidth, int wallHeight){
        if(sVideoWall != null){
            if( wallWidth * wallHeight <= 0 ){
                return null;
            }else{
                return sVideoWall;
            }
        }else{
            sVideoWall= new VideoWall(wallWidth, wallHeight);
            return sVideoWall;
        }
    }


    private VideoWall(int width, int height) {

        if( height * width <= 0 ) {
            Log.e(TAG, "VideoWall size: " + width + " x " + height);
        }
        else {
            mWallWidth  = width;
            mWallHeight = height;
        }
    }


    public int layoutVideoCells(int rows, int columns){

        int VideoCellNumber = rows * columns;

        if( VideoCellNumber <= 0 ||
                VideoCellNumber > MAX_SUPPORT_CELL){
            Log.e(TAG,"VideoCell number more than the max support number: " + VideoCellNumber);
            return sFailure;
        }else {
            //Log.i(TAG,"Begin to layout: rows = " + rows + ", columns = " + columns);
        }

        int VideoCellWidth = (int) Math.floor((getWallWidth() - sVideoCellGap * (columns  - 1 )) / columns);
        int VideoCellHeight = (int) Math.floor((getWallHeight() - sVideoCellGap * (rows  - 1 )) / rows);


        //ToDo: takes time to modify the shape of video shape
        //int VideoCellWidthTmp = (int) Math.floor(VideoCellHeight * sVideoCellSizeWidthToHeightRatioSmall);
        //int VideoCellHeightTmp = (int) Math.floor(VideoCellWidth / sVideoCellSizeWidthToHeightRatioSmall);
        sVideoCellCollections = new ArrayList<>();
        int posX = 0;
        int posY = 0;
        for( int i = 0; i < rows; i++ ){
            posY = i * (VideoCellHeight + sVideoCellGap);

            for( int j = 0; j < columns; j++){
                mVideoCell = new VideoCell(VideoCellWidth, VideoCellHeight);
                posX = j * (VideoCellWidth + sVideoCellGap);
                mVideoCell.setVideoCellPosition(posX, posY);
                mVideoCell.setCellId(i,j);
                sVideoCellCollections.add(mVideoCell);
                //Log.i(TAG,"Add video cell: x = " + posX + ", y = " + posY);
            }
            posX = 0;
        }
        //Log.i(TAG, "VideoCell number in VideoWall: " + sVideoCellCollections.size());
        return sSuccess;
    }

    public static ArrayList<VideoCell> getmVideoCellCollections(int rows, int colums) {
        if( sVideoCellCollections != null ){
            return sVideoCellCollections;
        }else {
            if( sSuccess == sVideoWall.layoutVideoCells(rows, colums) ){
                return sVideoCellCollections;
            }else {
                return null;
            }
        }
    }


    /*{
        if( sVideoCellCollections.size() == 0 ){
            Log.e(TAG,"Do not add any video cell yet!");
            return sFailure;
        }else {
            Log.i(TAG,"VideoCell number is: " + sVideoCellCollections.size()
                    +", begin to layout...");
        }
    }*/

    public int getWallWidth() {
        return mWallWidth;
    }

    public int setWallWidth(int wallWidth) {
        if( 0 <= wallWidth ) {
            return sFailure;
        }else{
            mWallWidth = wallWidth;
            return sSuccess;
        }
    }

    public int getWallHeight() {
        return mWallHeight;
    }

    public int setWallHeight(int wallHeight) {
        if( 0 >= wallHeight ){
            return sFailure;
        }else {
            mWallHeight = wallHeight;
            return sSuccess;
        }
    }
}
