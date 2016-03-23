package com.example.shashankshekhar.servicedemo.Mqtt;

import android.content.Context;
import android.content.Intent;

import com.example.shashankshekhar.servicedemo.FirstService;
import com.example.shashankshekhar.servicedemo.UtilityClasses.CommonUtils;

/**
 * Created by shashankshekhar on 23/03/16.
 */
public class MqttReconnecter {
    private final int THREAD_SLEEP_TIME = 4*60*1000; // 4 minutes sleep time between each sleep
    private boolean runReconnectorThread = false;
    private Context appContext;
    public MqttReconnecter (Context appContext) {
        this.appContext = appContext;
    }
    public void setRunReconnectorThread(boolean flag) {
        runReconnectorThread  = flag;
    }
    public void startReconnectorThread () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                while (runReconnectorThread) {
                    try {
                        Thread.sleep(THREAD_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ((SCMqttClient.isMqttConnected() == false) &&
                            (MqttConnector.isConnecting == false) &&
                            CommonUtils.isNetworkAvailable(appContext) == true ) {
                        Intent serviceIntent = new Intent(appContext, FirstService.class);
                        serviceIntent.putExtra("fromReconnecter", true);
                        appContext.startService(serviceIntent);
                    }
                }
            }
        }).start();
    }
}
