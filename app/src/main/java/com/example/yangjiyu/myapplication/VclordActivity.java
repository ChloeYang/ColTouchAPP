package com.example.yangjiyu.myapplication;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import commprocess.ExchangeStuct;
import commprocess.VCL3CommProcess;

import static engine.CpComm.*;

public class VclordActivity extends AppCompatActivity {

    public static int INPUT_BOARD_NUM = 4;
    private Button mButton;
    private IPEditText mIpText;
    private Spinner mSpinner;
    private String mVclordIp = "172.16.129.4";
    private byte mRow = 1;
    private byte mCol = 1;
    public static int PORT = 5800;
    private SYS_INFO mCurSysInfo = new SYS_INFO();
    ArrayAdapter<String> adapter;

    Vector<SYS_INFO> vecSys = new Vector<>();
    //ProgressDialog mProgressDialog;
    private MyProgressDialog mProgressDialog;
    private SharedAppData sharedAppData;

    private boolean ret = false;
    private boolean colormode_ret=false;

    public static VCL3CommProcess vcl3CommProcess;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_vclord);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.scene);


        sharedAppData = SharedAppData.newInstance(this);
        mProgressDialog = MyProgressDialog.createProgressDialog(this, 5000, new MyProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(ProgressDialog dialog) {
                Toast.makeText(getApplicationContext(), "TimeOut", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);*/
            }
        });
        mProgressDialog.setTitle(getString(R.string.app_name));
        String messageInfo = getString(R.string.progress_check_system);
        mProgressDialog.setMessage(messageInfo);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mButton = (Button) findViewById(R.id.connect_button);
        mButton.setEnabled(false);

        mIpText = (IPEditText) findViewById(R.id.iptext);
        SharedPreferences ipShare = getSharedPreferences(getString(R.string.pref_setting), Context.MODE_PRIVATE);
        mVclordIp = ipShare.getString(getString(R.string.pref_data_vclordip), "172.16.129.152");
        String[] strIp = mVclordIp.split("\\.");
        if (strIp.length > 0) {
            mIpText.setText(strIp[0], strIp[1], strIp[2], strIp[3]);
        }

        mSpinner = (Spinner) findViewById(R.id.vclord_ip);
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, intData()
        );

        final GetSystemInfo getSystemInfo = new GetSystemInfo();
        getSystemInfo.execute();

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Todo spinner selectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mVclordIp = mIpText.getText();

                String strItem = (String) mSpinner.getSelectedItem();
                if (strItem != "") {
                    //Toast.makeText(getApplicationContext(), "" +strItem, Toast.LENGTH_SHORT).show();
                    int index = position;
                    Iterator<SYS_INFO> iterator = vecSys.iterator();
                    while (index > 1 && iterator.hasNext()) {
                        iterator.next();
                    }
                    if (iterator.hasNext()) {
                        mCurSysInfo = iterator.next();
                    }
                    mButton.setEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_no_system, Toast.LENGTH_SHORT).show();
                    mCurSysInfo.sysID = -1;
                    mCurSysInfo.uiRow = 0;
                    mCurSysInfo.uiCol = 0;
                    mButton.setEnabled(false);
                }

                saveSetting();
                Class<?> myClass = AdapterView.class;
                try {
                    Field field = myClass.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);
                    field.setInt(mSpinner, AdapterView.INVALID_POSITION);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2017/11/22 if unknown ------ get signal info. && set signal position
                VclordConnect vclordConnect = new VclordConnect();
                vclordConnect.execute();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
