package rural.rhiss.Inspection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Upload.UploadPhotoActivity;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.TextViewCustom;
import rural.rhiss.Util.WebServiceCall;

public class InspectionDetailsActivity extends AppCompatActivity implements Constants {
    KProgressHUD kProgressHUD;
    private ListView listView;
    private TextViewCustom headTextView;
    static Context context;
    static HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_details);
        listView = (ListView) findViewById(R.id.listView);
        headTextView = (TextViewCustom) findViewById(R.id.loginText);
        context = getApplicationContext();


    }

    private void getInspectionDetails() {
        hashMap = RHISS.getBeneficiaryDetailObject().getHashMap();
        headTextView.setText(hashMap.get("Name") + "/ " + hashMap.get("Reg_code"));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RegCode", hashMap.get("Reg_code"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        kProgressHUD = KProgressHUD.create(InspectionDetailsActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        WebServiceCall.getWebServiceCallInstance(APP_URL + GET_LEVELS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                kProgressHUD.dismiss();
                ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Reg_code", jsonObject.getString("Reg_code"));
                        hashMap.put("HouseStatusCode", jsonObject.getString("HouseStatusCode"));
                        hashMap.put("HouseStatus", jsonObject.getString("HouseStatus"));
                        hashMap.put("Approved", jsonObject.getString("Approved"));
                        hashMap.put("Response", jsonObject.getString("Response"));
                        hashMap.put("ImagePath", jsonObject.getString("ImagePath"));
                        arrayList.add(hashMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String isUpload = "false";
                for (HashMap<String, String> hashMap : arrayList) {
                    if(hashMap.get("Approved").equals("0")&&isUpload.equals("false")){
                        isUpload="true";
                        hashMap.put("isUpload",isUpload);
                    }else{
                        isUpload="false";
                        hashMap.put("isUpload",isUpload);
                    }

                }


                InspectionDetailsAdapter inspectionDetailsAdapter = new InspectionDetailsAdapter(getApplicationContext(), arrayList, InspectionDetailsActivity.this);
                listView.setAdapter(inspectionDetailsAdapter);

            }

            @Override
            public void onServiceStatusFailed(String serviceName, String response) {
                kProgressHUD.dismiss();
                String message = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new DroidDialog.Builder(InspectionDetailsActivity.this)
                        .icon(R.drawable.info_icon)
                        .title(getString(R.string.error))
                        .content(message)
                        .cancelable(true, true)
                        .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                            @Override
                            public void onPositive(Dialog droidDialog) {
                                droidDialog.dismiss();
                            }
                        })
                        .color(ContextCompat.getColor(InspectionDetailsActivity.this, R.color.app_color), ContextCompat.getColor(InspectionDetailsActivity.this, R.color.white),
                                ContextCompat.getColor(InspectionDetailsActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(InspectionDetailsActivity.this)
                        .icon(R.drawable.info_icon)
                        .title(getString(R.string.error))
                        .content(getString(R.string.unable_process))
                        .cancelable(true, true)
                        .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                            @Override
                            public void onPositive(Dialog droidDialog) {
                                droidDialog.dismiss();
                            }
                        })
                        .color(ContextCompat.getColor(InspectionDetailsActivity.this, R.color.app_color), ContextCompat.getColor(InspectionDetailsActivity.this, R.color.white),
                                ContextCompat.getColor(InspectionDetailsActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();
            }
        }, "Get Levels");
    }

    public static void upload(HashMap<String, String> hashMap1) {
        hashMap.put("HouseStatus", hashMap1.get("HouseStatus"));
        hashMap.put("HouseStatusCode", hashMap1.get("HouseStatusCode"));
        Intent intent = new Intent(context, UploadPhotoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInspectionDetails();
    }
}
