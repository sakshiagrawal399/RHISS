package rural.rhiss.PLI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;

import java.util.ArrayList;
import java.util.HashMap;

import rural.rhiss.BeneficiaryLogin.BeneficiaryDetailActivity;
import rural.rhiss.R;
import rural.rhiss.RHISS;

public class BeneficiaryListActivity extends AppCompatActivity {

    private ListView listView;
    private EditText searchEditText;
    private ArrayList<HashMap<String, String>> beneficiaryTempArrayList;
    private ArrayList<HashMap<String, String>> beneficiaryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_list);
        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.editTextSearch);
        beneficiaryArrayList = RHISS.getBeneficiaryObject().getArrayList();
        populateBeneficiary();
        searchEvent();

    }

    private void searchEvent() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = searchEditText.getText().toString();
                try {
                    beneficiaryTempArrayList = new ArrayList<>(beneficiaryArrayList);
                    if (searchText.length() == 0) {
                        if (beneficiaryTempArrayList.size() == 0) {
                            listView.setVisibility(View.INVISIBLE);

                        } else {
                            listView.setVisibility(View.VISIBLE);

                            BeneficiaryListAdapter beneficiaryListAdapter = new BeneficiaryListAdapter(getApplicationContext(), beneficiaryTempArrayList, BeneficiaryListActivity.this);
                            listView.setAdapter(beneficiaryListAdapter);

                        }

                    } else {
                        beneficiaryTempArrayList.clear();
                        beneficiaryTempArrayList = filter(searchText);
                        if (beneficiaryTempArrayList.size() == 0) {
                            listView.setVisibility(View.INVISIBLE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            BeneficiaryListAdapter beneficiaryListAdapter = new BeneficiaryListAdapter(getApplicationContext(), beneficiaryTempArrayList, BeneficiaryListActivity.this);
                            listView.setAdapter(beneficiaryListAdapter);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public ArrayList<HashMap<String, String>> filter(String searchText) {
        for (HashMap<String, String> hashMap : beneficiaryArrayList) {
            if (hashMap.get("Name").contains(searchText)) {
                beneficiaryTempArrayList.add(hashMap);
            } else if (hashMap.get("Name").toLowerCase().contains(searchText)) {
                beneficiaryTempArrayList.add(hashMap);
            }
        }
        return beneficiaryTempArrayList;
    }


    private void populateBeneficiary() {
        BeneficiaryListAdapter beneficiaryListAdapter = new BeneficiaryListAdapter(getApplicationContext(), beneficiaryArrayList, BeneficiaryListActivity.this);
        listView.setAdapter(beneficiaryListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> hashMap = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                RHISS.getBeneficiaryDetailObject().setHashMap(hashMap);
                Intent intent = new Intent(getApplicationContext(), BeneficiaryDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new DroidDialog.Builder(BeneficiaryListActivity.this)
                .icon(R.drawable.info_icon)
                .title(getString(R.string.exit_scre))
                .content(getString(R.string.exit_screen))
                .cancelable(true, true)
                .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                    @Override
                    public void onPositive(Dialog droidDialog) {
                        finish();
                    }
                })

                .negativeButton("CANCEL", new DroidDialog.onNegativeListener() {
                    @Override
                    public void onNegative(Dialog droidDialog) {
                        droidDialog.dismiss();
                    }
                })
                .color(ContextCompat.getColor(BeneficiaryListActivity.this, R.color.app_color), ContextCompat.getColor(BeneficiaryListActivity.this, R.color.white),
                        ContextCompat.getColor(BeneficiaryListActivity.this, R.color.colorPrimary))
                .animation(AnimUtils.AnimFadeInOut)
                .show();
    }

}
