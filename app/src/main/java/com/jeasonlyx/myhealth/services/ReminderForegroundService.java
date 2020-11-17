package com.jeasonlyx.myhealth.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.jeasonlyx.myhealth.NavigationActivity;
import com.jeasonlyx.myhealth.R;
import com.jeasonlyx.myhealth.notification.AlarmReceiver;

import java.util.List;

import static com.jeasonlyx.myhealth.MyHealthApplication.CHANNEL_FOREGROUND_ID;
import static com.jeasonlyx.myhealth.MyHealthApplication.RECEIVER_INTENT_FILTER_REMINDER;
import static com.jeasonlyx.myhealth.MyHealthApplication.STOP_FOREGROUND_SERVICE;

public class ReminderForegroundService extends Service {

    public static final int FOREGROUND_NOTIFICATION_ID = 686868999;
    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        // Send foreground notification
        startForeGroundNotification();

        // Register broadcast
        IntentFilter filter_reminder = new IntentFilter(RECEIVER_INTENT_FILTER_REMINDER);
        registerReceiver(alarmReceiver, filter_reminder);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(STOP_FOREGROUND_SERVICE.equals(intent.getAction())){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(FOREGROUND_NOTIFICATION_ID);
            stopSelf();
        }

        // Here you can do something
        Toast.makeText(this,
                "Foreground Running: " + isServiceRunning(ReminderForegroundService.class),
                Toast.LENGTH_SHORT);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // Unregister broadcast
        unregisterReceiver(alarmReceiver);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startForeGroundNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_FOREGROUND_ID);

        // For Clicking Notification action
        Intent intentActivity = new Intent(this, NavigationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intentActivity, 0);

        // For Stop this service
        Intent stopSelf = new Intent(this, ReminderForegroundService.class);
        stopSelf.setAction(STOP_FOREGROUND_SERVICE);
        PendingIntent pendingStopSelf = PendingIntent.getService(this, 8,
                stopSelf, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_tech_plus_nurse);

        builder.setSmallIcon(R.drawable.ic_security)   // have to set for notification to work
                //.setContentTitle("My Health")
                .setContentText("My Health is guarding your health reminders")
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_close, "End Service", pendingStopSelf)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setColor(Color.GREEN) // Color for text
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM);

        startForeground(FOREGROUND_NOTIFICATION_ID, builder.build());
    }

    public boolean isServiceRunning(Class<?> serviceClass){

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(50);
        for(ActivityManager.RunningServiceInfo serviceInfo: runningServices){
            if(serviceClass.getName().equals(serviceInfo.service.getClassName())){
                if(serviceInfo.foreground){
                    return true;
                }
            }
        }
        return false;
    }
}
