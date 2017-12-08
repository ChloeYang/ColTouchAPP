package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
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
    private final static String TAG = SceneWall.class.getSimpleName();
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
    private int m_cellWidth=0;
    private int m_cellHeight=0;
    public int mListIndex=-1;
    public int mSceneIndex=-1;
    public int mSignalIndex=-1;

    private int mLastSceneIndex =-1;
    private boolean mSceneIsChanged=false;

    private int mLastSignalIndex =-1;
    private boolean mSignalIsChanged=false;

    private VCL3CommProcess mVcl3CommProces;
    private String mVclordIp="";
    private int mSignalWindowCount =0;//start from 1
    private int m_pixX=1024;
    private int m_pixY=768;

    public int getCellState() {
        return CellState;
    }

    public interface onCleanDefineScenelClickListener{
        void onCleanDefineScene(int scene);
    }
    private VideoWallView.onCleanDefineScenelClickListener mCleanDefineScenelClickListener;

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

    int start_x=0;
    int start_y=0;
    int end_x=0;
    int end_y=0;

    String[] StringSignal={getContext().getString(R.string.DVI_1_1),getContext().getString(R.string.DVI_1_2),getContext().getString(R.string.HDMI_1),getContext().getString(R.string.DP_1),
            getContext().getString(R.string.DVI_2_1),getContext().getString(R.string.DVI_2_2),getContext().getString(R.string.HDMI_2),getContext().getString(R.string.DP_2),
            getContext().getString(R.string.DVI_3_1),getContext().getString(R.string.DVI_3_2),getContext().getString(R.string.HDMI_3),getContext().getString(R.string.DP_3),
            getContext().getString(R.string.DVI_4_1),getContext().getString(R.string.DVI_4_2),getContext().getString(R.string.HDMI_4),getContext().getString(R.string.DP_4),
            getContext().getString(R.string.clear)};

    public VideoWallView(Context context, int wallWidth, int wallHeight,int listIndex,int sceneIndex,int signalIndex) {

        super(context);


        mCleanDefineScenelClickListener = (VideoWallView.onCleanDefineScenelClickListener) context;
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
            //// TODO: 2017/11/28 canvas scene
            if (mSceneIndex==7 && (mLastSceneIndex==4 || mLastSceneIndex==5)){
                confirmDefinedScene(mLastSceneIndex);
                mCleanDefineScenelClickListener.onCleanDefineScene(mLastSceneIndex);
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
                            getDefine1Scene();
                            drawDefinedScene(mSceneIndex);
                        }
                        break;
                    case 5:
                        //// TODO: 2017/11/29 get sharedpreferences
                        mDefine2Flag=defineShared.getInt(getContext().getString(R.string.pref_define2_flag),0);
                        if (mDefine2Flag==0){
                            initCell();
                        }else {
                            getDefine2Scene();
                            drawDefinedScene(mSceneIndex);
                        }
                        break;
                    case 6:
                        //// TODO: 2017/11/29 clear sharedpreferences
                        if (mLastSceneIndex==4 )
                        {
                            mDefine1Num=0;
                            Log.i(TAG,"initDefine1Scene");
                            initDefine1Scene();
                            mCleanDefineScenelClickListener.onCleanDefineScene(mLastSceneIndex);
                            mSceneIndex=mLastSceneIndex;
                        }
                        if(mLastSceneIndex==5)
                        {
                            mDefine2Num=0;
                            Log.i(TAG,"initDefine2Scene");
                            initDefine2Scene();
                            mCleanDefineScenelClickListener.onCleanDefineScene(mLastSceneIndex);
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

        getSystemInfo();
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
        saveVideoCellList(videoCells);

    }

    private void wholeSceneCell(){
        getSystemInfo();
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
            saveDefaultScene(getContext().getString(R.string.pref_whole_scene),i, cell.getCellPositionTopLeftX(), cell.getCellPositionTopLeftY(),
                    cell.getCellPositionTopLeftX() + cell.getCellWidth(),
                    cell.getCellPositionTopLeftY() + cell.getCellHeight());
            i=i+1;
        }
    }

    private void eachSceneCell(){
        getSystemInfo();
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
            saveDefaultScene(getContext().getString(R.string.pref_each_scene),i, cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY());
            i=i+1;
        }
    }

    private void h2PartSceneCell(){
        getSystemInfo();
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
            saveDefaultScene(getContext().getString(R.string.pref_h2part_scene),i, cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY());
            i=i+1;
        }
    }
    private void v2PartSceneCell(){
        getSystemInfo();
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
            saveDefaultScene(getContext().getString(R.string.pref_v2part_scene),i, cell.getM_startX(), cell.getM_startY(),cell.getM_endX(),cell.getM_endY());
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

    private ArrayList<SingleSceneCell> getSceneCell(){
        ArrayList<SingleSceneCell> sceneCells = new ArrayList<>();
        switch (mLastSceneIndex){
            case 0:
                sceneCells =getDefaultScene(getContext().getString(R.string.pref_whole_scene));
                break;
            case 1:
                sceneCells=getDefaultScene(getContext().getString(R.string.pref_h2part_scene));
                break;
            case 2:
                sceneCells=getDefaultScene(getContext().getString(R.string.pref_v2part_scene));
                break;
            case 3:
                sceneCells=getDefaultScene(getContext().getString(R.string.pref_each_scene));
                break;
            case 4:
                sceneCells=getDefine1Scene();
                break;
            case 5:
                sceneCells=getDefine2Scene();
                break;
            default:
                break;
        }
        return sceneCells;
    }
    private static int CellNumber = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int flag1=0;
        int flag2=0;
        byte winId=1;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                start_x = (int) Math.floor((int)event.getX()/m_cellWidth) * (m_cellWidth + VideoWall.sVideoCellGap);
                start_y = (int) Math.floor((int)event.getY()/m_cellHeight) * (m_cellHeight+ VideoWall.sVideoCellGap);
                Log.i("TouchEvent","ACTION_DOWN start_x="+start_x+" start_y="+start_y);

                /*if (mSignalIndex != mLastSignalIndex){
                    mSignalIsChanged=true;
                }
                else {
                    mSignalIsChanged=false;
                }*/
                if (mSignalIndex>=0 && mSignalIndex<StringSignal.length /*&& mSignalIsChanged*/){
                    SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting),Context.MODE_PRIVATE);
                    mSignalWindowCount =preferences.getInt(getContext().getString(R.string.pref_signal_window_num),0);
                    m_pixX=preferences.getInt(getContext().getString(R.string.pref_pix_x),1024);
                    m_pixY=preferences.getInt(getContext().getString(R.string.pref_pix_y),768);
                    ArrayList<SingleSceneCell> sceneCells = new ArrayList<>();
                    sceneCells = getSceneCell();
                    winId=(byte)(mSignalWindowCount);
                    for (SingleSceneCell scene_cell :sceneCells){
                        if (((int)event.getX() >= scene_cell.getM_startX() && (int)event.getX() <= scene_cell.getM_endX()) && ((int)event.getY() >= scene_cell.getM_startY() && (int)event.getY() <= scene_cell.getM_endY())){
                            CellPaint.setColor(Color.BLUE);
                            CellCanvas.drawRect(scene_cell.getM_startX(), scene_cell.getM_startY(),scene_cell.getM_endX(), scene_cell.getM_endY(), CellPaint);

                            if (mSignalIndex!=StringSignal.length-1) {
                                CellPaint.setColor(Color.WHITE);
                                CellPaint.setTextSize(30);
                                CellCanvas.drawText("   " + StringSignal[mSignalIndex] + "   ", (scene_cell.getM_startX() + scene_cell.getM_endX()) / 2, (scene_cell.getM_startY() + scene_cell.getM_endY()) / 2, CellPaint);

                                scene_cell.setM_signal(mSignalIndex);
                                //// TODO: 2017/12/4 save signal to sharedpreferences && send cmd to engine
                                //byte winId=(byte)((mSignalWindowCount+1)&0xff);
                                byte inputId = (byte) (mSignalIndex / INPUT_BOARD_NUM);
                                byte sigNum = (byte) (mSignalIndex % INPUT_BOARD_NUM);
                                short posX = (short) ((scene_cell.getM_startX() / m_cellWidth) * m_pixX);
                                short posY = (short) ((scene_cell.getM_startY() / m_cellHeight) * m_pixY);
                                short widthX = (short) (((scene_cell.getM_endX() - scene_cell.getM_startX()) / m_cellWidth) * m_pixX);
                                short heightY = (short) (((scene_cell.getM_endY() - scene_cell.getM_startY()) / m_cellHeight) * m_pixY);
                                winId++;
                                closeWindow(winId, (byte) 0, false);
                                boolean ret = openWindow(winId, inputId, sigNum, posX, posY, widthX, heightY);
                                if (ret) {
                                    Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), R.string.error_open_signal_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
                /*mLastSignalIndex=mSignalIndex;*/
                mSignalIndex=-1;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                Log.i("TouchEvent","mDefine1Num="+mDefine1Num+" mDefine2Num="+mDefine2Num);
                end_x=(int) Math.floor((int)event.getX()/m_cellWidth) * (m_cellWidth + VideoWall.sVideoCellGap)+m_cellWidth;
                end_y=(int) Math.floor((int)event.getY()/m_cellHeight) * (m_cellHeight+ VideoWall.sVideoCellGap)+m_cellHeight;
                //Log.i("TouchEvent","ACTION_UP endx="+end_x+" endy="+end_y);
                if(mSceneIndex==4 ) {
                    ArrayList<SingleSceneCell> cells=getDefine1Scene();
                    if (mDefine1Flag==1){ return false;}
                    for (SingleSceneCell cell : cells) {
                        if ((start_x >= cell.getM_startX() && start_x <= cell.getM_endX()) && (start_y >= cell.getM_startY() && start_y <= cell.getM_endY())){
                            Log.i("TouchEvent","startXY is covered");
                            return false;
                        }
                        else if ((end_x >= cell.getM_startX() && end_x <= cell.getM_endX()) && (end_y >= cell.getM_startY() && end_y <= cell.getM_endY())) {
                            Log.i("TouchEvent","endXY is covered");
                            return false;
                        }
                    }
                    //Toast.makeText(getContext(), "save", Toast.LENGTH_SHORT).show();
                    saveDefine1Scene(flag1, mDefine1Num+1, start_x, start_y, end_x, end_y);
                    /*if (start_x>end_x){
                        int temp=start_x;
                        start_x=end_x;
                        end_x=temp;

                        temp=start_y;
                        start_y=end_y;
                        end_y=temp;
                    }else if (start_y>end_y){
                        int temp=start_y;
                        start_y=end_y;
                        end_y=temp;

                        temp=start_x;
                        start_x=end_x;
                        end_x=temp;
                    }*/
                    CellPaint.setColor(Color.RED);
                    CellCanvas.drawRect(start_x, start_y,end_x,end_y, CellPaint);
                }else if(mSceneIndex==5 ) {
                    ArrayList<SingleSceneCell> cells=getDefine2Scene();
                    if (mDefine2Flag==1){ return false;}
                    for (SingleSceneCell cell:cells){
                        if ((start_x >= cell.getM_startX() && start_x <= cell.getM_endX()) && (start_y >= cell.getM_startY() && start_y <= cell.getM_endY())){
                            Log.i("TouchEvent","startXY is covered");
                            return false;
                        }
                        else if ((end_x>=cell.getM_startX()&&end_x<=cell.getM_endX()) && (end_y>=cell.getM_startY()&&end_y<=cell.getM_endY()))
                        {
                            Log.i("TouchEvent","endXY is covered");
                            return false;
                        }
                    }
                    saveDefine2Scene(flag2,mDefine2Num+1,start_x,start_y,end_x,end_y);
                    /*if (start_x>end_x){
                        int temp=start_x;
                        start_x=end_x;
                        end_x=temp;

                        temp=start_y;
                        start_y=end_y;
                        end_y=temp;
                    }else if (start_y>end_y){
                        int temp=start_y;
                        start_y=end_y;
                        end_y=temp;

                        temp=start_x;
                        start_x=end_x;
                        end_x=temp;
                    }*/
                    CellPaint.setColor(Color.RED);
                    CellCanvas.drawRect(start_x, start_y,end_x,end_y, CellPaint);
                }else {
                    break;
                }

                invalidate();
                return true;

            default:
                break;
        }

        invalidate();
        return true;
    }

    public void getSystemInfo()
    {
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting),Context.MODE_PRIVATE);
        m_cellRow=preferences.getInt(getContext().getString(R.string.pref_data_row),1);
        m_cellCol=preferences.getInt(getContext().getString(R.string.pref_data_col),1);
        m_cellWidth=preferences.getInt(getContext().getString(R.string.pref_width),1);
        m_cellHeight=preferences.getInt(getContext().getString(R.string.pref_height),1);
    }
    public void saveVideoCellList(ArrayList<VideoCell> videoCells) {
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_video_cell_list), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int iCellNum = videoCells.size();
        editor.putInt(getContext().getString(R.string.pref_cubeNum), iCellNum);
        for (int index = 0; index < iCellNum; index++) {
            editor.putInt(getContext().getString(R.string.pref_cubeId)+index, videoCells.get(index).getCellId());
            editor.putInt(getContext().getString(R.string.pref_posX)+index, videoCells.get(index).getCellPositionTopLeftX());
            editor.putInt(getContext().getString(R.string.pref_posY)+index, videoCells.get(index).getCellPositionTopLeftY());
            editor.putInt(getContext().getString(R.string.pref_width)+index, videoCells.get(index).getCellWidth());
            editor.putInt(getContext().getString(R.string.pref_height)+index, videoCells.get(index).getCellHeight());
            m_cellWidth=videoCells.get(index).getCellWidth();
            m_cellHeight=videoCells.get(index).getCellHeight();
        }
        editor.commit();

        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting),Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(getContext().getString(R.string.pref_width),m_cellWidth);
        ed.putInt(getContext().getString(R.string.pref_height),m_cellHeight);
        ed.commit();
    }
    public ArrayList<VideoCell> getVideoCellList(){
        ArrayList<VideoCell> videoCells =new ArrayList<>();
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_video_cell_list), Context.MODE_PRIVATE);
        int iCellNum = sharedPref.getInt(getContext().getString(R.string.pref_cubeNum),0);
        for (int index = 0; index < iCellNum; index++) {
            VideoCell videoCell = new VideoCell(m_cellWidth,m_cellHeight);
            videoCell.setCellId(m_cellRow,m_cellCol);
            videoCell.setVideoCellPosition(sharedPref.getInt(getContext().getString(R.string.pref_posX)+index, 0),sharedPref.getInt(getContext().getString(R.string.pref_posY)+index, 0));
            videoCell.setCellWidth(sharedPref.getInt(getContext().getString(R.string.pref_width)+index,0));
            videoCell.setCellHeight(sharedPref.getInt(getContext().getString(R.string.pref_height)+index,0));
            videoCells.add(videoCell);
        }
        return videoCells;
    }

    public void saveDefaultScene(String str,int num, int start_x,int start_y,int end_x,int end_y){
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getContext().getString(R.string.pref_default_num)+str,num+1);
        editor.putInt(getContext().getString(R.string.pref_default_startX_)+str+"_"+num,start_x);
        editor.putInt(getContext().getString(R.string.pref_default_startY_)+str+"_"+num,start_y);
        editor.putInt(getContext().getString(R.string.pref_default_endX_)+str+"_"+num,end_x);
        editor.putInt(getContext().getString(R.string.pref_default_endY_)+str+"_"+num,end_y);
        editor.commit();
    }
    public ArrayList<SingleSceneCell> getDefaultScene(String str){
        ArrayList<SingleSceneCell> cells = new ArrayList<>();
        SharedPreferences defineShared = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        int num=defineShared.getInt(getContext().getString(R.string.pref_default_num)+str,0);
        Log.i(TAG,str+"_scene num="+num);
        for (int index=0;index<num;index++){
            SingleSceneCell ss =new SingleSceneCell();
            ss.setM_startX(defineShared.getInt(getContext().getString(R.string.pref_default_startX_)+str+"_"+index,0));
            ss.setM_startY(defineShared.getInt(getContext().getString(R.string.pref_default_startY_)+str+"_"+index,0));
            ss.setM_endX(defineShared.getInt(getContext().getString(R.string.pref_default_endX_)+str+"_"+index,0));
            ss.setM_endY(defineShared.getInt(getContext().getString(R.string.pref_default_endY_)+str+"_"+index,0));
            ss.setM_signal(defineShared.getInt(getContext().getString(R.string.pref_default_signal_)+str+"_"+index,0));
            cells.add(ss);
        }
        return cells;
    }
    public void saveDefine1Scene(int flag,int numd, int start_x,int start_y,int end_x,int end_y){
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getContext().getString(R.string.pref_define1_flag),flag);
        editor.putInt(getContext().getString(R.string.pref_define1_num),numd);
        int num = numd-1;
        editor.putInt(getContext().getString(R.string.pref_define1_startX_)+num,start_x);
        editor.putInt(getContext().getString(R.string.pref_define1_startY_)+num,start_y);
        editor.putInt(getContext().getString(R.string.pref_define1_endX_)+num,end_x);
        editor.putInt(getContext().getString(R.string.pref_define1_endY_)+num,end_y);
       editor.commit();
    }
    public void saveDefine2Scene(int flag,int numd, int start_x,int start_y,int end_x,int end_y){
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getContext().getString(R.string.pref_define2_flag),flag);
        editor.putInt(getContext().getString(R.string.pref_define2_num),numd);
        int num = numd-1;
        editor.putInt(getContext().getString(R.string.pref_define2_startX_)+num,start_x);
        editor.putInt(getContext().getString(R.string.pref_define2_startY_)+num,start_y);
        editor.putInt(getContext().getString(R.string.pref_define2_endX_)+num,end_x);
        editor.putInt(getContext().getString(R.string.pref_define2_endY_)+num,end_y);
        editor.commit();
    }
    public ArrayList<SingleSceneCell> getDefine1Scene(){
        SharedPreferences defineShared = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        mDefine1Flag=defineShared.getInt(getContext().getString(R.string.pref_define1_flag),0);
        mDefine1Num=defineShared.getInt(getContext().getString(R.string.pref_define1_num),0);
        m_define1_sceneList.clear();
        Log.i(TAG,"mDefine1Num="+mDefine1Num);
        for (int index=0;index<mDefine1Num;index++){
            SingleSceneCell ss =new SingleSceneCell();
            ss.setM_startX(defineShared.getInt(getContext().getString(R.string.pref_define1_startX_)+index,0));
            ss.setM_startY(defineShared.getInt(getContext().getString(R.string.pref_define1_startY_)+index,0));
            ss.setM_endX(defineShared.getInt(getContext().getString(R.string.pref_define1_endX_)+index,0));
            ss.setM_endY(defineShared.getInt(getContext().getString(R.string.pref_define1_endY_)+index,0));
            ss.setM_signal(defineShared.getInt(getContext().getString(R.string.pref_define1_signal_)+index,0));
            m_define1_sceneList.add(ss);
        }
        return m_define1_sceneList;
    }
    public ArrayList<SingleSceneCell> getDefine2Scene(){
        SharedPreferences defineShared = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        mDefine2Flag=defineShared.getInt(getContext().getString(R.string.pref_define2_flag),0);
        mDefine2Num=defineShared.getInt(getContext().getString(R.string.pref_define2_num),0);
        Log.i(TAG,"mDefine2Num="+mDefine2Num);
        m_define2_sceneList.clear();
        for (int index=0;index<mDefine2Num;index++){
            SingleSceneCell ss =new SingleSceneCell();
            ss.setM_startX(defineShared.getInt(getContext().getString(R.string.pref_define2_startX_)+index,0));
            ss.setM_startY(defineShared.getInt(getContext().getString(R.string.pref_define2_startY_)+index,0));
            ss.setM_endX(defineShared.getInt(getContext().getString(R.string.pref_define2_endX_)+index,0));
            ss.setM_endY(defineShared.getInt(getContext().getString(R.string.pref_define2_endY_)+index,0));
            ss.setM_signal(defineShared.getInt(getContext().getString(R.string.pref_define2_signal_)+index,0));
            m_define2_sceneList.add(ss);
        }
        return m_define2_sceneList;
    }
    public void initDefine1Scene()
    {
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getContext().getString(R.string.pref_define1_flag),0);
        editor.putInt(getContext().getString(R.string.pref_define1_num),0);
        for (int index=0;index<MainActivity.MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(getContext().getString(R.string.pref_define1_startX_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define1_startY_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define1_endX_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define1_endY_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define1_signal_)+index,0);
        }
        editor.commit();
    }
    public void initDefine2Scene()
    {
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getContext().getString(R.string.pref_define2_flag),0);
        editor.putInt(getContext().getString(R.string.pref_define2_num),0);
        for (int index=0;index<MainActivity.MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(getContext().getString(R.string.pref_define2_startX_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define2_startY_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define2_endX_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define2_endY_)+index,0);
            editor.putInt(getContext().getString(R.string.pref_define2_signal_)+index,0);
        }
        editor.commit();
    }
    public void confirmDefinedScene(int LastSceneIndex){
        SharedPreferences sharedPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (LastSceneIndex==4){
            editor.putInt(getContext().getString(R.string.pref_define1_flag), 1);
        }else if (LastSceneIndex==5) {
            editor.putInt(getContext().getString(R.string.pref_define2_flag), 1);
        }
        editor.commit();
        drawDefinedScene(LastSceneIndex);
    }
    public void drawDefinedScene(int sceneIndex){
        initCell();
        if (sceneIndex==4){
            for (SingleSceneCell cell:getDefine1Scene()){
                CellPaint.setColor(Color.BLUE);
                CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),
                        cell.getM_endX(), cell.getM_endY(), CellPaint);
            }
        }else if (sceneIndex==5){
            for (SingleSceneCell cell:getDefine2Scene()){
                CellPaint.setColor(Color.BLUE);
                CellCanvas.drawRect(cell.getM_startX(), cell.getM_startY(),
                        cell.getM_endX(), cell.getM_endY(), CellPaint);
            }
        }else{}
        invalidate();
    }
    public void initVCL3Comm(){
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting), Context.MODE_PRIVATE);
        mVclordIp=preferences.getString(getContext().getString(R.string.pref_data_vclordip),"");
        /*if (mVcl3CommProces == null) {
            mVcl3CommProces = new VCL3CommProcess(mVclordIp, VclordActivity.PORT);
        }*/
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
    public void closeWindow(byte winId, byte type, boolean bIsAll){
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting),Context.MODE_PRIVATE);
        mSignalWindowCount =preferences.getInt(getContext().getString(R.string.pref_signal_window_num),0);
        initVCL3Comm();
        //VCLComm vclComm = new VCLComm(mVclordIp,VclordActivity.PORT);
        if (bIsAll) {
            for (byte i = 1; i <= mSignalWindowCount; i++) {
                VclordActivity.vcl3CommProcess.CloseSignalWindow(i, type);
                //vclComm.execute((byte)2,winId,type,(byte)1,(byte)mSignalWindowCount);
            }
            winId=1;
            ArrayList<SingleSceneCell> sceneCells = getSceneCell();
            for (SingleSceneCell scene_cell :sceneCells) {
                CellPaint.setColor(Color.BLUE);
                CellCanvas.drawRect(scene_cell.getM_startX(), scene_cell.getM_startY(), scene_cell.getM_endX(), scene_cell.getM_endY(), CellPaint);
                invalidate();
            }
        }else {
            VclordActivity.vcl3CommProcess.CloseSignalWindow(winId,type);
            //vclComm.execute((byte)2,winId,type,(byte)2);
        }

        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(getContext().getString(R.string.pref_signal_window_num),winId-1);
        editor.commit();

    }
    public boolean openWindow(byte winId,byte inputId,byte sig,short posX,short posY,short widthX,short widthY){
        initVCL3Comm();
        byte high_startX=(byte)(posX>>8);
        byte low_startX= (byte)(posX & 0x00FF);
        byte high_startY=(byte)(posY>>8);
        byte low_startY=(byte)(posY & 0x00FF);
        byte width_high_X=(byte)(widthX>>8);
        byte width_low_X=(byte)(widthX & 0x00FF);
        byte width_high_Y=(byte)(widthY>>8);
        byte width_low_Y=(byte)(widthY & 0x00FF);
        boolean ret =VclordActivity.vcl3CommProcess.OpenSignalWindow( winId, inputId, sig, high_startX, low_startX, high_startY, low_startY,width_high_X, width_low_X, width_high_Y, width_low_Y);
        /*VCLComm vclComm= new VCLComm(mVclordIp,VclordActivity.PORT);
        Boolean ret = null;
        try {
            ret = vclComm.execute((byte)1, winId, inputId, sig, high_startX, low_startX, high_startY, low_startY,width_high_X, width_low_X, width_high_Y, width_low_Y).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(getContext().getString(R.string.pref_signal_window_num),winId);
        editor.commit();
        return ret;
    }

    private class VCLComm extends AsyncTask<Byte,Void,Boolean> {

        public String mIp;
        public int mPort;
        VCL3CommProcess mVcl3CommProces=null;
        public VCLComm(String ip,int port){
            mIp=ip;
            mPort=port;
            mVcl3CommProces = new VCL3CommProcess(mIp, mPort);
        }


        @Override
        protected Boolean doInBackground(Byte... FuncName) {
            boolean ret=false;
            if (FuncName[0]==1){
                ret = mVcl3CommProces.OpenSignalWindow( FuncName[1], FuncName[2], FuncName[3], FuncName[4], FuncName[5], FuncName[6], FuncName[7],FuncName[8], FuncName[9], FuncName[10], FuncName[11]);
            }
            else if (FuncName[0]==2){
                if (FuncName[3]==1 &&FuncName[4]>=0){
                    for (int i =0;i<FuncName[4];i++){
                        ret |=mVcl3CommProces.CloseSignalWindow(FuncName[1], FuncName[2]);
                    }
                }else {
                    ret = mVcl3CommProces.CloseSignalWindow(FuncName[1], FuncName[2]);
                }
            }
            //end
            try {
                mVcl3CommProces.ProcessCancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mVcl3CommProces=null;
            return ret;
        }


    }
}
