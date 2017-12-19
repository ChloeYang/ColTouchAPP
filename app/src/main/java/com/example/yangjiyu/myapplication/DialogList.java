package com.example.yangjiyu.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import engine.CpComm;

/**
 * Created by yangjiyu on 2017/12/19.
 */

public class DialogList {

    private Context mContext;
    public DialogList(Context context) {
        mContext = context;
    }
    public void InterfaceVersion(CpComm.stuDlpQInterfaceVersion stInfo){
        final String items[] = {"版卡类型      MCU      FPGA      PCB      型号",
                "主控板     "+stInfo.stMain.strMcu+"   "+stInfo.stMain.strFpga+"    "+stInfo.stMain.strPcb+"   "+stInfo.stMain.strType,
                "采集卡1    "+stInfo.stSIn1.strMcu+"   "+stInfo.stSIn1.strFpga+"    "+stInfo.stSIn1.strPcb+"   "+stInfo.stSIn1.strType,
                "采集卡2    "+stInfo.stSIn2.strMcu+"   "+stInfo.stSIn2.strFpga+"    "+stInfo.stSIn2.strPcb+"   "+stInfo.stSIn2.strType,
                "采集卡3    "+stInfo.stSIn3.strMcu+"   "+stInfo.stSIn3.strFpga+"    "+stInfo.stSIn3.strPcb+"   "+stInfo.stSIn3.strType,
                "采集卡4    "+stInfo.stSIn4.strMcu+"   "+stInfo.stSIn4.strFpga+"    "+stInfo.stSIn4.strPcb+"   "+stInfo.stSIn4.strType,
                "输出卡1    "+stInfo.stSOut1.strMcu+"   "+stInfo.stSOut1.strFpga+"    "+stInfo.stSOut1.strPcb+"   "+stInfo.stSOut1.strType,
                "输出卡2    "+stInfo.stSOut2.strMcu+"   "+stInfo.stSOut2.strFpga+"    "+stInfo.stSOut2.strPcb+"   "+stInfo.stSOut2.strType,
                "输出卡3    "+stInfo.stSOut3.strMcu+"   "+stInfo.stSOut3.strFpga+"    "+stInfo.stSOut3.strPcb+"   "+stInfo.stSOut3.strType};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,3);
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
}