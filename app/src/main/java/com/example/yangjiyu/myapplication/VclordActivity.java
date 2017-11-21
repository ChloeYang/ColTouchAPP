package com.example.yangjiyu.myapplication;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import commprocess.VCL3CommProcess;
import engine.CpComm;

import static engine.CpComm.*;

public class VclordActivity extends AppCompatActivity {

    private Button mButton;
    private IPEditText mIpText;
    private Spinner mSpinner;
    private String mVclordIp;
    private int mPort=5800;
    private SYS_INFO mCurSysInfo;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vclord);

        mSpinner = (Spinner)findViewById(R.id.vclord_ip);
        adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,intData()
        );
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Todo spinner selectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVclordIp =mIpText.getText().toString();
                Toast.makeText(getApplicationContext(),""+mVclordIp+":"+mPort,Toast.LENGTH_SHORT).show();

                VCL3CommProcess vcl3CommProcess=new VCL3CommProcess(mVclordIp,mPort );
                Vector<SYS_INFO> vecSys=new Vector<SYS_INFO>();
                vcl3CommProcess.QueryAllSystem(vecSys);
                try {
                    vcl3CommProcess.ProcessCancel();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (vecSys.isEmpty()) {
                    Toast.makeText(getApplicationContext(),R.string.error_no_system,Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter.remove("    ");
                    Iterator<SYS_INFO> iterator= vecSys.iterator();
                    SYS_INFO sys_info;
                    while (iterator.hasNext()) {
                        sys_info=iterator.next();
                        adapter.add("系统号"+sys_info.sysID+" :"+sys_info.uiRow+"行 "+sys_info.uiCol+"列");
                    }
                }

//                String[] languages=getResources().getStringArray(R.array.languages);
//                Toast.makeText(getApplicationContext(),"selected :"+languages[position],Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),""+mSpinner.getSelectedItem(),Toast.LENGTH_SHORT).show();
                int index=position;
                Iterator<SYS_INFO> iterator= vecSys.iterator();
                while (index>0 && iterator.hasNext())
                {
                    iterator.next();
                }
                mCurSysInfo=iterator.next();
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

        mButton = (Button)findViewById(R.id.connect_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private List<String> intData()
    {
        // TODO Auto-generated method stub
        List<String> datalist=new ArrayList<String>();
        datalist.add("  ");
        return datalist;
    }

}
