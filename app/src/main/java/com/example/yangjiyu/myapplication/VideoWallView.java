package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import commprocess.VCL3CommProcess;

import static android.graphics.Color.YELLOW;

/**
 * Created by Howie on 2017/7/25.
 */

public class VideoWallView extends View {

    private final static String TAG = SceneWall.class.getSimpleName();
    private int m_cellRow=2;
    private int m_cellCol=3;
    public int m_cellWidth=0;
    public int m_cellHeight=0;
    public int mListIndex=-1;
    public int mSceneIndex=-1;
    public int mSignalIndex=-1;

    public int mLastSceneIndex =-1;
    private SharedAppData sharedAppData;


    public interface onCleanDefineSceneClickListener {
        void onCleanDefineScene(int scene);
    }
    public onCleanDefineSceneClickListener mCleanDefineSceneClickListener;

    public int getWallWidth() {
        return WallWidth;
    }

    public int getWallHeight() {
        return WallHeight;
    }

    private int WallWidth;
    private int WallHeight;

    private Bitmap CellBitmap;
    private Canvas CellCanvas;
    private Paint CellPaint;

    public VideoWallView(Context context, int wallWidth, int wallHeight,int listIndex,int sceneIndex,int signalIndex) {

        super(context);
        //sharedAppData=new SharedAppData(context);
        sharedAppData=SharedAppData.newInstance(context);

        mCleanDefineSceneClickListener = (onCleanDefineSceneClickListener) context;
        if( wallHeight * wallWidth < 0 ) {
            return;
        }
        else {
            WallWidth = wallWidth;
            WallHeight = wallHeight;

            mListIndex=listIndex;
            mSceneIndex=sceneIndex;
            mSignalIndex=signalIndex;

        }
        if (mListIndex==-1 && mSceneIndex==-1){
            initCell();
        }

        showVideoWall(sceneIndex);
    }

