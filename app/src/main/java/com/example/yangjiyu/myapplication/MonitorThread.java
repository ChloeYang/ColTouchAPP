package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.util.Log;

import commprocess.VCL3CommProcess;
import engine.CpComm;

/**
 * Created by yangjiyu on 2017/12/26.
 */

public class MonitorThread extends Thread{
    private String mIp="";
    private int mPort=5800;
    private SharedAppData sharedAppData;
    private static MonitorThread m_monitorThread=null;
    private static boolean bIsStarted=false;
    private MonitorThread (String ip, int port, Context context){
        mIp=ip;
        mPort=port;
        sharedAppData = SharedAppData.newInstance(context);
    }
    public static MonitorThread newInstance(String ip, int port, Context context){
        if (m_monitorThread==null){
            m_monitorThread = new MonitorThread(ip,port,context);
            bIsStarted = false;
            return m_monitorThread;
        }else {
            bIsStarted = true;
            return m_monitorThread;
        }
    }
    public static boolean getIsStarted(){
        return bIsStarted;
    }
    public void saveSignalInfo(CpComm.CpSignalInfo signalInfo) {

        sharedAppData.setSignalFlag(0, signalInfo.ucInput1);
        sharedAppData.setSignalFlag(1, signalInfo.ucInput2);
        sharedAppData.setSignalFlag(2, signalInfo.ucInput3);
        sharedAppData.setSignalFlag(3, signalInfo.ucInput4);
        int pixX, pixY;
        switch (signalInfo.ucPix) {
            case 0:
                pixX = 1920;
                pixY = 1080;
                break;
            case 1:
                pixX = 1400;
                pixY = 1050;
                break;
            case 2:
                pixX = 1024;
                pixY = 768;
                break;
            default:
                pixX = 1024;
                pixY = 768;
                break;
        }
        sharedAppData.setCubePix( pixX, pixY);
    }

    @Override
    public void run() {

        VCL3CommProcess vcl3CommProcess = new VCL3CommProcess(mIp, mPort);
        CpComm.CpSignalInfo signalInfo = new CpComm.CpSignalInfo();
        while (true){
            vcl3CommProcess.GetSignalInfo(signalInfo);
            saveSignalInfo(signalInfo);
            Log.d("MonitorThread","run......");

            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
