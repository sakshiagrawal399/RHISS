package rural.rhiss.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


public abstract class AbstractPreferenceManager
{
    private static final String VERSION = "__prefs_version__";

    private final Context ctx;
    private final SharedPreferences prefs;

    public AbstractPreferenceManager(Context ctx, int version)
    {
        this(ctx, null, version);
    }

    public AbstractPreferenceManager(Context ctx, String prefsName, int version)
    {
        this.ctx = ctx.getApplicationContext();
        if(TextUtils.isEmpty(prefsName))
        {
            prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        }
        else
        {
            prefs = ctx.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        }
        // TODO uncomment if need to flush old SharedPreference with updated version code.
//        init(version);
    }

    protected Context getContext()
    {
        return ctx;
    }

    protected SharedPreferences getPreferences()
    {
        return prefs;
    }

    private void init(int newVersion)
    {
        int oldVersion = prefs.getInt(VERSION, -1);
        if(oldVersion != newVersion)
        {
            onUpgrade(prefs, oldVersion, newVersion);
            saveInt(VERSION, newVersion);
        }
    }

    protected void onUpgrade(SharedPreferences prefs, int oldVersion, int newVersion)
    {
        prefs.edit().clear().apply();
    }

    protected boolean clearPreferences()
    {
        return prefs.edit().clear().commit();
    }

    protected boolean readBoolean(int keyResId)
    {
        return prefs.getBoolean(ctx.getString(keyResId), Boolean.FALSE);
    }

    protected boolean readBoolean(int keyResId, int defValueResId)
    {
        return prefs.getBoolean(ctx.getString(keyResId), ctx.getResources().getBoolean(defValueResId));
    }

    protected boolean readBoolean(int keyResId, Boolean value)
    {
        return prefs.getBoolean(ctx.getString(keyResId), value);
    }

    protected boolean readBoolean(String key)
    {
        return prefs.getBoolean(key, Boolean.FALSE);
    }

    protected boolean readBoolean(String key, int defValueResId)
    {
        return prefs.getBoolean(key, ctx.getResources().getBoolean(defValueResId));
    }

    protected boolean readBoolean(String key, Boolean value)
    {
        return prefs.getBoolean(key, value);
    }

    protected int readInt(int keyResId)
    {
        return prefs.getInt(ctx.getString(keyResId), Integer.MIN_VALUE);
    }

    protected int readInt(int keyResId, int defValue)
    {
        return prefs.getInt(ctx.getString(keyResId), defValue);
    }

    protected int readInt(String key)
    {
        return prefs.getInt(key, Integer.MIN_VALUE);
    }

    protected int readInt(String key, int value)
    {
        return prefs.getInt(key, value);
    }

    protected String readString(int keyResId)
    {
        return prefs.getString(ctx.getString(keyResId), null);
    }

    protected String readString(int keyResId, int defValueResId)
    {
        return prefs.getString(ctx.getString(keyResId), ctx.getResources().getString(defValueResId));
    }

    protected String readString(int keyResId, String defValue)
    {
        return prefs.getString(ctx.getString(keyResId), defValue);
    }

    protected String readString(String key)
    {
        return prefs.getString(key, null);
    }

    protected String readString(String key, int defValueResId)
    {
        return prefs.getString(key, ctx.getResources().getString(defValueResId));
    }

    protected String readString(String key, String defValue)
    {
        return prefs.getString(key, defValue);
    }

    protected long readLong(int keyResId)
    {
        return prefs.getLong(ctx.getString(keyResId), Long.MIN_VALUE);
    }

    protected long readLong(int keyResId, Long defValue)
    {
        return prefs.getLong(ctx.getString(keyResId), defValue);
    }

    protected long readLong(String key)
    {
        return prefs.getLong(key, Long.MIN_VALUE);
    }

    protected long readLong(String key, Long defValue)
    {
        return prefs.getLong(key, defValue);
    }

    protected float readFloat(int keyResId)
    {
        return prefs.getFloat(ctx.getString(keyResId), Float.MIN_VALUE);
    }

    protected float readFloat(int keyResId, Float defValue)
    {
        return prefs.getFloat(ctx.getString(keyResId), defValue);
    }

    protected float readFloat(String key)
    {
        return prefs.getFloat(key, Float.MIN_VALUE);
    }

    protected float readFloat(String key, Float defValue)
    {
        return prefs.getFloat(key, defValue);
    }

    protected boolean saveBoolean(int keyResId, boolean val)
    {
        return prefs.edit().putBoolean(ctx.getResources().getString(keyResId), val).commit();
    }

    protected boolean saveBoolean(String key, boolean val)
    {
        return prefs.edit().putBoolean(key, val).commit();
    }

    protected boolean saveInt(int keyResId, int val)
    {
        return prefs.edit().putInt(ctx.getResources().getString(keyResId), val).commit();
    }

    protected boolean saveInt(String key, int val)
    {
        return prefs.edit().putInt(key, val).commit();
    }

    protected boolean saveLong(int keyResId, long val)
    {
        return prefs.edit().putLong(ctx.getResources().getString(keyResId), val).commit();
    }

    protected boolean saveLong(String key, long val)
    {
        return prefs.edit().putLong(key, val).commit();
    }

    protected boolean saveString(int keyResId, String val)
    {
        return prefs.edit().putString(ctx.getResources().getString(keyResId), val).commit();
    }

    protected boolean saveString(String key, String val)
    {
        return prefs.edit().putString(key, val).commit();
    }

    protected boolean saveFloat(int keyResId, float val)
    {
        return prefs.edit().putFloat(ctx.getResources().getString(keyResId), val).commit();
    }

    protected boolean saveFloat(String key, float val)
    {
        return prefs.edit().putFloat(key, val).commit();
    }

    protected boolean remove(String key)
    {
        return prefs.edit().remove(key).commit();
    }
}
