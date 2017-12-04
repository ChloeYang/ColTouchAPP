package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import commprocess.VCL3CommProcess;

import static engine.CpComm.*;

public class VclordActivity extends AppCompatActivity {

    private Button mButton;
    private IPEditText mIpText;
    private Spinner mSpinner;
    private String mVclordIp="172.16.129.4";
    private int mPort=5800;
    private SYS_INFO mCurSysInfo = new SYS_INFO();
    ArrayAdapter<String> adapter;

    private int iCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vclord);

        mButton = (Button)findViewById(R.id.connect_button);
        mButton.setEnabled(false);

        mIpText=(IPEditText)findViewById(R.id.iptext);
        SharedPreferences ipShare=getSharedPreferences(getString(R.string.pref_setting),Context.MODE_PRIVATE);
        mVclordIp=ipShare.getString(getString(R.string.pref_data_vclordip),"172.16.129.152");
        String[] strIp=mVclordIp.split("\\.");
        if (strIp.length>0) {
            mIpText.setText(strIp[0], strIp[1], strIp[2], strIp[3]);
        }

        mSpinner = (Spinner)findViewById(R.id.vclord_ip);
        adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,intData()
        );
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Todo spinner selectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                iCount++;
//                if(iCount!=1)
//                {
//                    iCount=0;
//                    return;
//                }
                mVclordIp =mIpText.getText();
                //Toast.makeText(getApplicationContext(),""+mVclordIp+":"+mPort,Toast.LENGTH_SHORT).show();

                VCL3CommProcess vcl3CommProcess=new VCL3CommProcess(mVclordIp,mPort );
                Vector<SYS_INFO> vecSys=new Vector<SYS_INFO>();
                vcl3CommProcess.QueryAllSystem(vecSys);
                try {
                    vcl3CommProcess.ProcessCancel();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                }

                //// TODO: 2017/11/22 test vecSys
                SYS_INFO testSys = new SYS_INFO();
                testSys.sysID=2;
                testSys.uiRow=3;
                testSys.uiCol=3;
                vecSys.add(testSys);
                //end test

                if (vecSys.isEmpty()) {
                    Toast.makeText(getApplicationContext(),R.string.error_no_system,Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter.clear();
                    adapter.add("");
                    Iterator<SYS_INFO> iterator= vecSys.iterator();
                    SYS_INFO sys_info;
                    while (iterator.hasNext()) {
                        sys_info=iterator.next();
                        adapter.add(getString(R.string.system_id)+""+sys_info.sysID+" :"+sys_info.uiRow+getString(R.string.system_row)+" "+sys_info.uiCol+getString(R.string.system_col));
                    }
                }

                String strItem=(String)mSpinner.getSelectedItem();
                if (strItem!="") {
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
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error_no_system, Toast.LENGTH_SHORT).show();
                    mCurSysInfo.sysID=-1;
                    mCurSysInfo.uiRow=0;
                    mCurSysInfo.uiCol=0;
                    mButton.setEnabled(false);
                }

                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_setting),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.pref_data_vclordip), mVclordIp);
                editor.putInt(getString(R.string.pref_data_sid),mCurSysInfo.sysID);
                editor.putInt(getString(R.string.pref_data_row),mCurSysInfo.uiRow);
                editor.putInt(getString(R.string.pref_data_col),mCurSysInfo.uiCol);
                editor.commit();

                Class<?>myClass=AdapterView.class;
                try{
                    Field field=myClass.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);
                    field.setInt(mSpinner,AdapterView.INVALID_POSITION);
                }catch (NoSuchFieldException  e){
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//                String strSysInfo=(String)mSpinner.getSelectedItem();
//                String[] str=strSysInfo.split(":");
//                mSid=Integer.parseInt(str[0]);
//                String[] str1=str[1].split("-");
//                mRow=Integer.parseInt(str1[0]);
//                mCol=Integer.parseInt(str1[1]);
//                Toast.makeText(getApplicationContext(),""+mSid+" "+mRow+" "+mCol,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2017/11/22 if unknown
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private List<String> intData()
    {
        // TODO Auto-generated method stub
        List<String> datalist=new ArrayList<String>();
        datalist.add(getString(R.string.check_system));
        return datalist;
    }

    /**
     * Created by yangjiyu on 2017/11/29.
     */

    public static class SingleScene {
    }
}