    private void showVideoWall(int sceneIndex){

        SharedPreferences defineShared = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene),Context.MODE_PRIVATE);//getResources()
        SharedPreferences setPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setPref.edit();
        mLastSceneIndex=setPref.getInt(getContext().getString(R.string.pref_LastSceneIndex),-1);
        //mLastSignalIndex=setPref.getInt(getContext().getString(R.string.pref_LastSignalIndex),-1);
        if (true){
            m_cellRow=sharedAppData.getSystemInfo(1);
            m_cellCol=sharedAppData.getSystemInfo(2);
            m_cellWidth=sharedAppData.getSystemInfo(3);
            m_cellHeight=sharedAppData.getSystemInfo(4);
            //// TODO: 2017/11/28 canvas scene
            if (mSceneIndex==7 && (mLastSceneIndex==4 || mLastSceneIndex==5)){
                sharedAppData.confirmDefinedScene(mLastSceneIndex);
                drawDefinedScene(mLastSceneIndex);
                mCleanDefineSceneClickListener.onCleanDefineScene(mLastSceneIndex);
                mSceneIndex=mLastSceneIndex;
            }else{
                if (sceneIndex==7 && mLastSceneIndex <4){ mSceneIndex=mLastSceneIndex;}
                switch (mSceneIndex)
                {
                    case 0:
                        wholeSceneCell();
                        break;
                    case 1:
                        h2PartSceneCell();
                        break;
                    case 2:
                        v2PartSceneCell();
                        break;
                    case 3:
                        eachSceneCell();
                        break;
                    case 4:
                        //// TODO: 2017/11/29 get sharedpreferences
                        //mDefine1Flag=defineShared.getInt(getContext().getString(R.string.pref_define1_flag),0);
                        if (defineShared.getInt(getContext().getString(R.string.pref_define1_flag),0)==0){
                            initCell();
                        }else {
                            sharedAppData.getDefine1Scene();
                            drawDefinedScene(mSceneIndex);
                        }
                        break;
                    case 5:
                        //// TODO: 2017/11/29 get sharedpreferences
                        //mDefine2Flag=defineShared.getInt(getContext().getString(R.string.pref_define2_flag),0);
                        if (defineShared.getInt(getContext().getString(R.string.pref_define2_flag),0)==0){
                            initCell();
                        }else {
                            sharedAppData.getDefine2Scene();
                            drawDefinedScene(mSceneIndex);
                        }
                        break;
                    case 6:
                        //// TODO: 2017/11/29 clear sharedpreferences
                        if (mLastSceneIndex==4 )
                        {
                            //mDefine1Num=0;
                            Log.i(TAG,"initDefine1Scene");
                            sharedAppData.initDefine1Scene();
                            mCleanDefineSceneClickListener.onCleanDefineScene(mLastSceneIndex);
                            mSceneIndex=mLastSceneIndex;
                        }
                        if(mLastSceneIndex==5)
                        {
                            //mDefine2Num=0;
                            Log.i(TAG,"initDefine2Scene");
                            sharedAppData.initDefine2Scene();
                            mCleanDefineSceneClickListener.onCleanDefineScene(mLastSceneIndex);
                            mSceneIndex=mLastSceneIndex;
                        }
                        initCell();
                        break;
                    default:
                        initCell();
                        break;
                }
            }

            mLastSceneIndex=mSceneIndex;
            editor.putInt(getContext().getString(R.string.pref_LastSceneIndex), mLastSceneIndex);
            editor.putInt(getContext().getString(R.string.pref_LastSignalIndex), mLastSceneIndex);
            editor.commit();

        }
    }
    private void initCell(){
        //getSystemInfo();
        if (CellBitmap==null) {
            CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        }
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        if (CellCanvas==null) {
            CellCanvas = new Canvas(CellBitmap);
        }
        if (CellPaint==null) {
            CellPaint = new Paint(Paint.DITHER_FLAG);
        }

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
            CellPaint.setTextSize(16);
            CellCanvas.drawText("   " + ++i +"   ",cell.getCellPositionTopLeftX() + cell.getCellWidth()/2+i,
                    cell.getCellPositionTopLeftY() + cell.getCellHeight()/2+i,CellPaint);
            CellPaint.setColor(Color.BLACK);
        }

        sharedAppData.saveVideoCellList(videoCells);

        m_cellWidth=sharedAppData.getSystemInfo(3);
        m_cellHeight=sharedAppData.getSystemInfo(4);
    }

    private void wholeSceneCell(){
        //getSystemInfo();
        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(getResources().getColor(R.color.darkblue));
        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(1,1);
        ArrayList<VideoCell> videoCells = VideoWall.getmVideoCellCollections(1,1);
        int i = 0;
        for (VideoCell cell :videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(getResources().getColor(R.color.darkblue));
            sharedAppData.saveDefaultScene(getContext().getString(R.string.pref_whole_scene),i, cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight());
            i=i+1;
        }
    }

    private void eachSceneCell(){
        //getSystemInfo();
        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(getResources().getColor(R.color.darkblue));
        int i=0;
        SceneWall sceneWall = SceneWall.newInstance(WallWidth, WallHeight);
        ArrayList<SingleSceneCell> singleSceneCells=sceneWall.getEchSceneCellCollections(m_cellRow,m_cellCol);
        for (SingleSceneCell cell:singleSceneCells){
            CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY(), CellPaint);
            CellPaint.setColor(getResources().getColor(R.color.darkblue));
            sharedAppData.saveDefaultScene(getContext().getString(R.string.pref_each_scene),i, cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY());
            i=i+1;
        }
    }

    private void h2PartSceneCell(){
        //getSystemInfo();
        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(getResources().getColor(R.color.darkblue));
        int i=0;
        SceneWall sceneWall = SceneWall.newInstance(WallWidth, WallHeight);
        ArrayList<SingleSceneCell> singleSceneCells=sceneWall.getH2PartSceneCellCollections(m_cellRow,m_cellCol);
        for (SingleSceneCell cell:singleSceneCells){
            CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY(), CellPaint);
            CellPaint.setColor(getResources().getColor(R.color.darkblue));
            sharedAppData.saveDefaultScene(getContext().getString(R.string.pref_h2part_scene),i, cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY());
            i=i+1;
        }
    }
    private void v2PartSceneCell(){
        //getSystemInfo();
        CellBitmap = Bitmap.createBitmap(WallWidth, WallHeight, Bitmap.Config.ARGB_8888);
        CellBitmap.eraseColor(getResources().getColor(R.color.colorCellUnknown));
        CellCanvas = new Canvas(CellBitmap);
        CellPaint = new Paint(Paint.DITHER_FLAG);
        CellPaint.setColor(getResources().getColor(R.color.darkblue));
        int i=0;
        SceneWall sceneWall = SceneWall.newInstance(WallWidth, WallHeight);
        ArrayList<SingleSceneCell> singleSceneCells=sceneWall.getV2PartSceneCellCollections(m_cellRow,m_cellCol);
        for (SingleSceneCell cell:singleSceneCells){
            CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY(), CellPaint);
            CellPaint.setColor(getResources().getColor(R.color.darkblue));
            sharedAppData.saveDefaultScene(getContext().getString(R.string.pref_v2part_scene),i, cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY());
            i=i+1;
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

    public void drawCanvasText(String str,int start_X,int start_Y,int end_X,int end_Y){
        CellPaint.setColor(Color.WHITE);
        CellPaint.setTextSize(30);
        CellCanvas.drawText("   " + str + "   ", (start_X + end_X) / 2-50, (start_Y + end_Y) / 2-10, CellPaint);

    }
    public void drawCanvasBase() {
        initCell();
    }
    public void drawCanvasRect(int start_X,int start_Y,int end_X,int end_Y){
        CellPaint.setColor(getResources().getColor(R.color.darkblue));
        CellCanvas.drawRect(start_X, start_Y,end_X, end_Y, CellPaint);
    }

    public void drawTempRect(int start_X,int start_Y,int end_X,int end_Y){
        CellPaint.setColor(Color.RED);
        CellCanvas.drawRect(start_X, start_Y,end_X,end_Y, CellPaint);
    }

    public void drawDefinedScene(int sceneIndex){
        initCell();
        if (sceneIndex==4){
            for (SingleSceneCell cell:sharedAppData.getDefine1Scene()){
                CellPaint.setColor(getResources().getColor(R.color.darkblue));
                CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),
                        cell.getM_endX(), cell.getM_endY(), CellPaint);
            }
        }else if (sceneIndex==5){
            for (SingleSceneCell cell:sharedAppData.getDefine2Scene()){
                CellPaint.setColor(getResources().getColor(R.color.darkblue));
                CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),
                        cell.getM_endX(), cell.getM_endY(), CellPaint);
            }
        }else{}
        invalidate();
    }
}
