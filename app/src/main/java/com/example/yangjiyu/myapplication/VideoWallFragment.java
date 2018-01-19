package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import commprocess.ExchangeStuct;
import engine.CpComm;


public class VideoWallFragment extends Fragment {

    private final static String TAG = SceneWall.class.getSimpleName();
    public static final int INPUT_BOARD_NUM=4;
    public static final int WIN_INTER=12;
    private RelativeLayout mRelativeLayout;
    private VideoWallView mVideoWallView;
    private View v;

    private int start_x=0;
    private int start_y=0;
    private int end_x=0;
    private int end_y=0;
    private int flag1=0;
    private int flag2=0;
    private short posX = 0;
    private short posY = 0;
    private short widthX =0;
    private short heightY =0;
    private ComStruc.Stu_Open_Window openwin;

    private int windowsWidth = 1585;//1585//2100
    private int windowsHeight = 610;//610//840

    private int mListIndex=-1;
    private int mSceneIndex=-1;
    private int mSignalIndex=-1;
    private int mLastIndex =-1;
    private boolean m_bIsCloseAllWindows=false;
    private SharedAppData sharedAppData=null;
    private Activity mActivity;
    private MyProgressDialog mProgressDialog;

    public static   String[] StringSignal={"1-DVI_1","1-DVI_2","1-HDMI","1-DP",
            "2-DVI_1","2-DVI_2","2-HDMI","2-DP",
            "3-DVI_1","3-DVI_2","3-HDMI","3-DP",
            "4-DVI_1","4-DVI_2","4-HDMI","4-DP",
            "清除"};
    public void upDataList(int pos)
    {
        mListIndex=pos;
        mVideoWallView. mListIndex=mListIndex;
    }
    public void upDataItem(int pos)
    {
        if (mListIndex==2 || mListIndex==-1){
            mSceneIndex=pos;
            mVideoWallView.mSceneIndex = mSceneIndex;
            mVideoWallView = new VideoWallView(getContext(), windowsWidth, windowsHeight, mListIndex, mSceneIndex, mSignalIndex);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.fragment_video_wall_view);
            mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
            ));
            mSignalIndex=-1;
        }else if (mListIndex == 3){
            mSignalIndex = pos;
            mVideoWallView. mSignalIndex = mSignalIndex;
            if (mSignalIndex==16){
                closeAllWindow();
                Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
            }
        }else if (mListIndex == 0){
            int powerOnOff = pos;
            if (powerOnOff == 0){
                ComCommand((byte)1/*"power_on"*/);
            }else if (powerOnOff == 1){
                ComCommand((byte)2/*"power_off"*/);
            }
            mSignalIndex=-1;
        }else if (mListIndex == 5){//version && status
            byte type=(byte)(3+pos);
            ComCommand(type);
            mSignalIndex=-1;
        }else if (mListIndex == 1){//scene and signal to model
            //// TODO: 2017/12/26 model scene+signal
            if (pos == 3){
                //// TODO: 2017/12/26 save model
                SetModelSave();
            }else {
                //// TODO: 2017/12/26 Exchange
                Log.d("ExchangeSceneAndSignal","pos:"+pos);
                ExchangeSceneAndSignal(pos);
            }
        }else if (mListIndex == 4){//color mode
            byte type=(byte)(13+pos);
            ComCommand(type);
        }

        if (mListIndex ==3 && mLastIndex == 2){
            m_bIsCloseAllWindows=true;
        }
        mLastIndex =mListIndex;

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = MyProgressDialog.createProgressDialog(getContext(), 5000, new MyProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(ProgressDialog dialog) {
                Toast.makeText(getContext(), "TimeOut", Toast.LENGTH_SHORT).show();
            }
        });
        mProgressDialog.setTitle(getString(R.string.app_name));
        String messageInfo = getString(R.string.progress_check_system);
        mProgressDialog.setMessage(messageInfo);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_video_wall, container, false);
        v = inflater.inflate(R.layout.fragment_video_wall,container,false);

        inflater.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = ((WindowManager)v.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        Log.i("Checkout","display: width = " + display.getWidth() + ", height = " + display.getHeight());
        Log.i("Checkout","inflater: width = " + v.getMeasuredWidth() + ", height = " + v.getHeight());

        openwin=new ComStruc.Stu_Open_Window();
        sharedAppData=SharedAppData.newInstance(mActivity);

        mVideoWallView = new VideoWallView(mActivity, windowsWidth, windowsHeight,mListIndex,mSceneIndex,mSignalIndex);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.fragment_video_wall_view);
        mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        mVideoWallView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        start_x = (int) Math.floor((int)event.getX()/mVideoWallView.m_cellWidth) * (mVideoWallView.m_cellWidth + VideoWall.sVideoCellGap);
                        start_y = (int) Math.floor((int)event.getY()/mVideoWallView.m_cellHeight) * (mVideoWallView.m_cellHeight+ VideoWall.sVideoCellGap);
                        Log.i("TouchEvent","ACTION_DOWN start_x="+start_x+" start_y="+start_y);

                        if (3!= mListIndex){
                            return true;
                        }
                        if (mSignalIndex>=0 && 0==sharedAppData.getSignalFlag(mSignalIndex) && mSignalIndex<StringSignal.length){
                            Toast.makeText(getContext(), R.string.operation_signal_no_exist, Toast.LENGTH_SHORT).show();
                            mSignalIndex=-1;
                            v.invalidate();
                            return true;
                        }
                        if (mSignalIndex>=0 && mSignalIndex<StringSignal.length /*&& mSignalIsChanged*/){
                            ArrayList<Integer> cubePix =sharedAppData.getCubePix();
                            ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(mVideoWallView.mLastSceneIndex);
                            //// TODO: 2017/12/13
                            //VCLCommThread vclCommThread= new VCLCommThread(sharedAppData.getVCLordIP(),VclordActivity.PORT);
                            //// TODO: 2017/12/25 close all windows
                            if (m_bIsCloseAllWindows){
                                byte type =11;
                                VCLComm vclcom=new VCLComm(sharedAppData.getVCLordIP(),VclordActivity.PORT,sharedAppData.getSystemInfo(1),sharedAppData.getSystemInfo(2),mProgressDialog,getContext());
                                vclcom.execute(type);
                                m_bIsCloseAllWindows=false;
                            }
                            int i=0;
                            for (SingleSceneCell scene_cell :sceneCells){
                                i++;
                                if (((int)event.getX() >= scene_cell.getM_startX() && (int)event.getX() <= scene_cell.getM_endX()) && ((int)event.getY() >= scene_cell.getM_startY() && (int)event.getY() <= scene_cell.getM_endY())){
                                    mVideoWallView.drawCanvasRect(scene_cell.getM_startX(), scene_cell.getM_startY(),scene_cell.getM_endX(), scene_cell.getM_endY());

                                    if (mSignalIndex!=StringSignal.length-1) {
                                        mVideoWallView.drawCanvasText(sharedAppData.getSignalName(mSignalIndex),scene_cell.getM_startX(),scene_cell.getM_startY(),scene_cell.getM_endX(),scene_cell.getM_endY());
                                        /*scene_cell.setM_signal(mSignalIndex);*/
                                        //Log.d("scene_cell ","= "+mSignalIndex);
                                        sharedAppData.saveSceneSignal(mVideoWallView.mLastSceneIndex,i,mSignalIndex);

                                        //// TODO: 2017/12/4 save signal to sharedpreferences && send cmd to engine
                                        //byte winId=(byte)((mSignalWindowCount+1)&0xff);
                                        openwin.setWinId((byte)(mVideoWallView.mLastSceneIndex*WIN_INTER+i));
                                        openwin.setInputId((byte) (mSignalIndex / INPUT_BOARD_NUM));
                                        openwin.setSig((byte) (mSignalIndex % INPUT_BOARD_NUM));

                                        posX = (short) ((scene_cell.getM_startX() / mVideoWallView.m_cellWidth) * cubePix.get(0));
                                        posY = (short) ((scene_cell.getM_startY() / mVideoWallView.m_cellHeight) * cubePix.get(1));
                                        widthX = (short) (((scene_cell.getM_endX() - scene_cell.getM_startX()) / mVideoWallView.m_cellWidth) * cubePix.get(0));
                                        heightY = (short) (((scene_cell.getM_endY() - scene_cell.getM_startY()) / mVideoWallView.m_cellHeight) * cubePix.get(1));

                                        openwin.setHigh_startX((byte)(posX>>8));
                                        openwin.setLow_startX((byte)(posX & 0x00FF));
                                        openwin.setHigh_startY((byte)(posY>>8));
                                        openwin.setLow_startY((byte)(posY & 0x00FF));
                                        openwin.setWidth_high_X((byte)(widthX>>8));
                                        openwin.setWidth_low_X((byte)(widthX & 0x00FF));
                                        openwin.setWidth_high_Y((byte)(heightY>>8));
                                        openwin.setWidth_low_Y((byte)(heightY & 0x00FF));

                                        //// TODO: 2017/12/13
                                        /*vclCommThread.start();
                                        vclCommThread.putMsgCmdInQue_OpenWindow("openWindow",openwin);
                                        bRet=vclCommThread.getbRet();
                                        */
                                        VCLComm vclcom=new VCLComm(sharedAppData.getVCLordIP(),VclordActivity.PORT,sharedAppData.getSystemInfo(1),sharedAppData.getSystemInfo(2),mProgressDialog,getContext());
                                        byte type =12;
                                        vclcom.execute(type,openwin.getWinId(),openwin.getInputId(),openwin.getSig(),
                                                openwin.getHigh_startX(),openwin.getLow_startX(),openwin.getHigh_startY(),openwin.getLow_startY(),
                                                openwin.getWidth_high_X(),openwin.getWidth_low_X(),openwin.getWidth_high_Y(),openwin.getWidth_low_Y());
                                        Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //v.invalidate();
                            }
                        }
                        //mSignalIndex=-1;//touch once
                        v.invalidate();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //Log.i("TouchEvent","mDefine1Num="+mDefine1Num+" mDefine2Num="+mDefine2Num);
                        end_x=(int) Math.floor((int)event.getX()/mVideoWallView.m_cellWidth) * (mVideoWallView.m_cellWidth + VideoWall.sVideoCellGap)+mVideoWallView.m_cellWidth;
                        end_y=(int) Math.floor((int)event.getY()/mVideoWallView.m_cellHeight) * (mVideoWallView.m_cellHeight+ VideoWall.sVideoCellGap)+mVideoWallView.m_cellHeight;
                        //Log.i("TouchEvent","ACTION_UP endx="+end_x+" endy="+end_y);
                        if(mSceneIndex==4 ) {
                            ArrayList<SingleSceneCell> cells=sharedAppData.getDefine1Scene();
                            if (sharedAppData.getDefine1SceneFlag()==1){ return false;}
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
                            int mDefine1Num = sharedAppData.getDefine1SceneNum();
                            sharedAppData.saveDefine1Scene(flag1, mDefine1Num+1, start_x, start_y, end_x, end_y);
                            mVideoWallView.drawTempRect(start_x, start_y,end_x,end_y);
                        }else if(mSceneIndex==5 ) {
                            ArrayList<SingleSceneCell> cells=sharedAppData.getDefine2Scene();
                            if (sharedAppData.getDefine2SceneFlag()==1){ return false;}
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
                            int mDefine2Num = sharedAppData.getDefine2SceneNum();
                            sharedAppData.saveDefine2Scene(flag2,mDefine2Num+1,start_x,start_y,end_x,end_y);
                            mVideoWallView.drawTempRect(start_x, start_y,end_x,end_y);
                        }else {
                            break;
                        }

                        v.invalidate();
                        return true;

                    default:
                        break;
                }

                v.invalidate();
                return true;
            }
        });
        return v;
    }
    public void closeAllWindow(){
        //// TODO: 2017/12/13
        /*VCLCommThread vclCommThread=new VCLCommThread(sharedAppData.getVCLordIP(),VclordActivity.PORT);
        vclCommThread.start();
        vclCommThread.putMsgCmdInQue_TwoByte("closeWindow",(byte)0,(byte)0);*/
        ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(mVideoWallView.mLastSceneIndex);
        for (SingleSceneCell scene_cell :sceneCells) {
            mVideoWallView.drawCanvasRect(scene_cell.getM_startX(), scene_cell.getM_startY(),scene_cell.getM_endX(), scene_cell.getM_endY());
        }
        v.invalidate();
        VCLComm vclcom=new VCLComm(sharedAppData.getVCLordIP(),VclordActivity.PORT,sharedAppData.getSystemInfo(1),sharedAppData.getSystemInfo(2),mProgressDialog,getContext());
        byte type =11;
        vclcom.execute(type);
        Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
    }
    public void ComCommand(byte type/*String str*/) {
        VCLComm vclcom=new VCLComm(sharedAppData.getVCLordIP(),VclordActivity.PORT,sharedAppData.getSystemInfo(1),sharedAppData.getSystemInfo(2),mProgressDialog,getContext());
        Vector<Byte> vecResponse = new Vector<>();
        if (type ==3){
            CpComm.stuDlpQInterfaceVersion stInfo = new CpComm.stuDlpQInterfaceVersion();
            try {
                vecResponse = vclcom.execute(type).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                stInfo = ExchangeStuct.ExchangeInterfaceVersion(vecResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DialogList dialogList = new DialogList(getContext());
            dialogList.InterfaceInfo(stInfo);
        }else if (type ==4){
            CpComm.stuDlpQInterfaceStatus stInfo = new CpComm.stuDlpQInterfaceStatus();
            try {
                vecResponse = vclcom.execute(type).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                stInfo =ExchangeStuct.ExchangeInterfaceStatus(vecResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DialogList dialogList = new DialogList(getContext());
            dialogList.InterfaceInfo(stInfo);
        }else if (type ==5){
            DialogList dialogList = new DialogList(getContext());
            dialogList.EngineInfo(vclcom,type,sharedAppData.getSystemInfo(1),sharedAppData.getSystemInfo(2));
        }else {
            vclcom.execute(type);
        }
        Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
    }

    private void SetModelSave(){
        DialogList dialogList = new DialogList(getContext());
        dialogList.SetModelSave(sharedAppData,mVideoWallView.mLastSceneIndex,1);
    }
    private void ExchangeSceneAndSignal(int pos){
        int flag =sharedAppData.getSaveModelInfo_Flag(pos);
        if (flag==0){
            Toast.makeText(getContext(), R.string.error_not_save_model_yet, Toast.LENGTH_SHORT).show();
        }else {
            int sceneIndex = sharedAppData.getSaveModelInfo_Scene(pos);
            ModelAsyncTask model=new ModelAsyncTask(sharedAppData.getVCLordIP(),VclordActivity.PORT,mVideoWallView.m_cellWidth,mVideoWallView.m_cellHeight,mProgressDialog,getContext());
            Log.d("ExchangeSceneAndSignal","sceneIndex:"+sceneIndex);
            model.execute((byte)1,(byte)sceneIndex,(byte)pos);

            mVideoWallView.drawCanvasBase();
            int i=0;
            ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(sceneIndex);
            for (SingleSceneCell scene_cell :sceneCells) {
                i++;
                mVideoWallView.drawCanvasRect(scene_cell.getM_startX(), scene_cell.getM_startY(),scene_cell.getM_endX(), scene_cell.getM_endY());
                //Log.d("ExchangeSceneAndSignal ","= "+sharedAppData.getModelSignal(sceneIndex,i));
                mVideoWallView.drawCanvasText(sharedAppData.getSignalName(sharedAppData.getModelSignal(sceneIndex,i,pos)),scene_cell.getM_startX(),scene_cell.getM_startY(),scene_cell.getM_endX(),scene_cell.getM_endY());
            }

            Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
        }
    }
}
