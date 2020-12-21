package rural.rhiss.PLI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Util.CommonMethods;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.WebServiceCall;

public class PLILoginActivity extends AppCompatActivity implements Constants, TextWatcher {

    private static String edit_text_sha;
    private static String final_edit_text_sha;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, buttonReset;
    private KProgressHUD kProgressHUD;
    String AB;
    SecureRandom rnd;
    String random_no;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pli_login);
        usernameEditText = (EditText) findViewById(R.id.editTextUserName);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        buttonReset = findViewById(R.id.buttonReset);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordEditText.setText("");
                usernameEditText.setText("");
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        rnd = new SecureRandom();
        random_no(8);

        passwordEditText.addTextChangedListener(this);

    }

    public void random_no(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        random_no = sb.toString();
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);

            }
            edit_text_sha = hexString.toString();

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String sha256_final(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);

            }
            final_edit_text_sha = hexString.toString();

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(
            CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() >= 6) {
            updatePasswordStrengthView(s.toString());
            sha256(passwordEditText.getText().toString());

            sha256_final(edit_text_sha + "ETTs8uxu");
        }

    }

    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView strengthView = (TextView) findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }

    private void login() {
        kProgressHUD = KProgressHUD.create(PLILoginActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        String userName = usernameEditText.getText().toString().toUpperCase();
        String password = passwordEditText.getText().toString();
        if (userName.equals("")) {
            kProgressHUD.dismiss();
            usernameEditText.requestFocus();
            usernameEditText.setError(getString(R.string.enter_username));
        } else if (password.equals("")) {
            kProgressHUD.dismiss();
            passwordEditText.requestFocus();
            passwordEditText.setError(getString(R.string.enter_password));
        } else {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("PLICode", userName);
                jsonObject.put("Password", final_edit_text_sha);
                jsonObject.put("Randomenumber", "ETTs8uxu");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                jsonObject.put("imeino", telephonyManager.getDeviceId());
                jsonObject.put("version", CommonMethods.getApplicationVersionName(getApplicationContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            WebServiceCall.getWebServiceCallInstance(APP_URL + PLI_LOGIN_URL1).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
                @Override
                public void onServiceCallSucceed(String serviceName, String response) {
                    kProgressHUD.dismiss();
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                    HashMap<String, String> hashMap;
                    try {
                        JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                        JSONObject responseJsonObject = new JSONObject(response);
                        String accessToken = responseJsonObject.getString("accessToken");
                        RHISS.getPreferenceManager().setAccessTokenSharedPreference(accessToken);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hashMap = new HashMap<>();
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
                            hashMap.put("Village_Name", jsonObject.getString("Village_Name"));
                            hashMap.put("Gender", jsonObject.getString("Gender"));
                            hashMap.put("Email", jsonObject.getString("Email"));
                            arrayList.add(hashMap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), BeneficiaryListActivity.class);
                    RHISS.getBeneficiaryObject().setArrayList(arrayList);
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
                    new DroidDialog.Builder(PLILoginActivity.this)
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
                            .color(ContextCompat.getColor(PLILoginActivity.this, R.color.app_color), ContextCompat.getColor(PLILoginActivity.this, R.color.white),
                                    ContextCompat.getColor(PLILoginActivity.this, R.color.colorPrimary))
                            .animation(AnimUtils.AnimFadeInOut)
                            .show();

                }

                @Override
                public void onServiceCallFailed(String serviceName, Exception e) {
                    kProgressHUD.dismiss();
                    new DroidDialog.Builder(PLILoginActivity.this)
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
                            .color(ContextCompat.getColor(PLILoginActivity.this, R.color.app_color), ContextCompat.getColor(PLILoginActivity.this, R.color.white),
                                    ContextCompat.getColor(PLILoginActivity.this, R.color.colorPrimary))
                            .animation(AnimUtils.AnimFadeInOut)
                            .show();
                }
            }, "PLI Login");
        }
    }
}
