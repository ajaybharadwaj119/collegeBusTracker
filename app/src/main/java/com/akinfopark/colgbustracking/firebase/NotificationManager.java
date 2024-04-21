package com.akinfopark.colgbustracking.firebase;

import static com.akinfopark.colgbustracking.Utils.NotificationSender.JSON;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationManager {
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(); // Dedicated thread pool

    public static void callNotifAPI(String to) throws JSONException {
        OkHttpClient client = new OkHttpClient();

        String serverKey = "AAAAoiQMXhM:APA91bHY81Ly3hB5dADg8eqB1a6wT7zbqtdqFLYVhykXgpmDDUZ3E7HAt7rIvbg22SbjQaCMp40OCRXf4ri1oinBfKOrGCsfhxtQtH_Ay-Top-j6jTXnvCzi6AvrVx8CO3YUOuIYdfh7"; // Replace with your actual server key
        //String to = NotificationManager.getChattingWithToken(); // Assuming method to retrieve token


      //  String to="ej2v-A4nRPyNqvoov5rNTy:APA91bHuysH__7XaOn5Wv0UiIgvcVKcpTb7wofzcmBbhRvVX-_N2Mwz7XlUQYhPaYwZeiQyXfvSqFnfcxyQwApec0KnG_GCQ0tgmqMoKPTHqx6d45Xwqojgdve0_rLwFVi13Vq_1Rf4J";
       /* JsonObject requestJson = new JsonObject();
        requestJson.add("notification", jsonObject.get("notification"));
        requestJson.add("data", jsonObject.get("data"));
        requestJson.addProperty("to", to);*/

        JSONObject json = new JSONObject();
        JSONObject notifJson = new JSONObject();
        JSONObject dataJson = new JSONObject();
        notifJson.put("text", "body");
        notifJson.put("title", "title");
        notifJson.put("priority", "high");
        dataJson.put("customId", "02");
        dataJson.put("badge", 1);
        dataJson.put("alert", "Alert");
        json.put("notification", notifJson);
        json.put("data", dataJson);
        json.put("to", to);
       // RequestBody requestBody = RequestBody.create(JSON, json.toString());

        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, json.toString());

        Request request = new Request.Builder()
                .url(FCM_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + serverKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure scenario
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle successful response (optional)
            }
        });
    }

    /*public static void sendNotif(String message) {
        executorService.execute(() -> { // Use executor for asynchronous execution
            JsonObject notificationObject = new JsonObject();
            JsonObject dataObject = new JsonObject();
            String username = NotificationManager.getUsername(); // Assuming method to retrieve username
            String userId = NotificationManager.getUserId(); // Assuming methods to retrieve data

            notificationObject.addProperty("title", username);
            notificationObject.addProperty("body", message);
            dataObject.addProperty("userId", userId);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("notification", notificationObject);
            jsonObject.add("data", dataObject);
            jsonObject.addProperty("to", NotificationManager.getChattingWithToken());

            try {
                callNotifAPI(jsonObject);
            } catch (IOException e) {
                // Handle exception here
            }
        });
    }*/

   /* public static void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        UserData userData = *//* Get instance of UserData object *//*; // Assuming UserData exists
                        userData.setToken(token);

                        DocumentReference userRef = db.collection("users").document(userData.getUserId());
                        userRef.update("token", token);
                    }
                });
    }*/
}

