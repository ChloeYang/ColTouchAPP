package com.example.yangjiyu.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by yangjiyu on 2017/12/26.
 */

public class PollingService extends Service {

    public static final String ACTION = "com.example.yangjiyu.myapplication.PollingService";

    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.i("polling service","onCreate...");
        initNotifyManager();
        Log.i("polling service","start...");
        new PollingThread().start();
    }

    /*@Override
    public void onStart(Intent intent, int startId) {

    }*/

    //初始化通知栏配置
    private void initNotifyManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //int icon = R.drawable.ic_launcher;
        mNotification = new Notification();
        //mNotification.icon = icon;
        mNotification.tickerText = "New Message";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    //弹出Notification
    private void showNotification() {
        /*mNotification.when = System.currentTimeMillis();
        //Navigator to the new activity when click the notification title
        Intent i = new Intent(this, MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.setLatestEventInfo(this,
                getResources().getString(R.string.app_name), "You have new message!", pendingIntent);
        mManager.notify(0, mNotification);*/
        Log.i("polling service","show notification");
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     */
    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {
            Log.i("polling service","Polling...");
            //System.out.println("");
            count ++;
            //当计数能被5整除时弹出通知
            if (count % 5 == 0) {
                showNotification();
                //System.out.println("New message!");
                Log.i("polling service","New message!");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("polling service","onDestroy");
    }

}