package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

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

    private int m_cellRow=2;
    private int m_cellCol=3;

    public int mListIndex=-1;
    public int mSceneIndex=-1;
    public int mSignalIndex=-1;

    private int mLastSceneIndex =-1;
    private boolean mSceneIsChanged=false;

    private int mLastSignalIndex =-1;
    private boolean mSignalIsChanged=false;

    public int getCellState() {
        return CellState;
    }

    public void setCellState(int cellState) {

        if( cellState < 0 || cellState > CELL_MAX_STATE_NUMBER )
            CellState = CELL_STATE_ERROR;
        else
            CellState = cellState;
    }


    private int WallWidth;
    private int WallHeight;

    private Bitmap CellBitmap;
    private Canvas CellCanvas;
    private Paint CellPaint;

    public VideoWallView(Context context, int wallWidth, int wallHeight,int listIndex,int sceneIndex,int signalIndex) {

        super(context);
        if( wallHeight * wallWidth < 0 ) {
            setCellState(CELL_STATE_ERROR);
            return;
        }
        else {
            WallWidth = wallWidth;
            WallHeight = wallHeight;

            mListIndex=listIndex;
            mSceneIndex=sceneIndex;
            mSignalIndex=signalIndex;

            if (mSceneIndex != mLastSceneIndex)
            {
                mLastSceneIndex =mSceneIndex;
                mSceneIsChanged=true;
            }
            else {
                mSceneIsChanged=false;
            }

            if (mSignalIndex != mLastSignalIndex)
            {
                mLastSignalIndex =mSignalIndex;
                mSignalIsChanged=true;
            }
            else {
                mSignalIsChanged=false;
            }
        }
        if (mListIndex==-1 || mSceneIndex==-1)
        {
            initCell();
        }
        if (mListIndex==0 && mSceneIsChanged==true)
        {
            //// TODO: 2017/11/28 canvas scene
            if (mSceneIndex==0)
            {
                wholeSceneCell();
            }
            if (mSceneIndex==1)
            {
                h2PartSceneCell();
            }
            if (mSceneIndex==2)
            {
                v2PartSceneCell();
            }
            if (mSceneIndex==3)
            {
                allSceneCell();
            }
        }
    }

    private void initCell(){

        SharedPreferences preferences = getContext().getSharedPreferences("setting",Context.MODE_PRIVATE);
        m_cellRow=preferences.getInt("ROW",1);
        m_cellCol=preferences.getInt("COL",1);

        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);

        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(m_cellRow,m_cellCol);
        ArrayList<VideoCell> videoCells = VideoWall.getmVideoCellCollections(m_cellRow,m_cellCol);
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

    private void wholeSceneCell(){

        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(Color.BLUE);
        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(1,1);
        ArrayList<VideoCell> videoCells = VideoWall.getmVideoCellCollections(1,1);
        int i = 0;
        for (VideoCell cell :
                videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(Color.BLUE);
        }
    }

    private void allSceneCell(){

        SharedPreferences preferences = getContext().getSharedPreferences("setting",Context.MODE_PRIVATE);
        m_cellRow=preferences.getInt("ROW",1);
        m_cellCol=preferences.getInt("COL",1);

        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(Color.BLUE);
        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(m_cellRow,m_cellCol);
        ArrayList<VideoCell> videoCells = VideoWall.getmVideoCellCollections(m_cellRow,m_cellCol);
        int i = 0;
        for (VideoCell cell :
                videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(Color.BLUE);
        }
    }

    private void h2PartSceneCell(){

        SharedPreferences preferences = getContext().getSharedPreferences("setting",Context.MODE_PRIVATE);
        m_cellRow=preferences.getInt("ROW",1);
        m_cellCol=preferences.getInt("COL",1);

        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(Color.BLUE);
        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(m_cellRow,m_cellCol);
        ArrayList<VideoCell> videoCells = VideoWall.getH2PartVideoCellCollections(m_cellRow,m_cellCol);
        int i = 0;
        for (VideoCell cell :
                videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(Color.BLUE);
        }
    }
    private void v2PartSceneCell(){

        SharedPreferences preferences = getContext().getSharedPreferences("setting",Context.MODE_PRIVATE);
        m_cellRow=preferences.getInt("ROW",1);
        m_cellCol=preferences.getInt("COL",1);

        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(Color.BLUE);
        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(m_cellRow,m_cellCol);
        ArrayList<VideoCell> videoCells = VideoWall.getV2PartVideoCellCollections(m_cellRow,m_cellCol);
        int i = 0;
        for (VideoCell cell :
                videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(Color.BLUE);
        }
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
            ArrayList<VideoCell> cells = VideoWall.getmVideoCellCollections(m_cellRow, m_cellCol);
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
