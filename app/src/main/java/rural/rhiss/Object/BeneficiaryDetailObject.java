package rural.rhiss.Object;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by acer on 12/20/2017.
 */

public class BeneficiaryDetailObject {

    private HashMap<String, String> hashMap;
    private Context mContext;

    public BeneficiaryDetailObject(Context ctx) {
        super();
        mContext = ctx;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }
}
