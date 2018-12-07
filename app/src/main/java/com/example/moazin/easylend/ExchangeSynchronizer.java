package com.example.moazin.easylend;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ExchangeSynchronizer implements Runnable {
    Context mContext;

    public ExchangeSynchronizer(Context context){
        mContext = context;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Log.e("THREAD:", "From thread, KiDDO!");
        Intent intent = new Intent();
        intent.setAction("moazin.khatri");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}

