package rural.rhiss.BeneficiaryLogin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rural.rhiss.PLI.BeneficiaryListActivity;
import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.WebServiceCall;

public class BeneficiaryLoginActivity extends AppCompatActivity implements Constants {

    private EditText mobileNumberEditText, PLICodeEditText;
    private Button loginButton,buttonReset;
    KProgressHUD kProgressHUD;
    Dialog dialog;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_login);

        mobileNumberEditText = (EditText) findViewById(R.id.editTextMobileNumber);
        PLICodeEditText = (EditText) findViewById(R.id.editTextPLICode);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        buttonReset=findViewById(R.id.buttonReset);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP(BENEFICIARY_LOGIN_URL);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumberEditText.setText("");
                PLICodeEditText.setText("");
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

    private void sendOTP(String url) {
        mobileNumber = mobileNumberEditText.getText().toString();
        String pliCode = PLICodeEditText.getText().toString();
        if (mobileNumber != null && !mobileNumber.isEmpty() && !mobileNumber.equals("null")) {
            if (pliCode != null && !pliCode.isEmpty() && !pliCode.equals("null")) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("MobileNo", mobileNumber);
                    jsonObject.put("PLICode", pliCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                kProgressHUD = KProgressHUD.create(BeneficiaryLoginActivity.this);
                kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setDetailsLabel(getString(R.string.loading_data))
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();

                WebServiceCall.getWebServiceCallInstance(APP_URL + url).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
                    @Override
                    public void onServiceCallSucceed(String serviceName, String response) {
                        kProgressHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            RHISS.getPreferenceManager().setAccessTokenSharedPreference(jsonObject.getString("accessToken"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog = new Dialog(BeneficiaryLoginActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.otp_popup);
                        final Button buttonTimer = (Button) dialog.findViewById(R.id.buttonTimer);
                        final Button verifyButton = (Button) dialog.findViewById(R.id.buttonLogin);
                        final EditText otpEditText = (EditText) dialog.findViewById(R.id.editTextOTP);

                        final CountDownTimer timer = new CountDownTimer(600000, 500) {

                            public void onTick(long millisUntilFinished) {
                                long seconds = millisUntilFinished / 1000;
                                buttonTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                            }

                            public void onFinish() {
                                //dialog.dismiss();
                                buttonTimer.setText("Resend");
                            }
                        }.start();
                        verifyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                timer.cancel();
                                String otp = otpEditText.getText().toString();
                                if (otp.length() == 4) {
                                    verifyOTP(otpEditText.getText().toString());
                                    dialog.dismiss();
                                    // AwaasApp.getBeneficiaryIdListObject().setBeneIdArrayList(beneIdArrayList);
                                        /*Intent intent = new Intent(getApplicationContext(), BeneficiaryIdListActivity.class);
                                        startActivity(intent);*/
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.valid_otp), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        buttonTimer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (buttonTimer.getText().equals("Resend")) {
                                    dialog.dismiss();
                                    sendOTP(RESEND_OTP);
                                } else {

                                }
                            }
                        });

                        dialog.show();

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
                        new DroidDialog.Builder(BeneficiaryLoginActivity.this)
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
                                .color(ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.app_color), ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.white),
                                        ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.colorPrimary))
                                .animation(AnimUtils.AnimFadeInOut)
                                .show();

                    }

                    @Override
                    public void onServiceCallFailed(String serviceName, Exception e) {
                        kProgressHUD.dismiss();
                        new DroidDialog.Builder(BeneficiaryLoginActivity.this)
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
                                .color(ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.app_color), ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.white),
                                        ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.colorPrimary))
                                .animation(AnimUtils.AnimFadeInOut)
                                .show();
                    }
                }, "Beneficiary Login");
            } else {

            }
        } else {

        }

    }

    private void verifyOTP(String otp) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("MobileNo", mobileNumber);
            jsonObject.put("OTP", otp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        kProgressHUD = KProgressHUD.create(BeneficiaryLoginActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        WebServiceCall.getWebServiceCallInstance(APP_URL + VERIFY_OTP).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
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
                        hashMap.put("Name", jsonObject.getString("Name"));
                        hashMap.put("MobileNo", jsonObject.getString("MobileNo"));
                        hashMap.put("PLI_Code", jsonObject.getString("PLI_Code"));
                        hashMap.put("LGD_State_Code", jsonObject.getString("LGD_State_Code"));
                        hashMap.put("State_Name", jsonObject.getString("State_Name"));
                        hashMap.put("LGD_District_Code", jsonObject.getString("LGD_District_Code"));
                        hashMap.put("District_Name", jsonObject.getString("District_Name"));
                        hashMap.put("LGD_Block_Code", jsonObject.getString("LGD_Block_Code"));
                        hashMap.put("Block_Name", jsonObject.getString("Block_Name"));
                        hashMap.put("LGD_GP_Code", jsonObject.getString("LGD_GP_Code"));
                        hashMap.put("GP_Name", jsonObject.getString("GP_Name"));
                        hashMap.put("LGD_Village_code", jsonObject.getString("LGD_Village_code"));
                        hashMap.put("Gender", jsonObject.getString("Gender"));
                        hashMap.put("Email", jsonObject.getString("Email"));
                        arrayList.add(hashMap);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                RHISS.getBeneficiaryObject().setArrayList(arrayList);
                Intent intent = new Intent(getApplicationContext(), BeneficiaryListActivity.class);
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
                new DroidDialog.Builder(BeneficiaryLoginActivity.this)
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
                        .color(ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.app_color), ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.white),
                                ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();


            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                kProgressHUD.dismiss();
                new DroidDialog.Builder(BeneficiaryLoginActivity.this)
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
                        .color(ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.app_color), ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.white),
                                ContextCompat.getColor(BeneficiaryLoginActivity.this, R.color.colorPrimary))
                        .animation(AnimUtils.AnimFadeInOut)
                        .show();

            }
        }, "Verify OTP");
    }

}
