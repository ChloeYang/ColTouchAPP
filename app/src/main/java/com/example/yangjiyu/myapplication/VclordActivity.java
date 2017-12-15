package com.example.yangjiyu.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import commprocess.VCL3CommProcess;

import static engine.CpComm.*;

public class VclordActivity extends AppCompatActivity {

    public int INPUT_BOARD_NUM = 4;
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
    MyProgressDialog mProgressDialog;
    private int iCount = 0;
    private boolean ret = false;

    private static Context mInstance;

    public static VCL3CommProcess vcl3CommProcess;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vclord);

        mProgressDialog = MyProgressDialog.createProgressDialog(this, 5000, new MyProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(ProgressDialog dialog) {
                Toast.makeText(getApplicationContext(), "TimeOut", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getApplicationContext(),""+mVclordIp+":"+mPort,Toast.LENGTH_SHORT).show();


                //getSystemInfo.execute();
                /*if (vecSys.isEmpty()) {
                    mButton.setEnabled(false);
                    Toast.makeText(getApplicationContext(),R.string.error_no_system,Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter.clear();
                    adapter.add(getString(R.string.check_system));
                    Iterator<SYS_INFO> iterator= vecSys.iterator();
                    SYS_INFO sys_info;
                    while (iterator.hasNext()) {
                        sys_info=iterator.next();
                        adapter.add(getString(R.string.system_id)+""+sys_info.sysID+" :"+sys_info.uiRow+getString(R.string.system_row)+" "+sys_info.uiCol+getString(R.string.system_col));
                    }
                }*/

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


        mInstance = getApplicationContext();
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

    public static Context getVclordActivity() {
        return mInstance;
    }

    private List<String> intData() {
        // TODO Auto-generated method stub
        List<String> datalist = new ArrayList<String>();
        datalist.add(getString(R.string.check_system));
        return datalist;
    }

    private void saveSetting() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_data_vclordip), mVclordIp);
        editor.putInt(getString(R.string.pref_data_sid), mCurSysInfo.sysID);
        editor.putInt(getString(R.string.pref_data_row), mCurSysInfo.uiRow);
        editor.putInt(getString(R.string.pref_data_col), mCurSysInfo.uiCol);
        editor.commit();

        mRow = (byte) mCurSysInfo.uiRow;
        mCol = (byte) mCurSysInfo.uiCol;
    }

    private void saveSignal(int inputId, byte sigInfo) {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int dvi1, dvi2, hdmi, dp;
/*0 、1  bit 分别表示 DVI0，DVI1
2:     HDMI 信号
3：DP 信号*/


        dvi1 = sigInfo & 0x01;
        dvi2 = sigInfo & 0x02;
        hdmi = sigInfo & 0x04;
        dp = sigInfo & 0x08;
        editor.putInt(getString(R.string.pref_input_all_sig_) + (inputId * INPUT_BOARD_NUM + 0), dvi1);
        editor.putInt(getString(R.string.pref_input_all_sig_) + (inputId * INPUT_BOARD_NUM + 1), dvi2);
        editor.putInt(getString(R.string.pref_input_all_sig_) + (inputId * INPUT_BOARD_NUM + 2), hdmi);
        editor.putInt(getString(R.string.pref_input_all_sig_) + (inputId * INPUT_BOARD_NUM + 3), dp);
        editor.commit();
    }

    private void saveSignalInfo(CpSignalInfo signalInfo) {

        saveSignal(0, signalInfo.ucInput1);
        saveSignal(1, signalInfo.ucInput2);
        saveSignal(2, signalInfo.ucInput3);
        saveSignal(3, signalInfo.ucInput4);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_setting), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
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
        editor.putInt(getString(R.string.pref_pix_x), pixX);
        editor.putInt(getString(R.string.pref_pix_y), pixY);
        editor.commit();
    }

    private class GetSystemInfo extends AsyncTask<String, String, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        protected Void doInBackground(String... phoneNumber) {
            vcl3CommProcess = new VCL3CommProcess(mVclordIp, PORT);
            ret = vcl3CommProcess.QueryAllSystem(vecSys);
            if (!ret) {
                //Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                return null;
            }
            if (vecSys.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.error_no_system, Toast.LENGTH_SHORT).show();
            } else {
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            mProgressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            if (!ret) {
                return;
            }
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

    private class VclordConnect extends AsyncTask<String, String, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... phoneNumber) {
            VCL3CommProcess vcl3CommProcess = new VCL3CommProcess(mVclordIp, PORT);
            if (vcl3CommProcess != null) {
                CpSignalInfo signalInfo = new CpSignalInfo();
                ret = vcl3CommProcess.GetSignalInfo(signalInfo);
                if (!ret) {
                    //Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                    return null;
                }
                saveSignalInfo(signalInfo);

                int outputNum = signalInfo.ucOutputNum;
                Vector<Short> cubeID = new Vector<>();
                for (int irow = 0; irow < mRow; irow++) {
                    for (int icol = 0; icol < mCol; icol++) {
                        short deviceID = (short) (irow * VideoCell.CUBE_ROW_MAX + icol);
                        cubeID.add(deviceID);
                    }
                }
                short s1 = 0, s2 = 0, s3 = 0, s4 = 0;
                for (int i = 0; i < outputNum; i++) {
                    if (i * INPUT_BOARD_NUM >= cubeID.size()) {
                        s1 = 0xff;
                    } else {
                        s1 = cubeID.get(i * INPUT_BOARD_NUM);
                    }
                    if ((i * INPUT_BOARD_NUM + 1) >= cubeID.size()) {
                        s2 = 0xff;
                    } else {
                        s2 = cubeID.get(i * INPUT_BOARD_NUM + 1);
                    }
                    if ((i * INPUT_BOARD_NUM + 2) >= cubeID.size()) {
                        s3 = 0xff;
                    } else {
                        s3 = cubeID.get(i * INPUT_BOARD_NUM + 2);
                    }
                    if ((i * INPUT_BOARD_NUM + 3) >= cubeID.size()) {
                        s4 = 0xff;
                    } else {
                        s4 = cubeID.get(i * INPUT_BOARD_NUM + 3);
                    }
                    ret = vcl3CommProcess.SetSignalPosition((byte) i, s1, s2, s3, s4, mRow, mCol);
                }
                if (!ret) {
                    //Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
                    return null;
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
                return;
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