/*    //// TODO: 2017/12/18 disable HOME_KEY

    //end todo*/
    private List<String> intData() {
        // TODO Auto-generated method stub
        List<String> datalist = new ArrayList<String>();
        datalist.add(getString(R.string.check_system));
        return datalist;
    }

    private void saveSetting() {

        sharedAppData.saveSystemInfo(mVclordIp,mCurSysInfo.sysID, mCurSysInfo.uiRow, mCurSysInfo.uiCol);
        mRow = (byte) mCurSysInfo.uiRow;
        mCol = (byte) mCurSysInfo.uiCol;
    }


    public void saveSignalInfo(CpSignalInfo signalInfo) {

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

    private class GetSystemInfo extends AsyncTask<String, String, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        protected Void doInBackground(String... phoneNumber) {
            vcl3CommProcess = new VCL3CommProcess(mVclordIp, PORT);
            ret = vcl3CommProcess.QueryAllSystem(vecSys);
            try {
                vcl3CommProcess.ProcessCancel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            vcl3CommProcess =null;
            if (!ret) {
                //Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                return null;
            }
            /*if (vecSys.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.error_no_system, Toast.LENGTH_SHORT).show();
            } else {
            }*/
            return null;
        }

        protected void onProgressUpdate(String... values) {
            mProgressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            if (!ret) {
                Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);*/
                return;
            }
            if (vecSys.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.error_no_system, Toast.LENGTH_SHORT).show();
            } else {
                adapter.clear();
                adapter.add(getString(R.string.check_system));
                Iterator<SYS_INFO> iterator = vecSys.iterator();
                SYS_INFO sys_info;
                while (iterator.hasNext()) {
                    sys_info = iterator.next();
                    adapter.add(getString(R.string.system_id) + "" + sys_info.sysID + " :" + sys_info.uiRow + getString(R.string.system_row) + " " + sys_info.uiCol + getString(R.string.system_col));
                }
                mProgressDialog.dismiss();
                mSpinner.setSelection(1);
                Iterator<SYS_INFO> itor = vecSys.iterator();
                if (itor.hasNext()) {
                    mCurSysInfo = itor.next();
                }
                mButton.setEnabled(true);
                saveSetting();
            }
        }
    }


    private class VclordConnect extends AsyncTask<String, String, Void> {

        private String getModelName(Vector<Byte>res) {
            byte[] bytes =new byte[res.size()];
            for (int i=0;i<res.size();i++){
                bytes[i]=res.get(i);
            }
            char[] buf_get = new char[res.size()];
            try {
                /*BufferedReader tmp_get = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), "GBK"));
                int number = tmp_get.read(buf_get);
                char[] result = new char[number];
                System.arraycopy(buf_get, 0, result, 0, number);
                tmp_get.close();
                return String.valueOf(result);*/
                String string = new String(bytes,"GBK");
                return string;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            } /*catch (IOException e) {
                e.printStackTrace();
                return null;
            }*/
        }
        protected void onPreExecute() {
            super.onPreExecute();
            mVclordIp = mIpText.getText();
        }

        protected Void doInBackground(String... str) {

            VCL3CommProcess vcl3CommProcess = new VCL3CommProcess(mVclordIp, PORT);
            if (mVclordIp != sharedAppData.getVCLordIP()) {

                ret = vcl3CommProcess.QueryAllSystem(vecSys);
                if (!ret) {
                    ret = false;
                    return null;
                } else {
                    Iterator<SYS_INFO> itor = vecSys.iterator();
                    if (itor.hasNext()) {
                        mCurSysInfo = itor.next();
                    }
                    saveSetting();
                }
            }

            if (vcl3CommProcess != null) {
                CpSignalInfo signalInfo = new CpSignalInfo();
                ret = vcl3CommProcess.GetSignalInfo(signalInfo);
                if (!ret) {
                    //Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                    return null;
                }
                saveSignalInfo(signalInfo);
                //// TODO: 2017/12/28 get color mode name 6 num
                String modeName;

                for (byte mode=0;mode<3;mode++){
                    Vector<Byte> res=new Vector<>();
                    try {
                        Log.d("mode",":"+mode);
                        colormode_ret = vcl3CommProcess.ColorModeGetName((short)0,(byte)(mode+8),res);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sharedAppData.saveColorModeName(mode,getString(R.string.color_mode)+mode);
                    }
                    if (colormode_ret){
                        //modeName= ExchangeStuct.ExchangeModelName(res);
                        modeName = getModelName(res);
                        Log.d("mode-name",":"+modeName);
                        sharedAppData.saveColorModeName(mode,modeName);
                    }else {
                        sharedAppData.saveColorModeName(mode,getString(R.string.color_mode)+mode);
                    }
                }
                try {
                    vcl3CommProcess.ProcessCancel();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            if (!ret) {
                Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
