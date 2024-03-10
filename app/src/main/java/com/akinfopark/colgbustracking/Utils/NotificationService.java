package com.akinfopark.colgbustracking.Utils;

import static com.akinfopark.colgbustracking.Utils.CommonConstants.SHARED_PREF_NAME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.akinfopark.colgbustracking.MainActivity;
import com.akinfopark.colgbustracking.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    int i = 0, not_id = 0;
    int count = 0;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.firebase_token), s);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        count++;
        Log.i("checkRemoteMsg",remoteMessage.toString());

        //MyPrefs.getInstance(getApplicationContext()).putInt(UserData.KEY_NOTIF_COUNT,count);

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
        Map<String, String> receivedMap = remoteMessage.getData();

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String icon = "";
        String type = "";

       /* int i = App.getInstance().myPrefs.getInt("unreadcount");
        i = i + 1;*/

        sendNotification(title, body, icon, type, receivedMap);

        //  broadcaster.sendBroadcast(intent);

        /*    Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.mipmap.logo_test)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
         manager.notify(123456789, notification);*/
    }

    private void sendNotification(String title, String messageBody, String image, String type, Map<String, String> map) {
       /* SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();*/


        //  if (!channelId.equalsIgnoreCase("MissedCall") && !channelId.equalsIgnoreCase("chat")) {
        int counter = MyPrefs.getInstance(getApplicationContext()).getInt("notifycount");
        MyPrefs.getInstance(getApplicationContext()).putInt("notifycount", counter + 1);
        // }

        Bitmap bitmap = null;
        try {
            URL url = new URL(image);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        }
        byte[] byteArray = bStream.toByteArray();

//        boolean logged = App.getInstance().myPrefs.getBoolean(UserData.KEY_USER_ID);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
        Intent intent;
        stackBuilder.addParentStack(MainActivity.class);
        Bundle bundle = new Bundle();
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

       /* if (MyPrefs.getInstance(getApplicationContext()).getBoolean(UserData.KEY_LOGGED_IN)) {
            intent = new Intent(this, NotificationActivity.class);
            stackBuilder.addNextIntentWithParentStack(intent);

        } else {
            intent = new Intent(this, MainActivity.class);
            stackBuilder.addNextIntentWithParentStack(intent);
        }*/

       /* if (MyPrefs.getInstance(getApplicationContext()).getBoolean("login")) {
            intent = new Intent(this, Activitynotification.class);
            stackBuilder.addNextIntentWithParentStack(intent);

        } else {
            intent = new Intent(this, HomeActivity.class);
            stackBuilder.addNextIntentWithParentStack(intent);
        }*/
        /*if (logged) {
            Log.d("Logged In", "L1");


            intent = new Intent(this, NotificationClickActivity.class);
            if (type.equalsIgnoreCase("paymentsuccess")) {
                bundle.putString("details", map.get("details"));
                bundle.putString("review", map.get("review"));

                intent = new Intent(this, BankTransactionClickActivity.class);
            }
            bundle.putString("type", "notification");

            switch (type) {

                case "chat":
                    bundle.putString("type", "chat");
                    intent.putExtra("type", "chat");

                    break;
                case "freshchat_user":
                    intent = new Intent(this, HomeActivity.class);
                    bundle.putString("type", "chat");
                    bundle.putBoolean("gotofreshchat", true);
                    intent.putExtra("type", "chat");
                    break;
                case "ticket":
                    bundle.putString("type", "ticket");
                    intent.putExtra("type", "ticket");

                    break;
                default:
                    bundle.putString("type", "notification");
                    intent.putExtra("type", "normal");

                    break;
            }

        } else {
            Log.d("Logged In", "L0");
            intent = new Intent(this, LoginActivity.class);
        }
        intent.putExtras(bundle);*/

// Adds the back stack for the Intent (but not the Intent itself)
// Adds the Intent that starts the Activity to the top of the stack

        //  PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
        s.setSummaryText(messageBody);
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody));
        //.setStyle(s);

        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager.class);
        } else {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (notificationManager != null) {
            notificationManager.notify(not_id, notificationBuilder.build());
        }
        not_id++;
    }
}
