package com.example.myapplication.worker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MediaVaultApplication;
import com.example.myapplication.R;
import com.example.myapplication.ui.DetailActivity;

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 1001;

    public static void showTrendingNotification(Context context, int movieId, String title, String posterPath) {

        // Load image as bitmap for notification
        if (posterPath != null) {
            String posterUrl = "https://image.tmdb.org/t/p/w342" + posterPath;

            Glide.with(context)
                    .asBitmap()
                    .load(posterUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,
                                @Nullable Transition<? super Bitmap> transition) {
                            sendNotification(context, movieId, title, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } else {
            sendNotification(context, movieId, title, null);
        }
    }

    private static void sendNotification(Context context, int movieId, String title, Bitmap largeIcon) {
        // Intent to open details
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movieId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MediaVaultApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setContentTitle("New Trending Movie")
                .setContentText("Check out " + title + " today!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon)
                    .setStyle(
                            new NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon((Bitmap) null));
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, cannot show notification
                return;
            }
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
