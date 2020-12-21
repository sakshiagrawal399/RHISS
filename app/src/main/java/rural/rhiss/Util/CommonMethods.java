package rural.rhiss.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class CommonMethods {

    public static String key = "8080808080808080";
    public static String iv = "8080808080808080";
    public static Double longitude;
    public static Double latitude;
    public static Double accuracy;

    public static String getApplicationVersionName(Context context) {
        String versionNumber = "";

        if (versionNumber.length() == 0) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                versionNumber = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException ex) {

            } catch (Exception e) {

            }
        }
        return versionNumber;
    }
}
