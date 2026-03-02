package com.example.myapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.worker.TrendingWorker;

import java.util.concurrent.TimeUnit;

public class MediaVaultApplication extends Application {

    public static final String CHANNEL_ID = "media_vault_channel";
    private static final String TRENDING_WORK_NAME = "trending_movie_work";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        scheduleTrendingWorker();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MediaVault Notifications";
            String description = "Daily trending movies and show notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void scheduleTrendingWorker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest trendingWorkRequest = new PeriodicWorkRequest.Builder(TrendingWorker.class, 24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                TRENDING_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                trendingWorkRequest);
    }
}
