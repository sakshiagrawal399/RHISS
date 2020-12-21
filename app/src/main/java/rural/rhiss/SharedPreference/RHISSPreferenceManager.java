package rural.rhiss.SharedPreference;

import android.content.Context;

/**
 * Created by acer on 9/11/2017.
 */

public class RHISSPreferenceManager extends AbstractPreferenceManager {

    private static final int VERSION = 1;
    private static final String PREF_NAME = "THE RHISS Pref";


    String accessTokenSharedPreference = "accessTokenSharedPreference";

    private Context mContext;

    public RHISSPreferenceManager(Context ctx) {
        super(ctx, PREF_NAME, VERSION);
        mContext = ctx;
    }

    public String getAccessTokenSharedPreference() {
        return readString(accessTokenSharedPreference, "");
    }

    public void setAccessTokenSharedPreference(String accessToken) {
        saveString(accessTokenSharedPreference, accessToken);
    }


    @Override
    public boolean clearPreferences() {
        return super.clearPreferences();
    }


}
