package rural.rhiss.Object;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by acer on 12/14/2017.
 */

public class BeneficiaryObject {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context mContext;

    public BeneficiaryObject(Context ctx) {
        super();
        mContext = ctx;
    }

    public ArrayList<HashMap<String, String>> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<HashMap<String, String>> arrayList) {
        this.arrayList = arrayList;
    }
}
