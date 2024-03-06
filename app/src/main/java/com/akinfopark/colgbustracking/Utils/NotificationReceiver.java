package com.akinfopark.colgbustracking.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        String action = intent.getAction();

        // Check if the intent action matches the action of your notification
        if (action != null && action.equals("your_notification_action")) {
            // Handle your notification here
            Log.d(TAG, "Notification received");

            // You can perform any necessary actions, such as displaying a notification, updating UI, etc.
        }
    }
}
