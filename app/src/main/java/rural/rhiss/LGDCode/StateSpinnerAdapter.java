package rural.rhiss.LGDCode;

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
 * Created by acer on 12/15/2017.
 */

public class StateSpinnerAdapter extends ArrayAdapter<HashMap<String, String>> implements Constants {
    Context context;
    String schemeName;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> hashMap;
    Activity activity;

    public StateSpinnerAdapter(Context context, ArrayList<HashMap<String, String>> list, Activity activity) {
        super(context, R.layout.list_state, list);
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_state, parent, false);
        }
        // Lookup view for data population
        hashMap = (HashMap<String, String>) getItem(position);
        TextViewCustom stateNameTextView = (TextViewCustom) convertView.findViewById(R.id.textView);

        stateNameTextView.setText(hashMap.get("stateName"));


        return convertView;
    }
}
