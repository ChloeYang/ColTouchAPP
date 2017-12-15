package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import commprocess.VCL3CommProcess;


/**
 * Created by yangjiyu on 2017/12/7.
 *//*


*/
public class VCLComm extends AsyncTask<Byte,Void,Boolean> {
    private final static String TAG = SceneWall.class.getSimpleName();
    private String mIp;
    private int mPort;
    private int m_cellRow;
    private int m_cellCol;
    private MyProgressDialog mProgressDialog;
    Context mContext;

    VCL3CommProcess mVcl3CommProcess =null;
    public VCLComm(String ip, int port, int row, int col, MyProgressDialog myProgressDialog, Context context){
        mIp=ip;
        mPort=port;
        m_cellRow=row;
        m_cellCol=col;
        mProgressDialog=myProgressDialog;
        mContext=context;
    }


    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Byte... FuncName) {

        mVcl3CommProcess = new VCL3CommProcess(mIp, mPort);
        boolean bRet=false;
        if (FuncName[0]==1){
            Log.i(TAG,"power_on ");
            int type =0;
            int broadcast=0;
            short cubeId=0;
            for (int i = 0;i<m_cellRow;i++){//setSysRowCol
                for (int j=0;j<m_cellCol;j++){
                    cubeId=(short)(i*VideoCell.CUBE_ROW_MAX+j);
                    Log.i(TAG,"power_on cubeId= "+cubeId);
                    bRet = mVcl3CommProcess.EngineOnOff(cubeId,type,broadcast) ;

                }
            }
        }
        else if (FuncName[0]==2){
            Log.i(TAG,"power_off ");
            int type =1;
            int broadcast=0;
            short cubeId=0;
            for (int i = 0;i<m_cellRow;i++){//setSysRowCol
                for (int j=0;j<m_cellCol;j++){
                    cubeId=(short)(i*VideoCell.CUBE_ROW_MAX+j);
                    Log.i(TAG,"power_off cubeId= "+cubeId);
                    bRet = mVcl3CommProcess.EngineOnOff(cubeId,type,broadcast) ;
                }
            }
        }
        //end
        try {
            mVcl3CommProcess.ProcessCancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mVcl3CommProcess =null;

        return bRet;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        String messageInfo = mContext.getString(R.string.progress_check_system);
        mProgressDialog.setMessage(messageInfo);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mProgressDialog.dismiss();
    }
}

