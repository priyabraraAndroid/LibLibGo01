package com.lib.liblibgo.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lib.liblibgo.R;
import com.lib.liblibgo.activity.homedashboard.HomeActivity;
import com.lib.liblibgo.activity.homedashboard.fragmentshome.HomeFragment;
import com.lib.liblibgo.common.Constants;
import com.lib.liblibgo.dashboard.apartment_admin.ManageCommunityUsersActivity;
import com.lib.liblibgo.dashboard.notification.NotificationActivity;
import com.lib.liblibgo.dashboard.profile.OrderDetailsActivityNew;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static String badge = "0";
    public static String orderId = "";
    private String title = "";
    private String body = "";
    private String notificationStatus = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("notificationNUmber", "From : " + remoteMessage.getFrom());
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
        body = remoteMessage.getData().get("body");
        badge = remoteMessage.getData().get("badge");
        title = remoteMessage.getData().get("title");
        orderId = remoteMessage.getData().get("order_id");
        notificationStatus = remoteMessage.getData().get("notification_status");

        Log.e("notificationNUmber","Body :"+remoteMessage.getData());
        /*if (badge.equals("1")){
            HomeFragment.viewBadge.setVisibility(View.VISIBLE);
        }else {
            HomeFragment.viewBadge.setVisibility(View.GONE);
        }*/
        if (!remoteMessage.getData().isEmpty()){
            if (orderId.equals("0")){
                 if (notificationStatus.equals("1")){
                    Intent intent = new Intent(this, ManageCommunityUsersActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    String channelId = "Default";
                    NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_noti_bell)
                            .setColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                        manager.createNotificationChannel(channel);
                    }
                    manager.notify(0, builder.build());
                }else {
                     Constants.orderId = orderId;
                     Intent intent = new Intent(this, NotificationActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                     String channelId = "Default";
                     NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                             .setSmallIcon(R.drawable.ic_noti_bell)
                             .setColor(getResources().getColor(R.color.colorPrimaryDark))
                             .setContentTitle(title)
                             .setContentText(body)
                             .setAutoCancel(true)
                             .setContentIntent(pendingIntent);
                     NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                         NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                         manager.createNotificationChannel(channel);
                     }
                     manager.notify(0, builder.build());
                 }

            }else {
                Constants.orderId = orderId;
                Intent intent = new Intent(this, OrderDetailsActivityNew.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order_id",orderId);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_noti_bell)
                        .setColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            }
        }

        //final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.noti_tune);
        //final long[] VIBRATE_PATTERN    = {0, 500};

    }
}
