package com.jeasonlyx.myhealth;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class MyHealthApplication extends Application {

    public static final String CHANNEL_FOREGROUND_ID = "com.jeasonlyx.myhealth.notification.Reminder_Foreground_Service_Channel";
    public static final String CHANNEL_FOREGROUND_NAME = "Reminder_Foreground_Service_Channel";

    public static final String CHANNEL_REMINDER_ID = "com.jeasonlyx.myhealth.notification.Reminder_Notification_Channel";
    public static final String CHANNEL_REMINDER_NAME = "Reminder_Notification_Channel";


    public static final String RECEIVER_INTENT_FILTER_REMINDER = "com.jeasonlyx.myhealth.notification.Reminder_Intent_Filter";

    public static final String INTENT_EXTRA_REMINDER_NOTIFICATION = "com.jeasonlyx.myhealth.notification.Reminder_Intent_EXTRA";
    public static final String STOP_FOREGROUND_SERVICE = "com.jeasonlyx.myhealth.notification.STOP_FOREGROUND_SERVICE";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    /*
    * Create Notifications When App starting
    * "Creating an existing notification channel with its original values performs no operation"
    * */

    public void createNotificationChannels(){

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel_foreground = new NotificationChannel(CHANNEL_FOREGROUND_ID, CHANNEL_FOREGROUND_NAME, importance);
            channel_foreground.enableLights(false);
            channel_foreground.enableVibration(false);
            //channel_foreground.setAllowBubbles(false);
            //channel_foreground.setLightColor(R.color.colorPrimary);
            channel_foreground.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel_foreground);


            int importance2 = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel_reminder = new NotificationChannel(CHANNEL_REMINDER_ID, CHANNEL_REMINDER_NAME, importance2);
            //channel_reminder.enableLights(false);
            //channel_reminder.enableVibration(true);
            //channel_reminder.setLightColor(androidx.core.R.color.notification_icon_bg_color);
            //channel_reminder.setShowBadge(true);
            //channel_reminder.setAllowBubbles(true);
            channel_reminder.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel_reminder);
        }

    }
}
