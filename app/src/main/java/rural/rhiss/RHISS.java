package rural.rhiss;

import android.app.Application;
import android.content.Intent;

import rural.rhiss.Object.BeneficiaryDetailObject;
import rural.rhiss.Object.BeneficiaryObject;
import rural.rhiss.Object.LGDCodeObject;
import rural.rhiss.SharedPreference.RHISSPreferenceManager;
import rural.rhiss.Util.GlobalLocationService;

/**
 * Created by acer on 12/14/2017.
 */

public class RHISS extends Application {
    private static RHISS instance = null;

    private static RHISSPreferenceManager preferenceManager;
    private static BeneficiaryObject beneficiaryObject;
    private static LGDCodeObject lgdCodeObject;
    private static BeneficiaryDetailObject beneficiaryDetailObject;

    public synchronized static RHISS getInstance() {
        return instance;
    }

    public static RHISSPreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public static BeneficiaryObject getBeneficiaryObject() {
        return beneficiaryObject;
    }

    public static LGDCodeObject getLgdCodeObject() {
        return lgdCodeObject;
    }

    public static BeneficiaryDetailObject getBeneficiaryDetailObject() {
        return beneficiaryDetailObject;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        startService(new Intent(instance, GlobalLocationService.class));

        preferenceManager = new RHISSPreferenceManager(getApplicationContext());
        beneficiaryObject = new BeneficiaryObject(getApplicationContext());
        lgdCodeObject = new LGDCodeObject(getApplicationContext());
        beneficiaryDetailObject = new BeneficiaryDetailObject(getApplicationContext());
    }
}
