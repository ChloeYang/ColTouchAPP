package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.IOException;

import commprocess.VCL3CommProcess;

/**
 * Created by yangjiyu on 2017/12/7.
 */

/*public class VCLComm extends AsyncTask<Byte,Void,Boolean> {

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


}*/
