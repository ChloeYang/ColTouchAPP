package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Vector;

import commprocess.VCL3CommProcess;
import engine.CpComm;

import static java.lang.Thread.sleep;


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
    private CpComm.stuDlpQInterfaceStatus stuDlpQInterfaceStatusInfo;
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
        stuDlpQInterfaceStatusInfo=new CpComm.stuDlpQInterfaceStatus();
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
        }else if (FuncName[0]==4){
            //// TODO: 2017/12/19 get interface status
            try {
                bRet = mVcl3CommProcess.GetInterfaceStatus(stuDlpQInterfaceStatusInfo,vecResponse) ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (FuncName[0]==5){
            //// TODO: 2017/12/21 get engine status
            short usDeviceID = (short)(FuncName[1]*VideoCell.CUBE_ROW_MAX + FuncName[2]);
            bRet = mVcl3CommProcess.GetEngineStatusInfo(usDeviceID,vecResponse) ;
        }else if (FuncName[0]==11){
            //// TODO: 2017/12/19 close all signal windows
            bRet = mVcl3CommProcess.CloseSignalWindow((byte)0,(byte)0,(byte)0) ;

        }else if (FuncName[0]==12){
            //// TODO: 2017/12/19 open signal window ,then close first
            bRet = mVcl3CommProcess.CloseSignalWindow(FuncName[1],(byte)0,(byte)0) ;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bRet |= mVcl3CommProcess.OpenSignalWindow(FuncName[1],FuncName[2],FuncName[3],
                    FuncName[4],FuncName[5],FuncName[6],FuncName[7],
                    FuncName[8],FuncName[9],FuncName[10],FuncName[11],(byte)0);
        }else if (FuncName[0]>=13 && FuncName[0]<=14){
            byte type = (byte)(FuncName[0] - 4);
            short cubeId;
            for (int i = 0;i<m_cellRow;i++){//setSysRowCol
                for (int j=0;j<m_cellCol;j++){
                    cubeId=(short)(i*VideoCell.CUBE_ROW_MAX+j);
                    Log.i(TAG,"color mode switch cubeId= "+cubeId);
                    try {
                        bRet = mVcl3CommProcess.ColorModeSwitch(cubeId,type) ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

