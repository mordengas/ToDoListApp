package com.dominik_machnik.ToDoApp.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.dominik_machnik.ToDoApp.R;
import com.dominik_machnik.ToDoApp.activities.MainActivity;

import java.util.Calendar;
import java.util.Date;


public class NotificationUtils {


    public void showNotification(Context context, String title, String description) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_check);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "powiadomienia";
            CharSequence nameNot = "powiadomienia";
            String Description = "Description of channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, nameNot, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(false);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "powiadomienia")
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_check)
                .setLargeIcon(bitmap)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentText(description)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);


        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);


        notificationManager.notify(0, notification.build());
    }


    public void showNotificationAtDateTime(Context context, String title, String description, String date, String time) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "powiadomieniaTask";
            CharSequence name = "My channel";
            String Description = "My channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(Description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setShowBadge(false);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context.getApplicationContext(), NotificationPublisher.class);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "powiadomieniaTask")
                    .setContentTitle(title)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.ic_baseline_access_time)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setContentText(description)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true);

            intent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
            intent.putExtra(NotificationPublisher.NOTIFICATION, builder.build());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String[] dateArray = date.split("/");
            String[] timeArray = time.split(":");

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
            cal.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]) - 1);


            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

        }
    }
}