package com.example.yangjiyu.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import commprocess.VCL3CommProcess;

/**
 * Created by yangjiyu on 2017/12/12.
 */

public class VCLCommThread extends Thread {
    private final static String TAG = SceneWall.class.getSimpleName();
    private static VCLCommThread m_vclCommThread=null;
    private static String mIp;
    private static int mPort;
    private int m_cellRow=1;
    private int m_cellCol=1;
    private Handler _curHandler;    //rcv msg from other thread with this
    private static final String STR_CMD = "cmd";
    private static final String BYTE_ARG1 = "byte_arg1";
    private static final String BYTE_ARG2 = "byte_arg2";
    private static final String STU_OPEN_WIN = "stu_open_win";
    private ComStruc.Stu_Open_Window openWindow=null;
    private Message msg;

    public boolean getbRet() {
        int iCount=50;
        while (bRet==false && iCount-->0){
            try {
                sleep(100);
                //Log.d("getbRet","sleep "+ iCount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bRet;
    }
    private boolean bRet=false;
    private VCL3CommProcess mVcl3CommProcess;
    public   VCLCommThread (String ip,int port){
        mIp=ip;
        mPort=port;
    }
    public static  VCLCommThread newInstance(String ip,int port){
        if (m_vclCommThread==null){
            if (ip.isEmpty()){
                return null;
            }
            m_vclCommThread=new VCLCommThread(ip,port);
            return m_vclCommThread;
        }else {
            return m_vclCommThread;
        }
    }

    public void setSysRowCol(int row, int col)
    {
        this.m_cellRow=row;
        this.m_cellCol=col;
    }

    public int putMsgCmdInQue_TwoByte(String strCmd,byte arg1,byte arg2){
        if(null == _curHandler){
            _curHandler = new Handler();
            Log.d("Heiko","curHandler is returned null");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        msg=_curHandler.obtainMessage();
        if(msg == null){
            Log.d("Heiko","curHandler.obtainMessage() is returned null");
        }else {
            //Log.d("Heiko","curHandler.obtainMessage() return: " + msg.toString());
        }
        Bundle b= new Bundle();                     //form the msg frame.
        b.putString(STR_CMD,strCmd);
        b.putByte(BYTE_ARG1,arg1);
        b.putByte(BYTE_ARG2,arg2);
        msg.setData(b);
        _curHandler.sendMessage(msg);
        return strCmd.length();
    }

    public int putMsgCmdInQue_OpenWindow(String strCmd,ComStruc.Stu_Open_Window open_window){

        if(null == _curHandler) {
            _curHandler = new Handler();
            Log.d("Heiko","curHandler is returned null");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            msg=_curHandler.obtainMessage();
        if(msg == null){
            Log.d("Heiko","curHandler.obtainMessage() is returned null");
        }else {
            //Log.d("Heiko","curHandler.obtainMessage() return: " + msg.toString());
        }
        Bundle b= new Bundle();                     //form the msg frame.
        b.putString(STR_CMD,strCmd);
        b.putSerializable(STU_OPEN_WIN,open_window);
        msg.setData(b);
        _curHandler.sendMessage(msg);
        return strCmd.length();
    }
    public void  run(){
        Looper.prepare();

        _curHandler = new Handler() {
            public void handleMessage(Message msg) {
                while (msg==null){
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mVcl3CommProcess = new VCL3CommProcess(mIp, mPort);
                String string = msg.getData().getString(STR_CMD);
                if (string=="openWindow"){
                    openWindow=(ComStruc.Stu_Open_Window)msg.getData().getSerializable(STU_OPEN_WIN);
                    Log.i(TAG,"openWindow "+openWindow.getWinId()+","+openWindow.getInputId()+","+openWindow.getSig()+","+
                            openWindow.getHigh_startX()+","+openWindow.getLow_startX()+","+openWindow.getHigh_startY()+","+openWindow.getLow_startY()+","+
                            openWindow.getWidth_high_X()+","+openWindow.getWidth_low_X()+","+openWindow.getWidth_high_Y()+","+openWindow.getWidth_low_Y());

                    bRet = mVcl3CommProcess.CloseSignalWindow(openWindow.getWinId(),(byte)0,(byte)0) ;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bRet |= mVcl3CommProcess.OpenSignalWindow(openWindow.getWinId(),openWindow.getInputId(),openWindow.getSig(),
                            openWindow.getHigh_startX(),openWindow.getLow_startX(),openWindow.getHigh_startY(),openWindow.getLow_startY(),
                            openWindow.getWidth_high_X(),openWindow.getWidth_low_X(),openWindow.getWidth_high_Y(),openWindow.getWidth_low_Y(),(byte)0);
                    /*try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
                else if(string=="closeWindow"){
                    Log.i(TAG,"closeWindow "+msg.getData().getByte(BYTE_ARG1)+","+msg.getData().getByte(BYTE_ARG2));
                    bRet = mVcl3CommProcess.CloseSignalWindow(msg.getData().getByte(BYTE_ARG1),msg.getData().getByte(BYTE_ARG2),(byte)0) ;
                    //Log.i(TAG,"closeWindow ret ="+bRet);
                }
                else if(string=="power_on"){
                    Log.i(TAG,"power_on ");
                    int type =0;
                    int broadcast=0;
                    short cubeId=0;
                    for (int i = 0;i<m_cellRow;i++){//setSysRowCol
                        for (int j=0;j<m_cellCol;j++){
                            cubeId=(short)(i*VideoCell.CUBE_ROW_MAX+j);
                            Log.i(TAG,"power_on cubeId= "+cubeId);
                            bRet = mVcl3CommProcess.EngineOnOff(cubeId,type,broadcast) ;
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if (string=="power_off"){
                    Log.i(TAG,"power_off ");
                    int type =1;
                    int broadcast=0;
                    short cubeId=0;
                    for (int i = 0;i<m_cellRow;i++){//setSysRowCol
                        for (int j=0;j<m_cellCol;j++){
                            cubeId=(short)(i*VideoCell.CUBE_ROW_MAX+j);
                            Log.i(TAG,"power_off cubeId= "+cubeId);
                            bRet = mVcl3CommProcess.EngineOnOff(cubeId,type,broadcast) ;
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }
}
