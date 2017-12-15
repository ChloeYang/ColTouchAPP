package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.ArrayList;


public class VideoWallFragment extends Fragment {

    private final static String TAG = SceneWall.class.getSimpleName();
    RelativeLayout mRelativeLayout;
    VideoWallView mVideoWallView;
    View v;

    int start_x=0;
    int start_y=0;
    int end_x=0;
    int end_y=0;

    int windowsWidth = 1585;//1585//2100
    int windowsHeight = 610;//610//840

    private int mListIndex=-1;
    private int mSceneIndex=-1;
    private int mSignalIndex=-1;
    private int mLastIndex =-1;
    private SharedAppData sharedAppData=null;
    private boolean bRet=false;
    private Activity mActivity;

    public  String[] StringSignal={"1-DVI_1","1-DVI_2","1-HDMI","1-DP",
            "2-DVI_1","2-DVI_2","2-HDMI","2-DP",
            "3-DVI_1","3-DVI_2","3-HDMI","3-DP",
            "4-DVI_1","4-DVI_2","4-HDMI","4-DP",
            "清除"};
    /*{getContext().getString(R.string.DVI_1_1),getContext().getString(R.string.DVI_1_2),getContext().getString(R.string.HDMI_1),getContext().getString(R.string.DP_1),
            getContext().getString(R.string.DVI_2_1),getContext().getString(R.string.DVI_2_2),getContext().getString(R.string.HDMI_2),getContext().getString(R.string.DP_2),
            getContext().getString(R.string.DVI_3_1),getContext().getString(R.string.DVI_3_2),getContext().getString(R.string.HDMI_3),getContext().getString(R.string.DP_3),
            getContext().getString(R.string.DVI_4_1),getContext().getString(R.string.DVI_4_2),getContext().getString(R.string.HDMI_4),getContext().getString(R.string.DP_4),
            getContext().getString(R.string.clear)};*/

