package com.jeasonlyx.myhealth.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.jeasonlyx.myhealth.MyHealthApplication.INTENT_EXTRA_REMINDER_NOTIFICATION;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // For Sending Notification
        if(intent.getStringExtra(INTENT_EXTRA_REMINDER_NOTIFICATION) != null){

        }
    }
}
