package rural.rhiss.LGDCode;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Util.TextViewCustom;

public class DisplayLGDCodeActivity extends AppCompatActivity {

    TextViewCustom stateNameTextViewCustom, districtNameTextViewCustom, blockNameTextViewCustom,
            panchayatNameTextViewCustom, villageNameTextViewCustom;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lgd_code);


        stateNameTextViewCustom = (TextViewCustom) findViewById(R.id.textView1);
        districtNameTextViewCustom = (TextViewCustom) findViewById(R.id.textView2);
        blockNameTextViewCustom = (TextViewCustom) findViewById(R.id.textView3);
        panchayatNameTextViewCustom = (TextViewCustom) findViewById(R.id.textView4);
        villageNameTextViewCustom = (TextViewCustom) findViewById(R.id.textView5);
        displayLGDCodes();
    }

    private void displayLGDCodes() {
        try {
            JSONArray jsonArray = new JSONObject(RHISS.getLgdCodeObject().getString()).getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                stateNameTextViewCustom.setText(": " + jsonObject.getString("State_Name") + "/ " + jsonObject.getString("LGD_State_Code"));
                districtNameTextViewCustom.setText(": " + jsonObject.getString("District_Name") + "/ " + jsonObject.getString("LGD_District_Code"));
                blockNameTextViewCustom.setText(": " + jsonObject.getString("Block_Name") + "/ " + jsonObject.getString("LGD_Block_Code"));
                panchayatNameTextViewCustom.setText(": " + jsonObject.getString("GP_Name") + "/ " + jsonObject.getString("LGD_GP_Code"));
                villageNameTextViewCustom.setText(": " + jsonObject.getString("Village_Name") + "/ " + jsonObject.getString("LGD_Village_Code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