    public void upDataList(int pos)
    {
        mListIndex=pos;
        mVideoWallView. mListIndex=mListIndex;
    }
    public void upDataItem(int pos)
    {
        if (mListIndex==0 || mListIndex==-1){
            /*if (mLastIndex ==1){
                mVideoWallView.closeWindow((byte)0,(byte)0,true);
            }*/
            mSceneIndex=pos;
            mVideoWallView.mSceneIndex = mSceneIndex;
            mVideoWallView = new VideoWallView(getContext(), windowsWidth, windowsHeight, mListIndex, mSceneIndex, mSignalIndex);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.fragment_video_wall_view);
            mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
            ));
        }else if (mListIndex == 1){
            mSignalIndex = pos;
            mVideoWallView. mSignalIndex = mSignalIndex;
            if (mSignalIndex==16){
                closeAllWindow();
                Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
            }
        }else if (mListIndex == 2){
            int powerOnOff = pos;
            if (powerOnOff == 0){
                powerOnOff("power_on");
            }else if (powerOnOff == 1){
                powerOnOff("power_off");
            }
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

        sharedAppData=SharedAppData.newInstance(mActivity);
        mVideoWallView = new VideoWallView(mActivity, windowsWidth, windowsHeight,mListIndex,mSceneIndex,mSignalIndex);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.fragment_video_wall_view);
        mRelativeLayout.addView(mVideoWallView, new ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        ));
        mVideoWallView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int flag1=0;
                int flag2=0;
                byte winId;
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        start_x = (int) Math.floor((int)event.getX()/mVideoWallView.m_cellWidth) * (mVideoWallView.m_cellWidth + VideoWall.sVideoCellGap);
                        start_y = (int) Math.floor((int)event.getY()/mVideoWallView.m_cellHeight) * (mVideoWallView.m_cellHeight+ VideoWall.sVideoCellGap);
                        Log.i("TouchEvent","ACTION_DOWN start_x="+start_x+" start_y="+start_y);

                        sharedAppData=SharedAppData.newInstance(getContext());
                        if (mSignalIndex>=0 && mSignalIndex<StringSignal.length /*&& mSignalIsChanged*/){
                            ArrayList<Integer> cubePix =sharedAppData.getCubePix();
                            ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(mVideoWallView.mLastSceneIndex);
                            //// TODO: 2017/12/13
                            VCLCommThread vclCommThread= new VCLCommThread(sharedAppData.getVCLordIP(),VclordActivity.PORT);
                            int i=0;
                            for (SingleSceneCell scene_cell :sceneCells){
                                i++;
                                if (((int)event.getX() >= scene_cell.getM_startX() && (int)event.getX() <= scene_cell.getM_endX()) && ((int)event.getY() >= scene_cell.getM_startY() && (int)event.getY() <= scene_cell.getM_endY())){
                                    mVideoWallView.drawCanvasRect(scene_cell.getM_startX(), scene_cell.getM_startY(),scene_cell.getM_endX(), scene_cell.getM_endY());

                                    if (mSignalIndex!=StringSignal.length-1) {
                                        mVideoWallView.drawCanvasText(StringSignal[mSignalIndex],scene_cell.getM_startX(),scene_cell.getM_startY(),scene_cell.getM_endX(),scene_cell.getM_endY());
                                        scene_cell.setM_signal(mSignalIndex);
                                        //// TODO: 2017/12/4 save signal to sharedpreferences && send cmd to engine
                                        //byte winId=(byte)((mSignalWindowCount+1)&0xff);
                                        ComStruc.Stu_Open_Window openwin=new ComStruc.Stu_Open_Window();
                                        openwin.setWinId((byte)(mVideoWallView.mLastSceneIndex*mVideoWallView.WIN_INTER+i));
                                        openwin.setInputId((byte) (mSignalIndex / mVideoWallView.INPUT_BOARD_NUM));
                                        openwin.setSig((byte) (mSignalIndex % mVideoWallView.INPUT_BOARD_NUM));

                                        short posX = (short) ((scene_cell.getM_startX() / mVideoWallView.m_cellWidth) * cubePix.get(0));
                                        short posY = (short) ((scene_cell.getM_startY() / mVideoWallView.m_cellHeight) * cubePix.get(1));
                                        short widthX = (short) (((scene_cell.getM_endX() - scene_cell.getM_startX()) / mVideoWallView.m_cellWidth) * cubePix.get(0));
                                        short heightY = (short) (((scene_cell.getM_endY() - scene_cell.getM_startY()) / mVideoWallView.m_cellHeight) * cubePix.get(1));

                                        openwin.setHigh_startX((byte)(posX>>8));
                                        openwin.setLow_startX((byte)(posX & 0x00FF));
                                        openwin.setHigh_startY((byte)(posY>>8));
                                        openwin.setLow_startY((byte)(posY & 0x00FF));
                                        openwin.setWidth_high_X((byte)(widthX>>8));
                                        openwin.setWidth_low_X((byte)(widthX & 0x00FF));
                                        openwin.setWidth_high_Y((byte)(heightY>>8));
                                        openwin.setWidth_low_Y((byte)(heightY & 0x00FF));

                                        //// TODO: 2017/12/13
                                        vclCommThread.start();
                                        vclCommThread.putMsgCmdInQue_OpenWindow("openWindow",openwin);
                                        bRet=vclCommThread.getbRet();
                                        //Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
                                        if (bRet) {
                                            Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), R.string.error_open_signal_failed, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                v.invalidate();
                            }
                        }
                        mSignalIndex=-1;
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
        VCLCommThread vclCommThread=new VCLCommThread(sharedAppData.getVCLordIP(),VclordActivity.PORT);
        vclCommThread.start();
        vclCommThread.putMsgCmdInQue_TwoByte("closeWindow",(byte)0,(byte)0);
        //Log.d(TAG,"mLastSceneIndex="+mVideoWallView.mLastSceneIndex);
        ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(mVideoWallView.mLastSceneIndex);
        for (SingleSceneCell scene_cell :sceneCells) {
            mVideoWallView.drawCanvasRect(scene_cell.getM_startX(), scene_cell.getM_startY(),scene_cell.getM_endX(), scene_cell.getM_endY());
        }
        v.invalidate();

        bRet=vclCommThread.getbRet();
        //Log.d(TAG,"getbRet finished");
        if (bRet) {
            Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.operation_failed, Toast.LENGTH_SHORT).show();
        }
    }
    public void powerOnOff(String str) {
        VCLCommThread vclCommThread=new VCLCommThread(sharedAppData.getVCLordIP(),VclordActivity.PORT);
        vclCommThread.setSysRowCol(sharedAppData.getSystemInfo(1),sharedAppData.getSystemInfo(2));
        //Log.d(TAG,"row="+sharedAppData.getSystemInfo(1)+",col="+sharedAppData.getSystemInfo(2));
        vclCommThread.start();
        vclCommThread.putMsgCmdInQue_TwoByte(str,(byte)0,(byte)0);

        //Log.d(TAG,"getbRet begin");
        bRet=vclCommThread.getbRet();
        //Log.d(TAG,"getbRet finished");
        if (bRet) {
            Toast.makeText(getContext(), R.string.operation_finished, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.operation_failed, Toast.LENGTH_SHORT).show();
        }
    }
    public void closeWindow(byte winId, byte type, boolean bIsAll){
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting),Context.MODE_PRIVATE);
        VclordActivity.vcl3CommProcess.CloseSignalWindow(winId,type);
        if (bIsAll) {
            winId=1;
            ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(mVideoWallView.mLastSceneIndex);
            for (SingleSceneCell scene_cell :sceneCells) {
                mVideoWallView.drawCanvasRect(scene_cell.getM_startX(), scene_cell.getM_startY(), scene_cell.getM_endX(), scene_cell.getM_endY());
                v.invalidate();
            }
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(getContext().getString(R.string.pref_signal_window_count),winId-1);
        editor.commit();

    }
    public boolean openWindow(byte winId,byte inputId,byte sig,short posX,short posY,short widthX,short widthY,byte inputWinNum){
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
        //if (ret) {
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getContext().getString(R.string.pref_signal_window_count), winId);
        editor.putInt(getContext().getString(R.string.pref_signal_output_board_window_num_) + inputId, inputWinNum);
        editor.commit();
        //}
        return ret;
    }
}
