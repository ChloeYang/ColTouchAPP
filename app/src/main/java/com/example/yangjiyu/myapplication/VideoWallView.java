package com.example.yangjiyu.myapplication;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.support.annotation.Nullable;
import android.text.method.Touch;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * Created by Howie on 2017/7/25.
 */

public class VideoWallView extends View {

    private static final int  CELL_MAX_STATE_NUMBER = 4;
    public static final int CELL_STATE_ERROR = -1;
    public static final int CELL_STATE_UNKNOWN = 0;
    public static final int CELL_STATE_ACTIVATED = 1;
    public static final int CELL_STATE_BUSY = 2;
    public static final int CELL_STATE_UNANCTIVATED = 3;

    private int CellState = CELL_STATE_UNKNOWN;

    private VideoWall mVideoWall;

    public int getCellState() {
        return CellState;
    }

    public void setCellState(int cellState) {

        if( cellState < 0 || cellState > CELL_MAX_STATE_NUMBER )
            CellState = CELL_STATE_ERROR;
        else
            CellState = cellState;
    }


    private int CellWidth;
    private int CellHeight;

    private Bitmap CellBitmap;
    private Canvas CellCanvas;
    private Paint CellPaint;

    public VideoWallView(Context context, int cellWidth, int cellHeight) {

        super(context);
        if( cellHeight * cellWidth < 0 ) {
            setCellState(CELL_STATE_ERROR);
            return;
        }
        else {
            CellWidth = cellWidth;
            CellHeight = cellHeight;
        }
        initCell();
    }

    private void initCell(){

        CellBitmap = Bitmap.createBitmap(CellWidth, CellHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);

        VideoWall videoWall = VideoWall.newInstance(CellWidth,CellHeight);
        videoWall.layoutVideoCells(3,4);
        ArrayList<VideoCell> videoCells = VideoWall.getmVideoCellCollections(3,4);
        int i = 0;
        for (VideoCell cell :
                videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(Color.WHITE);
            CellCanvas.drawText("   " + i++ +"   ",cell.getCellPositionTopLeftX() + cell.getCellWidth()/2+i,
                    cell.getCellPositionTopLeftY() + cell.getCellHeight()/2+i,CellPaint);
            CellPaint.setColor(Color.BLACK);
        }
        /*VideoCell cell = videoCells.get(1);
        int left = cell.getCellPositionTopLeftX();
        int top = cell.getCellPositionTopLeftY();
        int right = 3;
        int bottom = cell.getCellHeight();*/
        /*CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);*/
       /* CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                cell.getCellWidth(), cell.getCellHeight(), CellPaint);*/
        /*CellCanvas.drawRect(20,30,40,60,CellPaint);
        int d = 20;
        CellCanvas.drawRect(20+d,30+d,40+d,60+d,CellPaint);*/

    }

    private int PositionX;
    private int PositionY;
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(CellBitmap,PositionX, PositionY, CellPaint);
        super.onDraw(canvas);
    }
    public void setColor(int color){
        //CellPaint.setColor(color);
        CellBitmap.eraseColor(color);
    }

    public void setPosition(int x, int y){
        PositionX = x;
        PositionY = y;
    }


    private static int CellNumber = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /*if( event.getAction() == MotionEvent.ACTION_MOVE ){
            setColor(getResources().getColor(R.color.colorCellBusy));
            Log.i("TouchEvent","Action move");
            invalidate();
            return true;
        }else if( event.getAction() == MotionEvent.ACTION_DOWN ){
            setColor(getResources().getColor(R.color.colorCellActivated));
            Log.i("TouchEvent","Action down");
            if( event.getAction() == MotionEvent.ACTION_MOVE ){
                setColor(getResources().getColor(R.color.colorCellBusy));
                Log.i("TouchEvent","Action down & move");
                invalidate();
                return true;
            }*/
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            ArrayList<VideoCell> cells = VideoWall.getmVideoCellCollections(3, 4);
            for (VideoCell cell : cells) {
                if (((x >= cell.getCellPositionTopLeftX()) && x <= (cell.getCellWidth() + cell.getCellPositionTopLeftX()))
                        && ((y >= cell.getCellPositionTopLeftY()) && y <= (cell.getCellHeight() + cell.getCellPositionTopLeftY()))) {

                        //if( CellPaint.getColor() == Color.BLACK )
                            CellPaint.setColor(Color.RED);
                    //else CellPaint.setColor(Color.BLACK);
                    CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                            cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                            cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
                }
            }
            invalidate();
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_DOWN){
            int x = (int) event.getX();
            int y = (int) event.getY();
            ArrayList<VideoCell> cells = VideoWall.getmVideoCellCollections(3, 4);
            for (VideoCell cell : cells) {
                if (((x >= cell.getCellPositionTopLeftX()) && x <= (cell.getCellWidth() + cell.getCellPositionTopLeftX()))
                        && ((y >= cell.getCellPositionTopLeftY()) && y <= (cell.getCellHeight() + cell.getCellPositionTopLeftY()))) {

                    if( CellPaint.getColor() == Color.BLACK)
                        CellPaint.setColor(Color.BLUE);
                    else
                        CellPaint.setColor(Color.BLACK);

                    CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                            cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                            cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
                }
            }
            invalidate();
            return true;
        }else {
            invalidate();
            return super.onTouchEvent(event);
        }
        }/*else{
            setColor(getResources().getColor(R.color.colorCellUnknown));
            Log.i("TouchEvent", "On touch event: " + event.getAction());
        }
        invalidate();
        return super.onTouchEvent(event);
    }*/



}
