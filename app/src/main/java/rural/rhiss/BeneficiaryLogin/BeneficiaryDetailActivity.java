package rural.rhiss.BeneficiaryLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import rural.rhiss.Inspection.InspectionDetailsActivity;
import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Util.TextViewCustom;

public class BeneficiaryDetailActivity extends AppCompatActivity {

    private TextViewCustom headTextViewCustom, genderTextViewCustom, emailTextViewCustom, mobileTextViewCustom,
            stateNameTextViewCustom, districtNameTextViewCustom, blockNameTextViewCustom, gpNameTextViewCustom, villageNameTextViewCustom;
    private Button detailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_detail);

        headTextViewCustom = (TextViewCustom) findViewById(R.id.headText);
        genderTextViewCustom = (TextViewCustom) findViewById(R.id.textViewGender);
        emailTextViewCustom = (TextViewCustom) findViewById(R.id.textViewEmail);
        mobileTextViewCustom = (TextViewCustom) findViewById(R.id.textViewMobile);
        stateNameTextViewCustom = (TextViewCustom) findViewById(R.id.textViewStateName);
        districtNameTextViewCustom = (TextViewCustom) findViewById(R.id.textViewDistrictName);
        blockNameTextViewCustom = (TextViewCustom) findViewById(R.id.textViewBlockName);
        gpNameTextViewCustom = (TextViewCustom) findViewById(R.id.textViewGPName);
        villageNameTextViewCustom = (TextViewCustom) findViewById(R.id.textViewVillageName);
        detailButton = (Button) findViewById(R.id.buttonDetails);

        HashMap<String, String> hashMap = RHISS.getBeneficiaryDetailObject().getHashMap();

        headTextViewCustom.setText(hashMap.get("Name") + "/ " + hashMap.get("Reg_code"));
        genderTextViewCustom.setText(": " + hashMap.get("Gender"));
        emailTextViewCustom.setText(": " + hashMap.get("Email"));
        mobileTextViewCustom.setText(": " + hashMap.get("MobileNo"));
        stateNameTextViewCustom.setText(": " + hashMap.get("State_Name") + "/ " + hashMap.get("LGD_State_Code"));
        districtNameTextViewCustom.setText(": " + hashMap.get("District_Name") + "/ " + hashMap.get("LGD_District_Code"));
        blockNameTextViewCustom.setText(": " + hashMap.get("Block_Name") + "/ " + hashMap.get("LGD_Block_Code"));
        gpNameTextViewCustom.setText(": " + hashMap.get("GP_Name") + "/ " + hashMap.get("LGD_GP_Code"));
        villageNameTextViewCustom.setText(": " + hashMap.get("Village_Name") + "/ " + hashMap.get("LGD_Village_code"));

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InspectionDetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}
