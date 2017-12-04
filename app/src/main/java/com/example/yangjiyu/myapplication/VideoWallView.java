package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.nfc.Tag;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by Howie on 2017/7/25.
 */

public class VideoWallView extends View {
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

    public VideoWallView(Context context, int wallWidth, int wallHeight,int listIndex,int sceneIndex,int signalIndex) {

        super(context);

        SharedPreferences defineShared = getContext().getSharedPreferences(getContext().getString(R.string.pref_define_scene),Context.MODE_PRIVATE);//getResources()
        SharedPreferences setPref = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setPref.edit();
        mLastSceneIndex=setPref.getInt(getContext().getString(R.string.pref_LastSceneIndex),-1);
        mLastSignalIndex=setPref.getInt(getContext().getString(R.string.pref_LastSignalIndex),-1);

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

            if (mSignalIndex != mLastSignalIndex){
                mSignalIsChanged=true;
            }
            else {
                mSignalIsChanged=false;
            }
        }
        if (mListIndex==-1 && mSceneIndex==-1){
            initCell();
        }

        if (mSceneIsChanged==true){
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
            mLastSignalIndex=mSignalIndex;
            editor.putInt(getContext().getString(R.string.pref_LastSceneIndex), mLastSceneIndex);
            editor.putInt(getContext().getString(R.string.pref_LastSignalIndex), mLastSceneIndex);
            editor.commit();

        }
    }

    private void initCell(){

        getSystemInfo();
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


    private static int CellNumber = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int flag1=0;
        int flag2=0;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                start_x = (int) Math.floor((int)event.getX()/m_cellWidth) * (m_cellWidth + VideoWall.sVideoCellGap);
                start_y = (int) Math.floor((int)event.getY()/m_cellHeight) * (m_cellHeight+ VideoWall.sVideoCellGap);
                Log.i("TouchEvent","ACTION_DOWN start_x="+start_x+" start_y="+start_y);
                return true;
            case MotionEvent.ACTION_UP:
                Log.i("TouchEvent","mDefine1Num="+mDefine1Num+" mDefine2Num="+mDefine2Num);
                end_x=(int) Math.floor((int)event.getX()/m_cellWidth) * (m_cellWidth + VideoWall.sVideoCellGap)+m_cellWidth;
                end_y=(int) Math.floor((int)event.getY()/m_cellHeight) * (m_cellHeight+ VideoWall.sVideoCellGap)+m_cellHeight;
                Log.i("TouchEvent","ACTION_UP endx="+end_x+" endy="+end_y);
                if(mSceneIndex==4 ) {
                    for (SingleSceneCell cell : getDefine1Scene()) {
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
                    if (start_x>end_x){
                        int temp=start_x;
                        start_x=end_x;
                        end_x=temp;
                    }
                    if (start_y>end_y){
                        int temp=start_y;
                        start_y=end_y;
                        end_y=temp;
                    }
                    CellPaint.setColor(Color.RED);
                    CellCanvas.drawRect(start_x, start_y,end_x,end_y, CellPaint);
                }else if(mSceneIndex==5 ) {
                    for (SingleSceneCell cell:getDefine2Scene()){
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
                    if (start_x>end_x){
                        int temp=start_x;
                        start_x=end_x;
                        end_x=temp;
                    }
                    if (start_y>end_y){
                        int temp=start_y;
                        start_y=end_y;
                        end_y=temp;
                    }
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


        if(mListIndex ==1 && event.getAction() == MotionEvent.ACTION_DOWN){
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

    public void getSystemInfo()
    {
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.setting),Context.MODE_PRIVATE);
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

        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.setting),Context.MODE_PRIVATE);
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
            ss.setM_startY(defineShared.getInt(getContext().getString(R.string.pref_default_startY_)+str+index,0));
            ss.setM_endX(defineShared.getInt(getContext().getString(R.string.pref_default_endX_)+str+index,0));
            ss.setM_endY(defineShared.getInt(getContext().getString(R.string.pref_default_endY_)+str+index,0));
            ss.setM_signal(defineShared.getInt(getContext().getString(R.string.pref_default_signal_)+str+index,0));
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
}
