package rural.rhiss.Inspection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import rural.rhiss.R;
import rural.rhiss.Upload.UploadPhotoActivity;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.TextViewCustom;

/**
 * Created by acer on 12/21/2017.
 */

public class InspectionDetailsAdapter extends ArrayAdapter<HashMap<String, String>> implements Constants {
    Context context;
    String schemeName;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> hashMap;
    Activity activity;

    public InspectionDetailsAdapter(Context context, ArrayList<HashMap<String, String>> list, Activity activity) {
        super(context, R.layout.list_inspection, list);
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_inspection, parent, false);
        }
        // Lookup view for data population
        hashMap = (HashMap<String, String>) getItem(position);


        TextViewCustom houseStatusCodeTextView = (TextViewCustom) convertView.findViewById(R.id.textViewHouseStatus);
        TextViewCustom houseStatusNameTextView = (TextViewCustom) convertView.findViewById(R.id.textViewHouseStatus1);
        final Button button = (Button) convertView.findViewById(R.id.button);

        houseStatusCodeTextView.setText(hashMap.get("HouseStatus") + " (" + hashMap.get("HouseStatusCode") + ")");
        houseStatusNameTextView.setText(hashMap.get("Response"));
        String approved = hashMap.get("Approved");
        if (approved.equals("1") || approved.equals("2")) {
            button.setText("Upload Done");
        } else if (approved.equals("0") || approved.equals("3")) {
            if (hashMap.get("isUpload").equals("true")) {
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.INVISIBLE);
            }
            button.setText("Upload");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap1 = (HashMap<String, String>) getItem(position);
                String text= (String) button.getText();
                if (text.equals("Upload Done")) {

                } else {

                    InspectionDetailsActivity.upload(hashMap1);
                }
            }
        });

        return convertView;
    }
}
