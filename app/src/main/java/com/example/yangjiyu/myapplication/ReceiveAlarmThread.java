package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import commprocess.VCL3CommProcess;
import engine.CpComm;
import typeConversion.ByteUtil;

/**
 * Created by yangjiyu on 2018/1/15.
 */

public class ReceiveAlarmThread extends Thread {
    private final short COMMAND_ID_AUTO_REPORT_ALARM = 0x3003; //告警自动上报
    private final short COMMAND_ID_AUTO_REPORT_EVENT = 0x3009; //事件自动上报
    private final short COMMAND_ID_CUBE_ONOFFLINE = (short)0x9009; //机芯上下线通知
    private static final short BUFFER_SIZE=1024;
    private String m_ip ="";
    private int m_port =5801;
    private Socket m_sock;
    private SharedAppData m_sharedAppData;
    private static ReceiveAlarmThread mReceive=null;
    private static boolean bIsStarted=false;
    private byte[] m_buff=new byte[BUFFER_SIZE];
    private Context m_context;
    private DBHelper dbAlarmHelper;
    private DBHelper dbEventHelper;

    private ReceiveAlarmThread (String ip,  Context context){
        m_ip =ip;
        m_context =context;
        m_sharedAppData = SharedAppData.newInstance(context);

        //创建帮助类实例
        dbAlarmHelper = new DBHelper(m_context, "vtron_alarm.db", null, 1);
        dbAlarmHelper.setM_type(1);
        dbEventHelper = new DBHelper(m_context, "vtron_event.db", null, 1);
        dbEventHelper.setM_type(2);
    }
    public static ReceiveAlarmThread newInstance(String ip, Context context){
        if (mReceive==null){
            mReceive = new ReceiveAlarmThread(ip,context);
            bIsStarted = false;
            return mReceive;
        }else {
            bIsStarted = true;
            return mReceive;
        }
    }
    public static boolean getIsStarted(){
        return bIsStarted;
    }
    private boolean init_connect(){
        try {
            m_sock= new Socket(m_ip, m_port);
            if(m_sock.isClosed())
            {
                Log.d("ReceiveAlarmThread","closed......");
                return false;
            }
            if(m_sock==null)
            {
                Log.d("ReceiveAlarmThread","sock null......");
                return false;
            }
        } catch (IOException e) {
            if (m_sock!=null){
                try {
                    m_sock.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }

        return true;
    }
    private boolean setVclordSysInfo(){
        VCL3CommProcess vcl3CommProcess = new VCL3CommProcess(m_ip, 5800);
        CpComm.SYS_INFO vecSys =new CpComm.SYS_INFO();
        vecSys.sysID= m_sharedAppData.getSystemInfo(5);
        vecSys.uiRow= m_sharedAppData.getSystemInfo(1);
        vecSys.uiCol= m_sharedAppData.getSystemInfo(2);
        vecSys.vecCubID.clear();
        for(int ucRow = 0;ucRow < vecSys.uiRow;ucRow++)
        {
            for(int ucCol = 0;ucCol < vecSys.uiCol;ucCol++)
            {
                short usDeviceId = (short)(ucRow*100+ucCol);
                vecSys.vecCubID.add(usDeviceId);
            }
        }
        /*if (!vcl3CommProcess.SetSystemInfoToVCLord(vecSys)){
            return false;
        }*/
        try {
            vcl3CommProcess.ProcessCancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public void run() {
        //// TODO: 2018/1/15 socketconnect , then  SetSystemIdToVClord
        if(!init_connect()){
            bIsStarted=false;
            return;
        }

        Log.d("ReceiveAlarmThread","start......");
        int r_len=0;
        Vector<Byte> vec=new Vector<>();

        while (true){
            //// TODO: 2018/1/15 Socket listening ,then  SendAlarmToInterFace
            r_len=0;
            InputStream inputStream= null;
            try {
                inputStream = m_sock.getInputStream();
                DataInputStream in=new DataInputStream(inputStream);
                Arrays.fill(m_buff, (byte)0);
                r_len=in.read(m_buff);
            } catch (IOException e) {
                Log.d("ReceiveAlarmThread","InputStream IOException......");
                e.printStackTrace();
            }

/*            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            if(r_len< 0)
            {
                Log.d("Heiko","Out of listener..." + r_len);
                break;
            }
            byte[]b=new byte[r_len];
            System.arraycopy(m_buff, 0, b, 0 , r_len);
            Arrays.fill(m_buff, (byte)0);
            vec.clear();
            for (int i = 0; i < b.length; i++) {
                vec.addElement(b[i]);
            }

            Log.d("ReceiveAlarmThread","run......");
            SendAlarmToInterFace(vec);

        }
        //Looper.loop();
    }
    private final  int LOGTYPE_INVALID = -1;
    private final  int LOGTYPE_ALARM = 0;//自动上报的告警
    private final  int LOGTYPE_EVENT = 1;//自动上报的日志
    private final  int LOGTYPE_NOTIFY = 0x02;//机芯上下线通知
    private void SendAlarmToInterFace(Vector<Byte>vec){
        int nReportType = LOGTYPE_INVALID;
        short usCommandId = 0x00;
        Iterator<Byte> iter=vec.iterator();
        int nLenth;
        byte []b2=new byte[2];
        byte []b4=new byte[4];

        b4[0]=iter.next();
        b4[1]=iter.next();
        b4[2]=iter.next();
        b4[3]=iter.next();
        nLenth= ByteUtil.bntohs(b4);

        b2[0]=iter.next();
        b2[1]=iter.next();
        usCommandId= ByteUtil.bntohs(b2);
        Log.d("ReceiveAlarmThread","usCommandId......"+usCommandId);
        switch (usCommandId)
        {
            case COMMAND_ID_AUTO_REPORT_ALARM://告警
                nReportType = LOGTYPE_ALARM;
                break;
            case COMMAND_ID_AUTO_REPORT_EVENT://事件
                nReportType = LOGTYPE_EVENT;
                break;
            case COMMAND_ID_CUBE_ONOFFLINE://机芯上下线通知
                nReportType = LOGTYPE_NOTIFY;
                break;
            default:
                break;
        }

        if(nReportType == LOGTYPE_INVALID)
        {
            Log.d("ReceiveAlarmThread","LOGTYPE_INVALID......");
            return;//接收到的非告警也非事件
        }

        int nSysId;
        b4[0]=iter.next();
        b4[1]=iter.next();
        b4[2]=iter.next();
        b4[3]=iter.next();
        nSysId= ByteUtil.bntohs(b4);

        Log.d("ReceiveAlarmThread","nSysId......"+nSysId);
        if(nSysId != m_sharedAppData.getSystemInfo(5))//检测是不是当前的控制墙
            return;

        short usDeviceId ;
        b2[0]=iter.next();
        b2[1]=iter.next();
        usDeviceId= ByteUtil.bntohs(b2);

        int row=usDeviceId/100;
        int col=usDeviceId%100;
        //检测是否是本墙内部的告警信息
        Log.d("ReceiveAlarmThread","row......"+row+"~~~~~col......"+col);
        if(row >= m_sharedAppData.getSystemInfo(1) || col >= m_sharedAppData.getSystemInfo(2) )
        {//不是本墙告警信息，抛弃该信息，不处理
            if (row!=100)
            {
                return ;
            }
        }

        //解析出详细告警信息
        short usErrCode = 0;
        b2[0]=iter.next();
        b2[1]=iter.next();
        usErrCode= ByteUtil.bntohs(b2);

        StuAlarmEvent.EventInfo stuEventInfo = new StuAlarmEvent.EventInfo();
        StuAlarmEvent.AlarmInfo stuAlarmInfo = new StuAlarmEvent.AlarmInfo();

        switch (nReportType)
        {
            case LOGTYPE_ALARM:
                GetAlarmInfo(iter, stuAlarmInfo);
                stuAlarmInfo.usCubeId = usDeviceId;
                toastShowAlarm(stuAlarmInfo);
                break;
            case LOGTYPE_NOTIFY:
            case LOGTYPE_EVENT:
                GetEventInfo(iter, stuEventInfo);
                stuEventInfo.usCubeId = usDeviceId;
                toastShowEvent(stuEventInfo);
                break;
            default:
                break;
        }

    }
    private void GetAlarmInfo(Iterator<Byte>iterator, StuAlarmEvent.AlarmInfo stuAlarmInfo){
        Log.d("ReceiveAlarmThread","GetAlarmInfo......");
        stuAlarmInfo.bySlotID = iterator.next();//单板槽位号
        stuAlarmInfo.byEventType = iterator.next();//事件编码
        stuAlarmInfo.dwAlarmID = iterator.next();//告警ID来源(通讯协议中为1个字节)
        stuAlarmInfo.byGrade = iterator.next();//告警级别
        stuAlarmInfo.byType = iterator.next(); //告警类型
        stuAlarmInfo.byUserPri = iterator.next();//用户级别
        for( int ucAlarmDesSize = 0;ucAlarmDesSize < 128;ucAlarmDesSize++)
        {
            stuAlarmInfo.byDescr[ucAlarmDesSize] = iterator.next(); //告警描述信息
        }
        //告警发生时间
        byte []b2=new byte[2];
        b2[0]=iterator.next();
        b2[1]=iterator.next();
        stuAlarmInfo.wYear = ByteUtil.bntohs(b2); //年
        stuAlarmInfo.byMonth = iterator.next();  //月
        stuAlarmInfo.byDay = iterator.next();  //日
        stuAlarmInfo.byHour = iterator.next(); //时
        stuAlarmInfo.byMinute = iterator.next();  //分
        stuAlarmInfo.bySecond = iterator.next(); //秒
    }
    private void GetEventInfo(Iterator<Byte>iterator, StuAlarmEvent.EventInfo stuEventInfo){
        Log.d("ReceiveAlarmThread","GetEventInfo......");
        stuEventInfo.bySlotID = iterator.next();//单板槽位号
        stuEventInfo.byEventType = iterator.next();//事件编码
        stuEventInfo.dwEventID = iterator.next();//告警ID来源(通讯协议中为1个字节)
        for( int ucAlarmDesSize = 0;ucAlarmDesSize < 128;ucAlarmDesSize++)
        {
            stuEventInfo.byDescr[ucAlarmDesSize] = iterator.next(); //告警描述信息
        }
        //告警发生时间
        byte []b2=new byte[2];
        b2[0]=iterator.next();
        b2[1]=iterator.next();
        stuEventInfo.wYear = ByteUtil.bntohs(b2); //年
        stuEventInfo.byMonth = iterator.next();  //月
        stuEventInfo.byDay = iterator.next();  //日
        stuEventInfo.byHour = iterator.next(); //时
        stuEventInfo.byMinute = iterator.next();  //分
        stuEventInfo.bySecond = iterator.next(); //秒
    }
    private void toastShowAlarm(StuAlarmEvent.AlarmInfo stuAlarmInfo){
        String time = "ALARM："+stuAlarmInfo.wYear+"/"+stuAlarmInfo.byMonth+"/"+stuAlarmInfo.byDay+" "+stuAlarmInfo.byHour+":"+stuAlarmInfo.byMinute+":"+stuAlarmInfo.bySecond;
        String cube = " "+"cube"+((stuAlarmInfo.usCubeId/100)* m_sharedAppData.getSystemInfo(2)+(stuAlarmInfo.usCubeId%100)+1);
        String des = new String(stuAlarmInfo.byDescr);
        //Looper.prepare();
        //Toast.makeText(m_context,time+cube+""+des,Toast.LENGTH_SHORT).show();
        //Looper.loop();
        /**
         * getWritableDatabase() ：以可以读写方式获取数据库实例
         * getReadableDatabase() ; 以只读方式获取数据库实例
         * 只有通过调用上述两个方法之一 ，才会去真正执行DBHelper 中的onCreate()方法
         */
        //获取数据库对象
        SQLiteDatabase db = dbAlarmHelper.getWritableDatabase();
        //执行SQL语句
        /**
         * 不难理解，第一个参数写sql语句，将数据真实值用？占位符占位
         * 第二个参数：依次替换占位符中的值
         */

        Log.d("ReceiveAlarmThread",""+time+cube+des);
        db.execSQL("insert into alarm (time,cube,des) values(?,?,?)",new String[]{time,cube,des});
        db.close();
    }
    private void toastShowEvent(StuAlarmEvent.EventInfo stuEventInfo){
        String time = "EVENT："+stuEventInfo.wYear+"/"+stuEventInfo.byMonth+"/"+stuEventInfo.byDay+" "+stuEventInfo.byHour+":"+stuEventInfo.byMinute+":"+stuEventInfo.bySecond;
        String cube = " "+"cube"+((stuEventInfo.usCubeId/100)* m_sharedAppData.getSystemInfo(2)+(stuEventInfo.usCubeId%100)+1);
        String des = new String(stuEventInfo.byDescr);
        //Looper.prepare();
        //Toast.makeText(m_context,time+cube+""+des,Toast.LENGTH_SHORT).show();
        /*SQLiteDatabase db = dbEventHelper.getWritableDatabase();
        Log.d("ReceiveAlarmThread",""+time+cube+des);
        db.execSQL("insert into event (time,cube,des) values(?,?,?)",new String[]{time,cube,des});
        db.close();*/
        //Looper.loop();
    }
}
