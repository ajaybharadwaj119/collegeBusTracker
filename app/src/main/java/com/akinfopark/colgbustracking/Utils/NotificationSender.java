package com.akinfopark.colgbustracking.Utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

public class NotificationSender {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sendNotification(final String serverKey, final String body, final String title) {
        Log.i("Keys", "server key = " + serverKey + "||   body =" + body + " ||  title = " + title);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject notifJson = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    notifJson.put("text", body);
                    notifJson.put("title", title);
                    notifJson.put("priority", "high");
                    dataJson.put("customId", "02");
                    dataJson.put("badge", 1);
                    dataJson.put("alert", "Alert");
                    json.put("notification", notifJson);
                    json.put("data", dataJson);
                    json.put("to", "/topics/topic");
                    RequestBody requestBody = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + serverKey)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.i("NotificationSender", finalResponse);
                } catch (Exception e) {
                    Log.e("NotificationSender", "Error sending notification", e);
                }
                return null;
            }
        }.execute();
    }

}