package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import commprocess.VCL3CommProcess;
import engine.CpComm;

import static java.lang.Thread.sleep;

/**
 * Created by yangjiyu on 2017/12/26.
 */

public class ModelAsyncTask extends AsyncTask<Byte,Void,Vector<Byte>>  {
    private final static String TAG = SceneWall.class.getSimpleName();
    private String mIp;
    private int mPort;
    private MyProgressDialog mProgressDialog;
    private SharedAppData sharedAppData=null;
    private Vector vecResponse;
    Context mContext;
    private int m_cellWidth=0;
    private int m_cellHeight=0;

    VCL3CommProcess mVcl3CommProcess =null;
    public ModelAsyncTask(String ip, int port, int cellWidth, int cellHeight, MyProgressDialog myProgressDialog, Context context){
        mIp=ip;
        mPort=port;
        m_cellWidth=cellWidth;
        m_cellHeight=cellHeight;
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
        sharedAppData=SharedAppData.newInstance(mContext);
        mVcl3CommProcess.CloseSignalWindow((byte)0,(byte)0,(byte)0) ;
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte winId=0,inputId=0,sigNum=0,High_startX=0,Low_startX=0,High_startY=0,Low_startY=0,Width_high_X=0,Width_low_X=0,Width_high_Y=0,Width_low_Y=0;
        short posX=0,posY=0,widthX=0,heightY=0;
        int i = 0;
        int signal=0;
        if (FuncName[0]==1){
            //// TODO: 2017/12/26 send cmd
            ArrayList<Integer> cubePix =sharedAppData.getCubePix();
            ArrayList<SingleSceneCell> sceneCells = sharedAppData.getSceneCell(FuncName[1]);
            byte flag=0;
            for (SingleSceneCell scene_cell :sceneCells) {
                i++;
                signal = sharedAppData.getModelSignal(FuncName[1],i);
                Log.d("ModelAsyncTask ","= "+scene_cell.getM_signal());
                //// TODO: 2017/12/4 save signal to sharedpreferences && send cmd to engine
                //byte winId=(byte)((mSignalWindowCount+1)&0xff);
                winId=(byte) (FuncName[1] * VideoWallFragment.WIN_INTER + i);
                inputId=(byte) (signal / VideoWallFragment.INPUT_BOARD_NUM);
                sigNum=(byte) (signal % VideoWallFragment.INPUT_BOARD_NUM);

                posX = (short) ((scene_cell.getM_startX() / m_cellWidth) * cubePix.get(0));
                posY = (short) ((scene_cell.getM_startY() / m_cellHeight) * cubePix.get(1));
                widthX = (short) (((scene_cell.getM_endX() - scene_cell.getM_startX()) / m_cellWidth) * cubePix.get(0));
                heightY = (short) (((scene_cell.getM_endY() - scene_cell.getM_startY()) / m_cellHeight) * cubePix.get(1));

                High_startX=(byte) (posX >> 8);
                Low_startX=(byte) (posX & 0x00FF);
                High_startY=(byte) (posY >> 8);
                Low_startY=(byte) (posY & 0x00FF);
                Width_high_X=(byte) (widthX >> 8);
                Width_low_X=(byte) (widthX & 0x00FF);
                Width_high_Y=(byte) (heightY >> 8);
                Width_low_Y=(byte) (heightY & 0x00FF);
                if (i==1 && sceneCells.size()>1){ flag=1;}
                if (i==sceneCells.size() && sceneCells.size()>1){ flag=2;}
                mVcl3CommProcess.OpenSignalWindow(winId,inputId,sigNum,
                        High_startX,Low_startX,High_startY,Low_startY,
                        Width_high_X,Width_low_X,Width_high_Y,Width_low_Y,flag);
            }
        }
        try {
            mVcl3CommProcess.ProcessCancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mVcl3CommProcess =null;
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
