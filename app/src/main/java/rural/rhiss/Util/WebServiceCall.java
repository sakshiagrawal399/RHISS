package rural.rhiss.Util;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.NoSuchPaddingException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rural.rhiss.RHISS;


public class WebServiceCall implements Constants {

    private static WebServiceCall apiHelperInstance = null;
    private static String mainUrlString;
    private Request requestBuilder;
    private static CryptLib cryptLib;


    public static WebServiceCall getWebServiceCallInstance(String url) {
        apiHelperInstance = new WebServiceCall();
        try {
            cryptLib = new CryptLib();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        mainUrlString = url;
        return apiHelperInstance;
    }

    //Method to get data from the server
    public WebServiceCall get() {
        requestBuilder = new Request.Builder()
                .url(mainUrlString)
                .build();

        return apiHelperInstance;
    }

    public WebServiceCall post(JSONObject jsonObject, Context context) {
        try {
            String output = cryptLib.encrypt(jsonObject.toString(), KEY, IV); //encrypt
            FormBody.Builder formBuilder = new FormBody.Builder().add("Request", output.replace("\n",""));


            RequestBody formBody = formBuilder.build();
            String accessToken = RHISS.getPreferenceManager().getAccessTokenSharedPreference();
            if (accessToken.equals("")) {
                requestBuilder = new Request.Builder()
                        .url(mainUrlString)
                        .post(formBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Device-Type", "android")
                        .addHeader("Application-Version", Utils.getApplicationVersionName(context))
                        .addHeader("Authorization-Key", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789")
                        .build();
            } else {
                requestBuilder = new Request.Builder()
                        .url(mainUrlString)
                        .post(formBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Device-Type", "android")
                        .addHeader("Application-Version", Utils.getApplicationVersionName(context))
                        .addHeader("Authorization-Key", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
                        .addHeader("Authorization", accessToken)
                        .build();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return apiHelperInstance;
    }

    public WebServiceCall post( Context context) {
        try {
            String accessToken = RHISS.getPreferenceManager().getAccessTokenSharedPreference();
            if (accessToken.equals("")) {
                requestBuilder = new Request.Builder()
                        .url(mainUrlString)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Device-Type", "android")
                        .addHeader("Application-Version", Utils.getApplicationVersionName(context))
                        .addHeader("Authorization-Key", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
                        .build();
            } else {
                requestBuilder = new Request.Builder()
                        .url(mainUrlString)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Device-Type", "android")
                        .addHeader("Application-Version", Utils.getApplicationVersionName(context))
                        .addHeader("Authorization-Key", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
                        .addHeader("Authorization", accessToken)
                        .build();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return apiHelperInstance;
    }


    // Method to check response from the get request
    public void execute(WebServiceCallBackHandler callback, String serviceName) {

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Response okss = okHttpClient.newCall(requestBuilder).execute();
            String response = okss.body().string();
            if (callback != null) {
                callback.onServiceCallSucceed(serviceName, cryptLib.decrypt(response, KEY, IV));
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onServiceCallFailed(serviceName, e);
            }
            e.printStackTrace();
        }
    }

    //Method to handle the response from the server for the post resquest
    public void executeAsync(final WebServiceCallBackHandler callback, final String serviceName) {

        new AsyncTask<Void, Void, Void>() {

            String response = null;
            Exception exception = null;
            String responseStatus = null;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient.Builder client = new OkHttpClient.Builder();
                    client.readTimeout(120, TimeUnit.SECONDS);
                    client.writeTimeout(120, TimeUnit.SECONDS);
                    client.connectTimeout(600, TimeUnit.SECONDS).build();
                    Response okss = client.build().newCall(requestBuilder).execute();
                    response = okss.body().string();
                    response = cryptLib.decrypt(response, KEY, IV);
                    JSONObject objResponse = new JSONObject(response);
                    responseStatus = objResponse.getString("status");
                } catch (Exception e) {
                    this.exception = e;
                    response = null;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (response != null && responseStatus.equals("true")) {
                    callback.onServiceCallSucceed(serviceName, response);
                } else if (response != null && responseStatus.equals("false")) {
                    callback.onServiceStatusFailed(serviceName, response);
                } else {
                    callback.onServiceCallFailed(serviceName, exception);
                }
            }
        }.execute();
    }

    public interface WebServiceCallBackHandler {

        void onServiceCallSucceed(String serviceName, String response);

        void onServiceCallFailed(String serviceName, Exception e);

        void onServiceStatusFailed(String serviceName, String response);

    }
}