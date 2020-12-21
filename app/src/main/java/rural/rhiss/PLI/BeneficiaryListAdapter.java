package rural.rhiss.PLI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import rural.rhiss.R;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.TextViewCustom;

/**
 * Created by acer on 12/14/2017.
 */

public class BeneficiaryListAdapter extends ArrayAdapter<HashMap<String, String>> implements Constants {
    Context context;
    String schemeName;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> hashMap;
    Activity activity;

    public BeneficiaryListAdapter(Context context, ArrayList<HashMap<String, String>> list, Activity activity) {
        super(context, R.layout.list_beneficiary, list);
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_beneficiary, parent, false);
        }
        // Lookup view for data population
        hashMap = (HashMap<String, String>) getItem(position);

        TextViewCustom beneficiaryIdTextView = (TextViewCustom) convertView.findViewById(R.id.textViewId);
        TextViewCustom beneficiaryMobTextView = (TextViewCustom) convertView.findViewById(R.id.textViewMob);

        beneficiaryIdTextView.setText(hashMap.get("Reg_code") + "/ " + hashMap.get("Name"));
        beneficiaryMobTextView.setText(hashMap.get("MobileNo"));

        return convertView;
    }
}
