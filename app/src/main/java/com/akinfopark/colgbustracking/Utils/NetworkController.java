package com.akinfopark.colgbustracking.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.akinfopark.colgbustracking.API.APICallbacks;
import com.akinfopark.colgbustracking.API.APIStatus;
import com.android.volley.RequestQueue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

public class NetworkController {

    Context activity;
    private static String nextLine = "\n";
    RequestQueue requestQueue;
    PostRequestTask requestTask;

    public NetworkController(Context activity) {
        this.activity = activity;
    }

    public static NetworkController networkController;

    public static synchronized NetworkController getInstance() {
        if (networkController == null) {
            networkController = new NetworkController(App.getInstance());
        }
        return networkController;

    }


    public void callApiFile(Context activity, String url, Map<String, String> mapParams, Map<String, String> mapFiles, Map<String, List<String>> mapFilesList, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {

        ANRequest.MultiPartBuilder<?> multiPartBuilder = AndroidNetworking.upload(url);
        Log.i("APIHandler" + tag, url);
        Log.i("APIHandler" + tag, "" + mapParams);
        Log.i("APIHandler" + tag, "" + mapFiles);
        Log.i("APIHandler" + tag, "" + mapFilesList);
        if (mapParams != null) {
            for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                String key = entry.getKey(), value = entry.getValue();
                multiPartBuilder.addMultipartParameter(key, value);

            }
        }
        if (mapFiles != null) {
            for (Map.Entry<String, String> entry : mapFiles.entrySet()) {
                String key = entry.getKey(), value = entry.getValue();
                multiPartBuilder.addMultipartFile(key, new File(value));

            }
        }
        if (mapFilesList != null) {
            for (Map.Entry<String, List<String>> entry : mapFilesList.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                for (int j = 0; j < value.size(); j++) {
                    multiPartBuilder.addMultipartFile(key, new File(value.get(j)));
                }
            }
        }

        multiPartBuilder.setTag(tag)

                .setPriority(Priority.IMMEDIATE);
        ANRequest<?> request = multiPartBuilder.build().
                setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        int progress = (int) ((bytesUploaded * 100) / totalBytes);
                        if (objectExtras != null) {
                            objectExtras.putLong("bytesUploaded", bytesUploaded);
                            objectExtras.putLong("totalBytes", totalBytes);
                        }
                        apiCallbacks.taskProgress(tag, progress, objectExtras);


                    }
                });
        getResponse(activity, request, tag, objectExtras, apiCallbacks);

    }

    public static void getResponse(final Context activity, ANRequest<?> request, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {
       /* request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.i("APIHandler", "" + response);
                apiCallbacks.taskEnd(tag, response);
            }

            @Override
            public void onError(ANError anError) {
                apiCallbacks.taskEnd(tag, anError.getMessage());
                Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
        request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                //CommonFunctions.writeAPIResponse(response, activity);

                Log.i("APIHandler" + tag, "" + response);
                Log.i("APIHandler " + tag, response.replace(":null", ":\"\""));
                try {
                    JSONObject objectRes = new JSONObject(response.replace(":null", ":\"\""));
                    apiCallbacks.taskFinish(APIStatus.SUCCESS, tag, objectRes, "", objectExtras);
                } catch (JSONException e) {
                    apiCallbacks.taskFinish(APIStatus.FAILED, tag, new JSONObject(), e.getMessage(), objectExtras);
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                Log.i("APIHandlerErr" + tag, "" + anError.getErrorCode() + " " + anError.getMessage() + " " + anError.getErrorDetail() + " " + anError.getErrorBody());
                String message = "Error!! " + anError.getMessage();
                if (message.toLowerCase().contains("unknownhost")) {
                    message = "Check Internet!";
                } else if (message.toLowerCase().contains("timeout")) {
                    message = "Timeout!";
                }
                apiCallbacks.taskFinish(APIStatus.FAILED, tag, new JSONObject(), anError.getMessage(), objectExtras);
                if (anError.getErrorCode() >= 400) {
                    if (activity != null)
                        Toast.makeText(activity, anError.getErrorCode() + " Error in connecting to server", Toast.LENGTH_SHORT).show();
                } else {
                    if (anError.getMessage() != null && activity != null){
                        // Toast.makeText(activity, anError.getErrorCode() + " " + message, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public static void buildPostman(JSONObject objectParams, String tag) {
        StringBuilder postman = new StringBuilder();
        if (objectParams.length() > 0) {

            try {
                JSONArray arrayParams = objectParams.names();
                Log.i("ParamKeys", objectParams.toString());
                Log.i("ParamKeys", arrayParams.toString());
                for (int i = 0; i < arrayParams.length(); i++) {
                    String key = arrayParams.getString(i);
                    String value = objectParams.getString(key);
                    postman.append(key);
                    postman.append(":");
                    postman.append(value);
                    postman.append("\n");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("APIHandler" + tag, postman.toString());
    }

    public void callApiPost(Activity activity, String url, final Map<String, String> mapParams, final String tag, final Bundle bundle, final APICallbacks apiCallbacks) {
        Log.i("APIHandler" + tag, url);

        //asyncTask = new PostRequestAsyncTask(activity, mapParams, bundle, apiCallbacks);
        //asyncTask.execute(url, tag);

        StringBuilder postman = new StringBuilder();
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            postman.append(entry.getKey()).append(":").append(entry.getValue());
            postman.append("\n");
        }

        Log.i("APIHandler" + tag, postman.toString());

        ANRequest.PostRequestBuilder<?> postRequestBuilder = AndroidNetworking.post(url);
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            String key = entry.getKey(), value = entry.getValue();
            postRequestBuilder.addBodyParameter(key, value);
        }
        postRequestBuilder.setTag(tag);

        ANRequest<?> request = postRequestBuilder.build();
        postRequestBuilder.setPriority(Priority.IMMEDIATE);
        getResponse(activity, request, tag, bundle, apiCallbacks);
        // CommonFunctions.writeAPIResponse(url + nextLine + "" + mapParams + nextLine + "", activity);
    }


    public void callApiGet(Activity activity, String url, final Map<String, String> mapParams, final String tag, final Bundle bundle, final APICallbacks apiCallbacks) {
        Log.i("APIHandler" + tag, url);

        //asyncTask = new PostRequestAsyncTask(activity, mapParams, bundle, apiCallbacks);
        //asyncTask.execute(url, tag);

        StringBuilder postman = new StringBuilder();
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            postman.append(entry.getKey()).append(":").append(entry.getValue());
            postman.append("\n");
        }

        Log.i("APIHandler " + tag, postman.toString());

        ANRequest.GetRequestBuilder<?> postRequestBuilder = AndroidNetworking.get(url);


        postRequestBuilder.setTag(tag);
        ANRequest<?> request = postRequestBuilder.build();
        postRequestBuilder.setPriority(Priority.IMMEDIATE);
        getResponse(activity, request, tag, bundle, apiCallbacks);
        // CommonFunctions.writeAPIResponse(url + nextLine + "" + mapParams + nextLine + "", activity);
    }

    public void cancel(Activity activity) {
        Log.i("APIHadler Cancel", "Cancel");
        // requestQueue.cancelAll(activity);
        AndroidNetworking.cancel(activity);

    }

    public void cancel(String[] strings) {
        Log.i("APIHandler Cancel", "Cancel");
        // requestQueue.cancelAll(activity);
        for (String s : strings
        ) {
            AndroidNetworking.forceCancel(s);
        }

       /* if (asyncTask != null) {
            asyncTask.cancel(true);
        }*/
    }

    public void cancel() {
        requestQueue.cancelAll(activity);
        AndroidNetworking.cancelAll();
        if (requestTask != null) {
            requestTask.cancel(true);
        }
    }


    public class PostRequestTask extends AsyncTask<String, Void, String> {

        String tag;
        Map<String, String> map;

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    public static void downloadFile(Activity activity, String url, final String dirPath, final String fileName, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {


        //  File saveFilePath = new File(cDir.getPath() + "/lkf/" + filename);

        final String filePath = dirPath + "/" + fileName;

        Log.i("Download", dirPath);
        Log.i("Download", fileName);

        AndroidNetworking.download(url, dirPath.toString(), fileName)
                .setTag(tag)
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {

                        int progress = (int) ((bytesDownloaded * 100) / totalBytes);
                        objectExtras.putLong("bytesUploaded", bytesDownloaded);
                        objectExtras.putLong("totalBytes", totalBytes);
                        apiCallbacks.taskProgress(tag, progress, objectExtras);


                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.i("Download", filePath);
                        try {
                            JSONObject object = new JSONObject();
                            object.put("response_code", "1");
                            object.put("file", filePath);
                            apiCallbacks.taskFinish(APIStatus.SUCCESS, tag, object, "", objectExtras);
                        } catch (JSONException e) {
                            apiCallbacks.taskFinish(APIStatus.FAILED, tag, new JSONObject(), e.getMessage(), objectExtras);
                            e.printStackTrace();
                        }
                        // do anything after completion
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.i("Download", "" + error.getMessage());

                        apiCallbacks.taskFinish(APIStatus.FAILED, tag, new JSONObject(), error.getMessage(), objectExtras);
                        // handle error
                    }
                });
    }


}
