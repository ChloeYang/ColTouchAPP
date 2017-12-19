package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Vector;

import commprocess.VCL3CommProcess;
import engine.CpComm;


/**
 * Created by yangjiyu on 2017/12/7.
 *//*


*/
public class VCLComm extends AsyncTask<Byte,Void,Vector<Byte>> {
    private final static String TAG = SceneWall.class.getSimpleName();
    private String mIp;
    private int mPort;
    private int m_cellRow;
    private int m_cellCol;
    private MyProgressDialog mProgressDialog;
    private CpComm.stuDlpQInterfaceVersion stuDlpQInterfaceVersionInfo;
    private Vector vecResponse;
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
    protected Vector<Byte> doInBackground(Byte... FuncName) {

        mVcl3CommProcess = new VCL3CommProcess(mIp, mPort);
        stuDlpQInterfaceVersionInfo=new CpComm.stuDlpQInterfaceVersion();
        vecResponse = new Vector();
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
        }else if (FuncName[0]==3){
            //// TODO: 2017/12/18 get interface version
            try {
                bRet = mVcl3CommProcess.GetInterfaceVersion(stuDlpQInterfaceVersionInfo,vecResponse) ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //end
        try {
            mVcl3CommProcess.ProcessCancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mVcl3CommProcess =null;

        /*if (bRet) {
            Toast.makeText(mContext, R.string.operation_finished, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, R.string.operation_failed, Toast.LENGTH_SHORT).show();
        }*/
        return vecResponse;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        String messageInfo = mContext.getString(R.string.progress_check_system);
        mProgressDialog.setMessage(messageInfo);
    }

    @Override
    protected void onPostExecute(Vector<Byte> bytes) {
        mProgressDialog.dismiss();
    }

}

