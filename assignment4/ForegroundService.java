package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Enables foreground service
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ForegroundService extends Service {
    private static final String LOG_TAG = "ForegroundService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // Starting the foreground service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, MapsActivity.class); // Activity using foreground service
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, PendingIntent.FLAG_UPDATE_CURRENT,
                    notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);

            // Setting notification banner for foreground service
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("FTS")
                    .setTicker("Masi FTS")
                    .setContentText("My Run")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true).build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);

        }

        // Stopping foreground service
        else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used in case of bound services.
        return null;
    }
}
