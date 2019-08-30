package relawan.moviecatalogue.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import relawan.moviecatalogue.view.MainActivity;
import relawan.moviecatalogue.R;

public class ReminderNotification extends BroadcastReceiver {

    private final int REMINDER_NOTIFY = 104;

    public ReminderNotification() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showReminderNotification(context);
    }

    private void showReminderNotification(Context context) {

        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "Reminder Notification";


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(context.getString(R.string.reminder_notification))
                .setContentText(context.getString(R.string.reminder_message))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(REMINDER_NOTIFY, notification);
        }
    }

    public void setReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderNotification.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11); // jam 7 pagi
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null)   {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);



        }


    }

    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REMINDER_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

    }

}

