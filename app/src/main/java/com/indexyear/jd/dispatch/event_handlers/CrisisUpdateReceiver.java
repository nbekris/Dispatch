package com.indexyear.jd.dispatch.event_handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CrisisUpdateReceiver extends BroadcastReceiver {

    final String TAG = "CrisisUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String crisisString = intent.getStringExtra("example crisis info");

    }
}
