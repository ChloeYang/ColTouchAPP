package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjiyu on 2017/12/11.
 */

public class SharedAppData {
    private static Context mContext=null ;
    private static SharedAppData sharedAppData=null;

    SharedAppData(Context context){
        this.mContext=context;
    }

    public static SharedAppData newInstance(Context context){
        if (sharedAppData==null){
            if (context==null)
                return null;
            sharedAppData=new SharedAppData(context.getApplicationContext());
            return sharedAppData;
        }
        else {
            return sharedAppData;
        }
    }



    public static int getSystemInfo(int type)
    {
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting), Context.MODE_PRIVATE);
        int value=0;
        switch (type)
        {
            case 1:
                value=preferences.getInt(mContext.getString(R.string.pref_data_row),1);//m_cellRow
                 break;
            case 2:
                value=preferences.getInt(mContext.getString(R.string.pref_data_col),1);//m_cellCol
                break;
            case 3:
                value=preferences.getInt(mContext.getString(R.string.pref_width),1);//m_cellWidth
                break;
            case 4:
                value=preferences.getInt(mContext.getString(R.string.pref_height),1);//m_cellHeight
                break;
            default:
                break;
        }
        return value;
    }
    public static void saveSystemInfo(String VclordIp,int sysID, int uiRow, int uiCol){

        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mContext.getString(R.string.pref_data_vclordip), VclordIp);
        editor.putInt(mContext.getString(R.string.pref_data_sid), sysID);
        editor.putInt(mContext.getString(R.string.pref_data_row), uiRow);
        editor.putInt(mContext.getString(R.string.pref_data_col), uiCol);
        editor.commit();
    }

    public static void saveVideoCellList(ArrayList<VideoCell> videoCells) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_video_cell_list), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int iCellNum = videoCells.size();
        int cellWidth=0,  cellHeight=0;
        editor.putInt(mContext.getString(R.string.pref_cubeNum), iCellNum);
        for (int index = 0; index < iCellNum; index++) {
            editor.putInt(mContext.getString(R.string.pref_cubeId)+index, videoCells.get(index).getCellId());
            editor.putInt(mContext.getString(R.string.pref_posX)+index, videoCells.get(index).getCellPositionTopLeftX());
            editor.putInt(mContext.getString(R.string.pref_posY)+index, videoCells.get(index).getCellPositionTopLeftY());
            editor.putInt(mContext.getString(R.string.pref_width)+index, videoCells.get(index).getCellWidth());
            editor.putInt(mContext.getString(R.string.pref_height)+index, videoCells.get(index).getCellHeight());
            cellWidth=videoCells.get(index).getCellWidth();
            cellHeight=videoCells.get(index).getCellHeight();
        }
        editor.commit();

        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting),Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(mContext.getString(R.string.pref_width),cellWidth);
        ed.putInt(mContext.getString(R.string.pref_height),cellHeight);
        ed.commit();
    }
    public static ArrayList<VideoCell> getVideoCellList(){
        ArrayList<VideoCell> videoCells =new ArrayList<>();
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_video_cell_list), Context.MODE_PRIVATE);
        int iCellNum = sharedPref.getInt(mContext.getString(R.string.pref_cubeNum),0);
        int cellRow=getSystemInfo(1);
        int cellCol=getSystemInfo(2);
        int cellWidth=getSystemInfo(3);
        int cellHeight=getSystemInfo(4);
        for (int index = 0; index < iCellNum; index++) {
            VideoCell videoCell = new VideoCell(cellWidth,cellHeight);
            videoCell.setCellId(cellRow,cellCol);
            videoCell.setVideoCellPosition(sharedPref.getInt(mContext.getString(R.string.pref_posX)+index, 0),sharedPref.getInt(mContext.getString(R.string.pref_posY)+index, 0));
            videoCell.setCellWidth(sharedPref.getInt(mContext.getString(R.string.pref_width)+index,0));
            videoCell.setCellHeight(sharedPref.getInt(mContext.getString(R.string.pref_height)+index,0));
            videoCells.add(videoCell);
        }
        return videoCells;
    }

    public static void saveDefaultScene(String str,int num, int start_x,int start_y,int end_x,int end_y){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_default_num)+str,num+1);
        editor.putInt(mContext.getString(R.string.pref_default_startX_)+str+"_"+num,start_x);
        editor.putInt(mContext.getString(R.string.pref_default_startY_)+str+"_"+num,start_y);
        editor.putInt(mContext.getString(R.string.pref_default_endX_)+str+"_"+num,end_x);
        editor.putInt(mContext.getString(R.string.pref_default_endY_)+str+"_"+num,end_y);
        editor.commit();
    }
    public static ArrayList<SingleSceneCell> getDefaultScene(String str){
        ArrayList<SingleSceneCell> cells = new ArrayList<>();
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        int num=defineShared.getInt(mContext.getString(R.string.pref_default_num)+str,0);
        for (int index=0;index<num;index++){
            SingleSceneCell ss =new SingleSceneCell();
            ss.setM_startX(defineShared.getInt(mContext.getString(R.string.pref_default_startX_)+str+"_"+index,0));
            ss.setM_startY(defineShared.getInt(mContext.getString(R.string.pref_default_startY_)+str+"_"+index,0));
            ss.setM_endX(defineShared.getInt(mContext.getString(R.string.pref_default_endX_)+str+"_"+index,0));
            ss.setM_endY(defineShared.getInt(mContext.getString(R.string.pref_default_endY_)+str+"_"+index,0));
            ss.setM_signal(defineShared.getInt(mContext.getString(R.string.pref_default_signal_)+str+"_"+index,0));
            cells.add(ss);
        }
        return cells;
    }
    public static void saveDefine1Scene(int flag,int numd, int start_x,int start_y,int end_x,int end_y){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_define1_flag),flag);
        editor.putInt(mContext.getString(R.string.pref_define1_num),numd);
        int num = numd-1;
        editor.putInt(mContext.getString(R.string.pref_define1_startX_)+num,start_x);
        editor.putInt(mContext.getString(R.string.pref_define1_startY_)+num,start_y);
        editor.putInt(mContext.getString(R.string.pref_define1_endX_)+num,end_x);
        editor.putInt(mContext.getString(R.string.pref_define1_endY_)+num,end_y);
        editor.commit();
    }
    public static void saveDefine2Scene(int flag,int numd, int start_x,int start_y,int end_x,int end_y){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_define2_flag),flag);
        editor.putInt(mContext.getString(R.string.pref_define2_num),numd);
        int num = numd-1;
        editor.putInt(mContext.getString(R.string.pref_define2_startX_)+num,start_x);
        editor.putInt(mContext.getString(R.string.pref_define2_startY_)+num,start_y);
        editor.putInt(mContext.getString(R.string.pref_define2_endX_)+num,end_x);
        editor.putInt(mContext.getString(R.string.pref_define2_endY_)+num,end_y);
        editor.commit();
    }
    public static ArrayList<SingleSceneCell> getDefine1Scene(){
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        int mDefine1Flag=defineShared.getInt(mContext.getString(R.string.pref_define1_flag),0);
        int mDefine1Num=defineShared.getInt(mContext.getString(R.string.pref_define1_num),0);
        ArrayList<SingleSceneCell> m_define1_sceneList=new ArrayList<>();
        m_define1_sceneList.clear();
        for (int index=0;index<mDefine1Num;index++){
            SingleSceneCell ss =new SingleSceneCell();
            ss.setM_startX(defineShared.getInt(mContext.getString(R.string.pref_define1_startX_)+index,0));
            ss.setM_startY(defineShared.getInt(mContext.getString(R.string.pref_define1_startY_)+index,0));
            ss.setM_endX(defineShared.getInt(mContext.getString(R.string.pref_define1_endX_)+index,0));
            ss.setM_endY(defineShared.getInt(mContext.getString(R.string.pref_define1_endY_)+index,0));
            ss.setM_signal(defineShared.getInt(mContext.getString(R.string.pref_define1_signal_)+index,0));
            m_define1_sceneList.add(ss);
        }
        return m_define1_sceneList;
    }
    public static  int getDefine1SceneNum(){
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        int Define1Num=defineShared.getInt(mContext.getString(R.string.pref_define1_num),0);
        return Define1Num;
    }
    public static int getDefine1SceneFlag() {
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        int mDefine1Flag = defineShared.getInt(mContext.getString(R.string.pref_define1_flag), 0);
        return mDefine1Flag;
    }
    public static ArrayList<SingleSceneCell> getDefine2Scene(){
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        int mDefine2Flag=defineShared.getInt(mContext.getString(R.string.pref_define2_flag),0);
        int mDefine2Num=defineShared.getInt(mContext.getString(R.string.pref_define2_num),0);
        ArrayList<SingleSceneCell> m_define2_sceneList=new ArrayList<>();
        m_define2_sceneList.clear();
        for (int index=0;index<mDefine2Num;index++){
            SingleSceneCell ss =new SingleSceneCell();
            ss.setM_startX(defineShared.getInt(mContext.getString(R.string.pref_define2_startX_)+index,0));
            ss.setM_startY(defineShared.getInt(mContext.getString(R.string.pref_define2_startY_)+index,0));
            ss.setM_endX(defineShared.getInt(mContext.getString(R.string.pref_define2_endX_)+index,0));
            ss.setM_endY(defineShared.getInt(mContext.getString(R.string.pref_define2_endY_)+index,0));
            ss.setM_signal(defineShared.getInt(mContext.getString(R.string.pref_define2_signal_)+index,0));
            m_define2_sceneList.add(ss);
        }
        return m_define2_sceneList;
    }
    public static  int getDefine2SceneNum(){
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene),Context.MODE_PRIVATE);
        int Define2Num=defineShared.getInt(mContext.getString(R.string.pref_define2_num),0);
        return Define2Num;
    }
    public static int getDefine2SceneFlag() {
        SharedPreferences defineShared = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        int mDefine2Flag = defineShared.getInt(mContext.getString(R.string.pref_define2_flag), 0);
        return mDefine2Flag;
    }
    public static void initDefine1Scene()
    {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_define1_flag),0);
        editor.putInt(mContext.getString(R.string.pref_define1_num),0);
        for (int index=0;index<MainActivity.MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(mContext.getString(R.string.pref_define1_startX_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define1_startY_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define1_endX_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define1_endY_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define1_signal_)+index,0);
        }
        editor.commit();
    }
    public static void initDefine2Scene()
    {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_define2_flag),0);
        editor.putInt(mContext.getString(R.string.pref_define2_num),0);
        for (int index=0;index<MainActivity.MAX_SUPPORT_CELL;index++)
        {
            editor.putInt(mContext.getString(R.string.pref_define2_startX_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define2_startY_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define2_endX_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define2_endY_)+index,0);
            editor.putInt(mContext.getString(R.string.pref_define2_signal_)+index,0);
        }
        editor.commit();
    }
    public static void confirmDefinedScene(int LastSceneIndex){
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_define_scene), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (LastSceneIndex==4){
            editor.putInt(mContext.getString(R.string.pref_define1_flag), 1);
        }else if (LastSceneIndex==5) {
            editor.putInt(mContext.getString(R.string.pref_define2_flag), 1);
        }
        editor.commit();
        //drawDefinedScene(LastSceneIndex);
    }
    public static String getVCLordIP(){
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting), Context.MODE_PRIVATE);
        String vclordIp=preferences.getString(mContext.getString(R.string.pref_data_vclordip),"");
        return vclordIp;
    }

    public static ArrayList<SingleSceneCell> getSceneCell(int mLastSceneIndex){
        ArrayList<SingleSceneCell> sceneCells = new ArrayList<>();
        switch (mLastSceneIndex){
            case 0:
                sceneCells =getDefaultScene(mContext.getString(R.string.pref_whole_scene));
                break;
            case 1:
                sceneCells =getDefaultScene(mContext.getString(R.string.pref_h2part_scene));
                break;
            case 2:
                sceneCells=getDefaultScene(mContext.getString(R.string.pref_v2part_scene));
                break;
            case 3:
                sceneCells=getDefaultScene(mContext.getString(R.string.pref_each_scene));
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
    public static ArrayList<Integer> getCubePix(){
        ArrayList<Integer> pixXY= new ArrayList<>();
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting),Context.MODE_PRIVATE);
        pixXY.add(preferences.getInt(mContext.getString(R.string.pref_pix_x),1024));
        pixXY.add(preferences.getInt(mContext.getString(R.string.pref_pix_y),768));
        return pixXY;
    }
    public static void setCubePix(int pixX,int pixY){

        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(mContext.getString(R.string.pref_pix_x), pixX);
        editor.putInt(mContext.getString(R.string.pref_pix_y), pixY);
        editor.commit();
    }
    public static void setSignalFlag(int inputId ,byte sigInfo){
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int dvi1, dvi2, hdmi, dp;
        /*0 、1  bit 分别表示 DVI0，DVI1
        2:     HDMI 信号
        3：DP 信号*/
        dvi1 = sigInfo & 0x01;
        dvi2 = (sigInfo & 0x02)>>1;
        hdmi = (sigInfo & 0x04)>>2;
        dp = (sigInfo & 0x08)>>3;
        editor.putInt(mContext.getString(R.string.pref_signal_flag) + (inputId * VclordActivity.INPUT_BOARD_NUM + 0), dvi1);
        editor.putInt(mContext.getString(R.string.pref_signal_flag) + (inputId * VclordActivity.INPUT_BOARD_NUM + 1), dvi2);
        editor.putInt(mContext.getString(R.string.pref_signal_flag) + (inputId * VclordActivity.INPUT_BOARD_NUM + 2), hdmi);
        editor.putInt(mContext.getString(R.string.pref_signal_flag) + (inputId * VclordActivity.INPUT_BOARD_NUM + 3), dp);
        editor.commit();
    }
    public static int getSignalFlag(int num ){
        SharedPreferences preferences = mContext.getSharedPreferences(mContext.getString(R.string.pref_setting),Context.MODE_PRIVATE);
        int flag = preferences.getInt(mContext.getString(R.string.pref_signal_flag)+num,0);
        return flag;
    }

}
