package com.example.yangjiyu.myapplication;

import android.util.Log;

import java.util.UUID;

/**
 * Created by Howie on 2017/7/31.
 */

public class VideoCell {

    private final static String TAG = "vtron.dlpmini.VideoCell";

    public final static int sSuccess = 0;
    public final static int sFailure = -1;

    private final static int sCellStateNum = 5;
    public final static int sCellStateReady = 0;
    public final static int sCellStateActivated = 1;
    public final static int sCellStateUnknown = 2;
    public final static int sCellStateBusy = 3;
    public final static int sCellStateError = 4;


    private UUID mCellId;
    private int mCellWidth;
    private int mCellHeight;
    private int mCellState = sCellStateUnknown;
    private int mCellPositionTopLeftX;
    private int mCellPositionTopLeftY;



    public VideoCell(int cellWidth, int cellHeight) {
        if( cellHeight * cellHeight <= 0 ){
            setCellState(sCellStateError);
        }else{
            setCellState(sCellStateBusy);
            mCellWidth = cellWidth;
            mCellHeight = cellHeight;
            mCellId = UUID.randomUUID();
            setCellState(sCellStateReady);
        }
    }

    public UUID getCellId() {
        return mCellId;
    }

    public int setVideoCellPosition(int posX, int posY){
        if( posX * posY < 0 ){
            return sFailure;
        }else {
            Log.i(TAG,"VideoCell position: x = " + posX + ", y = " + posY);
        }
        mCellPositionTopLeftX = posX;
        mCellPositionTopLeftY = posY;
        return sSuccess;
    }

    public int getCellPositionTopLeftX(){
        return mCellPositionTopLeftX;
    }

    public int getCellPositionTopLeftY(){
        return mCellPositionTopLeftY;
    }




    public int getCellState() {
        return mCellState;
    }

    private int setCellState(int cellState) {
        if(cellState > sCellStateNum) {
            Log.e(TAG,"CellState no more than " + sCellStateNum);
            return sFailure;
        } else {
            mCellState = cellState;
            return sSuccess;
        }
    }

    public int getCellWidth() {
        if( sCellStateReady == getCellState() )
            return mCellWidth;
        else
            return sFailure;
    }

    public int setCellWidth(int cellWidth) {

        if( 0 >= cellWidth ){
            return sFailure;
        }else{
            mCellWidth = cellWidth;
            return sSuccess;
        }
    }

    public int getCellHeight() {

        if( sCellStateReady == getCellState() )
            return mCellHeight;
        else
            return sFailure;
    }

    public int setCellHeight(int cellHeight) {

        if( 0 >= cellHeight ){
            return sFailure;
        } else {
            mCellHeight = cellHeight;
            return sSuccess;
        }
    }

}
