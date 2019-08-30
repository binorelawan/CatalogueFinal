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
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.api.ApiInterface;
import relawan.moviecatalogue.model.moviemodel.MovieResponse;
import relawan.moviecatalogue.view.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static relawan.moviecatalogue.BuildConfig.API_KEY;

public class ReleaseNotification extends BroadcastReceiver {

    private static final String TAG = ReleaseNotification.class.getSimpleName();

    private final int RELEASE_NOTIFY = 103;

    ApiInterface apiInterface;

    public ReleaseNotification() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        loadRelease(context);
    }

    // TODO masih belum selesai
    private void loadRelease(final Context context)  {
        
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = formatter.format(date);


        Call<MovieResponse> call = apiInterface.getReleaseMovie(API_KEY, todayDate, todayDate);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse response:: " + response);

                if (response.isSuccessful())    {
                    showReleaseNotification(context);

                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

                Log.d(TAG, "failed");
            }
        });
    }

    private void showReleaseNotification(Context context)   {

        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Release Notification";


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(context.getString(R.string.release_notification))
                .setContentText(context.getString(R.string.release_message))
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
            notificationManager.notify(RELEASE_NOTIFY, notification);
        }
    }

    public void setRelease(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseNotification.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13); // jam 8 pagi
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null)   {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);



        }

    }

    public void cancelRelease(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_NOTIFY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

    }
}
