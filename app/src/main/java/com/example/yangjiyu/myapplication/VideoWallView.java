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

/**
 * Created by Howie on 2017/7/25.
 */

public class VideoWallView extends View {
    public static final int INPUT_BOARD_NUM=4;
    public static final int OUTPUT_BOARD_NUM=3;
    public static final int WIN_INTER=12;
    private final static String TAG = SceneWall.class.getSimpleName();
    private static final int  CELL_MAX_STATE_NUMBER = 4;
    public static final int CELL_STATE_ERROR = -1;
    public static final int CELL_STATE_UNKNOWN = 0;
    private int CellState = CELL_STATE_UNKNOWN;

    private VideoWall mVideoWall;

    private int m_cellRow=2;
    private int m_cellCol=3;
    public int m_cellWidth=0;
    public int m_cellHeight=0;
    public int mListIndex=-1;
    public int mSceneIndex=-1;
    public int mSignalIndex=-1;

    public int mLastSceneIndex =-1;
    private boolean mSceneIsChanged=false;

    private int mLastSignalIndex =-1;
    private boolean mSignalIsChanged=false;

    private VCL3CommProcess mVcl3CommProces;
    private int m_pixX=1024;
    private int m_pixY=768;

    private SharedAppData sharedAppData;
    public int getCellState() {
        return CellState;
    }

    public interface onCleanDefineSceneClickListener {
        void onCleanDefineScene(int scene);
    }
    private onCleanDefineSceneClickListener mCleanDefineSceneClickListener;

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

    private int mDefine1Flag;
    private int mDefine2Flag;
    private int mDefine1Num;
    private int mDefine2Num;
    private ArrayList<SingleSceneCell> m_define1_sceneList=new ArrayList<>();
    private ArrayList<SingleSceneCell> m_define2_sceneList=new ArrayList<>();


    public VideoWallView(Context context, int wallWidth, int wallHeight,int listIndex,int sceneIndex,int signalIndex) {

        super(context);
        //sharedAppData=new SharedAppData(context);
        sharedAppData=SharedAppData.newInstance(context);

        mCleanDefineSceneClickListener = (onCleanDefineSceneClickListener) context;
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

            if (mSceneIndex != mLastSceneIndex){
                mSceneIsChanged=true;
            }
            else {
                mSceneIsChanged=false;
            }

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
        mLastSignalIndex=setPref.getInt(getContext().getString(R.string.pref_LastSignalIndex),-1);
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
                        mDefine1Flag=defineShared.getInt(getContext().getString(R.string.pref_define1_flag),0);
                        if (mDefine1Flag==0){
                            initCell();
                        }else {
                            sharedAppData.getDefine1Scene();
                            drawDefinedScene(mSceneIndex);
                        }
                        break;
                    case 5:
                        //// TODO: 2017/11/29 get sharedpreferences
                        mDefine2Flag=defineShared.getInt(getContext().getString(R.string.pref_define2_flag),0);
                        if (mDefine2Flag==0){
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
                            mDefine1Num=0;
                            Log.i(TAG,"initDefine1Scene");
                            sharedAppData.initDefine1Scene();
                            mCleanDefineSceneClickListener.onCleanDefineScene(mLastSceneIndex);
                            mSceneIndex=mLastSceneIndex;
                        }
                        if(mLastSceneIndex==5)
                        {
                            mDefine2Num=0;
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
            CellCanvas.drawText("   " + i++ +"   ",cell.getCellPositionTopLeftX() + cell.getCellWidth()/2+i,
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
        CellPaint.setColor(Color.BLUE);
        VideoWall videoWall = VideoWall.newInstance(WallWidth, WallHeight);
        videoWall.layoutVideoCells(1,1);
        ArrayList<VideoCell> videoCells = VideoWall.getmVideoCellCollections(1,1);
        int i = 0;
        for (VideoCell cell :videoCells) {
            CellCanvas.drawRect(cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight(), CellPaint);
            CellPaint.setColor(Color.BLUE);
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
        CellPaint.setColor(Color.BLUE);
        int i=0;
        SceneWall sceneWall = SceneWall.newInstance(WallWidth, WallHeight);
        ArrayList<SingleSceneCell> singleSceneCells=sceneWall.getEchSceneCellCollections(m_cellRow,m_cellCol);
        for (SingleSceneCell cell:singleSceneCells){
            CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY(), CellPaint);
            CellPaint.setColor(Color.BLUE);
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
        CellPaint.setColor(Color.BLUE);
        int i=0;
        SceneWall sceneWall = SceneWall.newInstance(WallWidth, WallHeight);
        ArrayList<SingleSceneCell> singleSceneCells=sceneWall.getH2PartSceneCellCollections(m_cellRow,m_cellCol);
        for (SingleSceneCell cell:singleSceneCells){
            CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY(), CellPaint);
            CellPaint.setColor(Color.BLUE);
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
        CellPaint.setColor(Color.BLUE);
        int i=0;
        SceneWall sceneWall = SceneWall.newInstance(WallWidth, WallHeight);
        ArrayList<SingleSceneCell> singleSceneCells=sceneWall.getV2PartSceneCellCollections(m_cellRow,m_cellCol);
        for (SingleSceneCell cell:singleSceneCells){
            CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY(), CellPaint);
            CellPaint.setColor(Color.BLUE);
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
        CellCanvas.drawText("   " + str + "   ", (start_X + end_X) / 2, (start_Y + end_Y) / 2-10, CellPaint);

    }
    public void drawCanvasRect(int start_X,int start_Y,int end_X,int end_Y){
        CellPaint.setColor(Color.BLUE);
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
                CellPaint.setColor(Color.BLUE);
                CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),
                        cell.getM_endX(), cell.getM_endY(), CellPaint);
            }
        }else if (sceneIndex==5){
            for (SingleSceneCell cell:sharedAppData.getDefine2Scene()){
                CellPaint.setColor(Color.BLUE);
                CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),
                        cell.getM_endX(), cell.getM_endY(), CellPaint);
            }
        }else{}
        invalidate();
    }

    public void stopVCL3Comm(){
        try {
            mVcl3CommProces.ProcessCancel();
            mVcl3CommProces=null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getContext(),""+e,Toast.LENGTH_SHORT).show();
        }
    }


}
