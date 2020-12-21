package rural.rhiss.LGDCode;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.WebServiceCall;

public class GetLGDCodeActivity extends AppCompatActivity implements Constants {

    private MaterialBetterSpinner stateSpinner, districtSpinner, blockSpinner, panchayatSpinner, villageSpinner;
    private String stateLGDCode, districtLGDCode, blockLGDCode, panchayatLGDCode, villageLGDCode;
    private String stateName, districtName, blockName, panchayatName, villageName;
    private ArrayList<String> stateArrayList = new ArrayList<>(), districtArrayList = new ArrayList<>(), blockArrayList = new ArrayList<>(), panchayatArrayList = new ArrayList<>(), villageArrayList = new ArrayList<>();
    private KProgressHUD kProgressHUD;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_lgd_code);

        stateSpinner = (MaterialBetterSpinner) findViewById(R.id.spinnerState);
        districtSpinner = (MaterialBetterSpinner) findViewById(R.id.spinnerDistrict);
        blockSpinner = (MaterialBetterSpinner) findViewById(R.id.spinnerBlock);
        panchayatSpinner = (MaterialBetterSpinner) findViewById(R.id.spinnerPanchayat);
        villageSpinner = (MaterialBetterSpinner) findViewById(R.id.spinnerVillage);
        searchButton = (Button) findViewById(R.id.buttonSearch);

        stateSpinner.setFocusableInTouchMode(false);
        districtSpinner.setFocusableInTouchMode(false);
        blockSpinner.setFocusableInTouchMode(false);
        panchayatSpinner.setFocusableInTouchMode(false);
        villageSpinner.setFocusableInTouchMode(false);

        populateState();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void search() {
        if (stateLGDCode != null && !stateLGDCode.isEmpty() && !stateLGDCode.equals("null")) {
            if (districtLGDCode != null && !districtLGDCode.isEmpty() && !districtLGDCode.equals("null")) {
                if (blockLGDCode != null && !blockLGDCode.isEmpty() && !blockLGDCode.equals("null")) {
                    if (panchayatLGDCode != null && !panchayatLGDCode.isEmpty() && !panchayatLGDCode.equals("null")) {
                        if (villageLGDCode != null && !villageLGDCode.isEmpty() && !villageLGDCode.equals("null")) {
                            kProgressHUD = KProgressHUD.create(GetLGDCodeActivity.this);
                            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                    .setDetailsLabel(getString(R.string.loading_data))
                                    .setCancellable(false)
                                    .setAnimationSpeed(2)
                                    .setDimAmount(0.5f)
                                    .show();
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("SC", stateLGDCode);
                                jsonObject.put("DC", districtLGDCode);
                                jsonObject.put("BC", blockLGDCode);
                                jsonObject.put("GC", panchayatLGDCode);
                                jsonObject.put("VC", villageLGDCode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            WebServiceCall.getWebServiceCallInstance(APP_URL + GET_LGD_CODE_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
                                @Override
                                public void onServiceCallSucceed(String serviceName, String response) {
                                    kProgressHUD.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), DisplayLGDCodeActivity.class);
                                    RHISS.getLgdCodeObject().setString(response);
                                    startActivity(intent);
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
                                    new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                                            .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                                    ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                                            .animation(AnimUtils.AnimFadeInOut)
                                            .show();
                                }

                                @Override
                                public void onServiceCallFailed(String serviceName, Exception e) {
                                    kProgressHUD.dismiss();
                                    new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                                            .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                                    ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                                            .animation(AnimUtils.AnimFadeInOut)
                                            .show();
                                }
                            }, "Get LGD Code");
                        } else {
                            new DroidDialog.Builder(GetLGDCodeActivity.this)
                                    .icon(R.drawable.info_icon)
                                    .title(getString(R.string.error))
                                    .content(getString(R.string.select_village))
                                    .cancelable(true, true)
                                    .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                        @Override
                                        public void onPositive(Dialog droidDialog) {
                                            droidDialog.dismiss();
                                        }
                                    })
                                    .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                            ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                                    .animation(AnimUtils.AnimFadeInOut)
                                    .show();
                        }
                    } else {
                        new DroidDialog.Builder(GetLGDCodeActivity.this)
                                .icon(R.drawable.info_icon)
                                .title(getString(R.string.error))
                                .content(getString(R.string.select_panchayat))
                                .cancelable(true, true)
                                .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                    @Override
                                    public void onPositive(Dialog droidDialog) {
                                        droidDialog.dismiss();
                                    }
                                })
                                .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                        ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                                .animation(AnimUtils.AnimFadeInOut)
                                .show();
                    }
                } else {
                    new DroidDialog.Builder(GetLGDCodeActivity.this)
                            .icon(R.drawable.info_icon)
                            .title(getString(R.string.error))
                            .content(getString(R.string.select_block))
                            .cancelable(true, true)
                            .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                @Override
                                public void onPositive(Dialog droidDialog) {
                                    droidDialog.dismiss();
                                }
                            })
                            .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                    ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                            .animation(AnimUtils.AnimFadeInOut)
                            .show();
                }
            } else {
                new DroidDialog.Builder(GetLGDCodeActivity.this)
                        .icon(R.drawable.info_icon)
                        .title(getString(R.string.error))
                        .content(getString(R.string.select_district))
                        .cancelable(true, true)
                        .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                            @Override
                            public void onPositive(Dialog droidDialog) {
                                droidDialog.dismiss();
                            }
                        })
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();
            }
        } else {
            new DroidDialog.Builder(GetLGDCodeActivity.this)
                    .icon(R.drawable.info_icon)
                    .title(getString(R.string.error))
                    .content(getString(R.string.select_state))
                    .cancelable(true, true)
                    .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                        @Override
                        public void onPositive(Dialog droidDialog) {
                            droidDialog.dismiss();
                        }
                    })
                    .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                            ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                    .animation(AnimUtils.AnimFadeInOut)
                    .show();
        }
    }


    private void populateState() {
        clearList(0);
        kProgressHUD = KProgressHUD.create(GetLGDCodeActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        JSONObject jsonObject = new JSONObject();
        WebServiceCall.getWebServiceCallInstance(APP_URL + GET_STATES_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                kProgressHUD.dismiss();
                final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject stateJsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("stateName", stateJsonObject.getString("State_Name"));
                        hashMap.put("stateLGDCode", stateJsonObject.getString("LGD_State_Code"));
                        stateArrayList.add(stateJsonObject.getString("State_Name"));
                        arrayList.add(hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(GetLGDCodeActivity.this, R.layout.spinner_textview, stateArrayList);
                stateSpinner.setAdapter(stateAdapter);

                stateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //clearList(0);
                        for (HashMap<String, String> hashMap : arrayList) {
                            String stateName = stateSpinner.getText().toString();
                            if (hashMap.get("stateName").equals(stateName)) {
                                stateLGDCode = hashMap.get("stateLGDCode");
                                populateDistrict();
                            }
                        }
                    }
                });

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
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();
            }
        }, "Get States");
    }

    private void populateDistrict() {
        clearList(1);
        kProgressHUD = KProgressHUD.create(GetLGDCodeActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SC", stateLGDCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + GET_DISTRICTS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                kProgressHUD.dismiss();
                final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject districtJsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("districtName", districtJsonObject.getString("District_Name"));
                        hashMap.put("districtLGDCode", districtJsonObject.getString("LGD_District_Code"));
                        districtArrayList.add(districtJsonObject.getString("District_Name"));
                        arrayList.add(hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(GetLGDCodeActivity.this, R.layout.spinner_textview, districtArrayList);
                districtSpinner.setAdapter(districtAdapter);

                districtSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //clearList(0);
                        for (HashMap<String, String> hashMap : arrayList) {
                            String districtName = districtSpinner.getText().toString();
                            if (hashMap.get("districtName").equals(districtName)) {
                                districtLGDCode = hashMap.get("districtLGDCode");
                                populateBlock();
                                // break;
                            }
                        }
                    }
                });

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
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }
        }, "Get District");
    }

    private void populateBlock() {
        clearList(2);
        kProgressHUD = KProgressHUD.create(GetLGDCodeActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DC", districtLGDCode);
            jsonObject.put("SC", stateLGDCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + GET_BLOCKS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                kProgressHUD.dismiss();
                final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject districtJsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("blockName", districtJsonObject.getString("Block_Name"));
                        hashMap.put("blockLGDCode", districtJsonObject.getString("LGD_Block_Code"));
                        blockArrayList.add(districtJsonObject.getString("Block_Name"));
                        arrayList.add(hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> blockAdapter = new ArrayAdapter<String>(GetLGDCodeActivity.this, R.layout.spinner_textview, blockArrayList);
                blockSpinner.setAdapter(blockAdapter);

                blockSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //clearList(0);
                        for (HashMap<String, String> hashMap : arrayList) {
                            String blockName = blockSpinner.getText().toString();
                            if (hashMap.get("blockName").equals(blockName)) {
                                blockLGDCode = hashMap.get("blockLGDCode");
                                populatePanchayat();
                                // break;
                            }
                        }
                    }
                });

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
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();
            }
        }, "Get Block");
    }


    public void populatePanchayat() {
        clearList(3);
        kProgressHUD = KProgressHUD.create(GetLGDCodeActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DC", districtLGDCode);
            jsonObject.put("SC", stateLGDCode);
            jsonObject.put("BC", blockLGDCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + GET_PANCHAYATS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                kProgressHUD.dismiss();
                final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject districtJsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("panchayatName", districtJsonObject.getString("GP_Name"));
                        hashMap.put("panchayatLGDCode", districtJsonObject.getString("LGD_GP_Code"));
                        panchayatArrayList.add(districtJsonObject.getString("GP_Name"));
                        arrayList.add(hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> blockAdapter = new ArrayAdapter<String>(GetLGDCodeActivity.this, R.layout.spinner_textview, panchayatArrayList);
                panchayatSpinner.setAdapter(blockAdapter);

                panchayatSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //clearList(0);
                        for (HashMap<String, String> hashMap : arrayList) {
                            String blockName = panchayatSpinner.getText().toString();
                            if (hashMap.get("panchayatName").equals(blockName)) {
                                panchayatLGDCode = hashMap.get("panchayatLGDCode");
                                populateVillage();
                                // break;
                            }
                        }
                    }
                });

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
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();
            }
        }, "Get Block");
    }


    public void populateVillage() {
        clearList(4);
        kProgressHUD = KProgressHUD.create(GetLGDCodeActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DC", districtLGDCode);
            jsonObject.put("SC", stateLGDCode);
            jsonObject.put("BC", blockLGDCode);
            jsonObject.put("GC", panchayatLGDCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + GET_VILLAGES_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                kProgressHUD.dismiss();
                final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject districtJsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Village_Name", districtJsonObject.getString("Village_Name"));
                        hashMap.put("LGD_Village_Code", districtJsonObject.getString("LGD_Village_Code"));
                        villageArrayList.add(districtJsonObject.getString("Village_Name"));
                        arrayList.add(hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> blockAdapter = new ArrayAdapter<String>(GetLGDCodeActivity.this, R.layout.spinner_textview, villageArrayList);
                villageSpinner.setAdapter(blockAdapter);

                villageSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //clearList(0);
                        for (HashMap<String, String> hashMap : arrayList) {
                            String blockName = villageSpinner.getText().toString();
                            if (hashMap.get("Village_Name").equals(blockName)) {
                                villageLGDCode = hashMap.get("LGD_Village_Code");

                            }
                        }
                    }
                });

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
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(GetLGDCodeActivity.this)
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
                        .color(ContextCompat.getColor(GetLGDCodeActivity.this, R.color.app_color), ContextCompat.getColor(GetLGDCodeActivity.this, R.color.white),
                                ContextCompat.getColor(GetLGDCodeActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();
            }
        }, "Get Block");
    }


    private void clearList(int option) {

        ArrayAdapter<String> stateAdapter, districtAdapter, blockAdapter, panchayatAdapter, villageAdapter;
        switch (option) {
            case 0:
                stateName = "";
                stateLGDCode = "";
                stateArrayList.clear();
                stateSpinner.setFocusableInTouchMode(false);
                stateSpinner.setText("");
                stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateArrayList);
                stateSpinner.setAdapter(stateAdapter);

                districtName = "";
                districtLGDCode = "";
                districtArrayList.clear();
                districtSpinner.setFocusableInTouchMode(false);
                districtSpinner.setText("");
                districtAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtArrayList);
                districtSpinner.setAdapter(districtAdapter);

                blockName = "";
                blockLGDCode = "";
                blockArrayList.clear();
                blockSpinner.setFocusableInTouchMode(false);
                blockSpinner.setText("");
                blockAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, blockArrayList);
                blockSpinner.setAdapter(blockAdapter);

                panchayatName = "";
                panchayatLGDCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, panchayatArrayList);
                panchayatSpinner.setAdapter(panchayatAdapter);


                break;

            case 1:
                districtName = "";
                districtLGDCode = "";
                districtArrayList.clear();
                districtSpinner.setFocusableInTouchMode(false);
                districtSpinner.setText("");
                districtAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtArrayList);
                districtSpinner.setAdapter(districtAdapter);

                blockName = "";
                blockLGDCode = "";
                blockArrayList.clear();
                blockSpinner.setFocusableInTouchMode(false);
                blockSpinner.setText("");
                blockAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, blockArrayList);
                blockSpinner.setAdapter(blockAdapter);

                panchayatName = "";
                panchayatLGDCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, panchayatArrayList);
                panchayatSpinner.setAdapter(panchayatAdapter);

                villageName = "";
                villageLGDCode = "";
                villageArrayList.clear();
                villageSpinner.setFocusableInTouchMode(false);
                villageSpinner.setText("");
                villageAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, villageArrayList);
                villageSpinner.setAdapter(villageAdapter);
                break;

            case 2:
                blockName = "";
                blockLGDCode = "";
                blockArrayList.clear();
                blockSpinner.setFocusableInTouchMode(false);
                blockSpinner.setText("");
                blockAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, blockArrayList);
                blockSpinner.setAdapter(blockAdapter);

                panchayatName = "";
                panchayatLGDCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, panchayatArrayList);
                panchayatSpinner.setAdapter(panchayatAdapter);
                break;

            case 3:
                panchayatName = "";
                panchayatLGDCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, panchayatArrayList);
                panchayatSpinner.setAdapter(panchayatAdapter);
                break;

            case 4:
                villageName = "";
                villageLGDCode = "";
                villageArrayList.clear();
                villageSpinner.setFocusableInTouchMode(false);
                villageSpinner.setText("");
                villageAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, villageArrayList);
                villageSpinner.setAdapter(villageAdapter);
                break;
        }
    }
}
