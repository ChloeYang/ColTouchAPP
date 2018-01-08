package com.example.yangjiyu.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import commprocess.ExchangeStuct;
import engine.CpComm;

import static java.lang.Thread.sleep;

/**
 * Created by yangjiyu on 2017/12/19.
 */

public class DialogList {

    private Context mContext;
    public DialogList(Context context) {
        mContext = context;
    }
    public void InterfaceInfo(CpComm.stuDlpQInterfaceVersion stInfo){
        final String items[] = {"版卡类型      MCU      FPGA      PCB      型号",
                "主控板     "+stInfo.stMain.strMcu+"   "+stInfo.stMain.strFpga+"    "+stInfo.stMain.strPcb+"   "+stInfo.stMain.strType,
                "采集卡1    "+stInfo.stSIn1.strMcu+"   "+stInfo.stSIn1.strFpga+"    "+stInfo.stSIn1.strPcb+"   "+stInfo.stSIn1.strType,
                "采集卡2    "+stInfo.stSIn2.strMcu+"   "+stInfo.stSIn2.strFpga+"    "+stInfo.stSIn2.strPcb+"   "+stInfo.stSIn2.strType,
                "采集卡3    "+stInfo.stSIn3.strMcu+"   "+stInfo.stSIn3.strFpga+"    "+stInfo.stSIn3.strPcb+"   "+stInfo.stSIn3.strType,
                "采集卡4    "+stInfo.stSIn4.strMcu+"   "+stInfo.stSIn4.strFpga+"    "+stInfo.stSIn4.strPcb+"   "+stInfo.stSIn4.strType,
                "输出卡1    "+stInfo.stSOut1.strMcu+"   "+stInfo.stSOut1.strFpga+"    "+stInfo.stSOut1.strPcb+"   "+stInfo.stSOut1.strType,
                "输出卡2    "+stInfo.stSOut2.strMcu+"   "+stInfo.stSOut2.strFpga+"    "+stInfo.stSOut2.strPcb+"   "+stInfo.stSOut2.strType,
                "输出卡3    "+stInfo.stSOut3.strMcu+"   "+stInfo.stSOut3.strFpga+"    "+stInfo.stSOut3.strPcb+"   "+stInfo.stSOut3.strType};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,1);
        builder.setTitle(mContext.getString(R.string.getSystemInfo));
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private String PowerStatus(byte type){
        String power ="";
        if (type==0){
            power = power + "正常";
        }else if (type ==1){
            power = power + "异常";
        }else {
            power = power + "离线";
        }
        return power;
    }
    public void InterfaceInfo(CpComm.stuDlpQInterfaceStatus stInfo){
        int icount=5;
        int index=0;
        String fan="";
        String humidity="";
        String power1=PowerStatus(stInfo.ucPower1);
        String power2=PowerStatus(stInfo.ucPower2);
        /*//test
        stInfo.ucHumidity=10;
        stInfo.usFan1=3000;
        //end test*/
        if (stInfo.ucHumidity>0){
            humidity=humidity+stInfo.ucHumidity;
        }else {icount--;}
        if (stInfo.usFan1>-1){
            fan = fan + stInfo.usFan1 + "转/分钟";
        }else { icount--;}

        String[] items =  new String[icount];
        items[index++]="温度: "+stInfo.ucTemperature+"℃";
        if (humidity==""){
            if (fan==""){}else {
                items[index++]="风扇:  "+fan;
            }
        }else {
            items[index++]="湿度:  "+humidity;
            if (fan==""){}else {
                items[index++]="风扇:  "+fan;
            }
        }
        items[index++]="电源1: "+power1;
        items[index++]="电源2: "+power2;
        /*final String items[] = {"温度: "+stInfo.ucTemperature+"℃",
                "湿度:  "+humidity,
                "风扇:  "+fan,
                "电源1: "+power1,
                "电源2: "+power2};*/

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,1);
        builder.setTitle(mContext.getString(R.string.getSystemInfo));
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private String EnginePowerStatus(byte type){
        String power ="";
        if (type==3){
            power = power + "正常";
        }else if (type ==1){
            power = power + "异常";
        }else {
            power = power + "不在位";
        }
        return power;
    }
    private String LedStatus(byte type){
        String string = "";
        if (type==1){
            string = string + "点亮";
        }else {
            string = string + "熄灭";
        }
        return string;
    }
    public void EngineInfo(final VCLComm vclcom, final byte type, final int row, final int col){
        int iSysCount = row*col;
        String [] strings = new String[iSysCount];
        int index=0;
        for (int i=0;i<row;i++){
            for (int j=0;j<col;j++){
                strings[index++]="单元 "+i+"-"+j;
            }
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,1);
        builder.setTitle(mContext.getString(R.string.getSystemInfo));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Vector<Byte> vecResponse = new Vector<>();
                CpComm.stuDlpQEngineStatusInfo stInfo = new CpComm.stuDlpQEngineStatusInfo();
                try {
                    //sleep(500);
                    vecResponse = vclcom.execute(type,(byte)(which/col),(byte)(which%col)).get();
                    stInfo = ExchangeStuct.ExchangeEngineStatusInfo(vecResponse);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //// TODO: 2017/12/21  add info to view
                String power1 = EnginePowerStatus(stInfo.ucPower1);
                String power2 = EnginePowerStatus(stInfo.ucPower2);
                String power3 = EnginePowerStatus(stInfo.ucPower3);
                String power4 = EnginePowerStatus(stInfo.ucPower4);
                String Ledstatus = LedStatus(stInfo.LedSta);
                final String items[] = {"光源工作 "+stInfo.uiLampTime+"小时",
                        "投影机工作 "+stInfo.uiEngineTime+"小时",
                        "DMD温度 "+stInfo.ucDMDTemp+"℃",
                        "灯泡R温度 "+stInfo.ucLedRTemp+"℃",
                        "灯泡G温度 "+stInfo.ucLedGTemp+"℃",
                        "灯泡B温度 "+stInfo.ucLedBTemp+"℃",
                        "环境温度 "+stInfo.ucEnvTemp+"℃",
                        "DMD风扇 "+stInfo.usDMDFan+"转/分",
                        "RG间风扇 "+stInfo.usLedFan1+"转/分",
                        "GB间风扇 "+stInfo.usLedFan2+"转/分",
                        "过滤器风扇 "+stInfo.usFliterFan+"转/分",
                        "Driver1风扇 "+stInfo.usDriverFan1+"转/分",
                        "Driver2风扇 "+stInfo.usDriverFan2+"转/分",
                        "电源风扇 "+stInfo.usPowerFan+"转/分",
                        "电源1状态 "+power1,
                        "电源2状态 "+power2,
                        "电源3状态 "+power3,
                        "电源4状态 "+power4,
                        "光源状态 "+Ledstatus
                };

                AlertDialog.Builder inter_builder = new AlertDialog.Builder(mContext,1);
                inter_builder.setTitle(mContext.getString(R.string.getSystemInfo));
                inter_builder.setIcon(R.mipmap.ic_launcher);
                inter_builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface inter_dialog, int which) {
                        inter_dialog.dismiss();
                    }
                });
                inter_builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface inter_dialog, int which) {
                        inter_dialog.dismiss();
                    }
                });
                inter_builder.create().show();
                //end // TODO: 2017/12/21
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
    private int index=0;
    private SharedAppData mSharedAppData;
    private int mLastSceneIndex=0;
    private int mFlag=1;
    public int SetModelSave(SharedAppData sharedAppData,int LastSceneIndex,int flag){

        mSharedAppData =sharedAppData;
        mLastSceneIndex=LastSceneIndex;
        mFlag=flag;
        String [] strings = {"自定义模式1","自定义模式2"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,1);
        builder.setTitle(mContext.getString(R.string.getSystemInfo));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;

                //end // TODO: 2017/12/21
                //dialog.dismiss();
            }
        });
        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("SetModelSave onClick","index =  "+index);
                mSharedAppData.setSaveModelInfo(index,mLastSceneIndex,mFlag);
                ArrayList<SingleSceneCell> sceneCells = mSharedAppData.getSceneCell(mLastSceneIndex);
                int i=0;
                for (SingleSceneCell scene_cell :sceneCells) {
                    i++;
                    Log.d("SetModelSave onClick","i =  "+i);
                    mSharedAppData.saveModelSignal(mLastSceneIndex,i);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
        Log.d("SetModelSave return","index =  "+index);
        return index;
    }
    public int getIndex(){
        return this.index;
    }
}
