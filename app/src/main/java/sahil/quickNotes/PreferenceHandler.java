package sahil.quickNotes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by SAHIL SINGLA on 11-06-2017.
 * Activity to store static data for reference
 */

public class PreferenceHandler {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    Context context;
    private final String prefName="ReadyToGoPreferences";
    public static final String FirstLoginPrefs="FirstTime";
    public static final String name="Name";
    public static final String loginStatus="loginStatus";
    public static final String pin="PIN";
    public PreferenceHandler(Context c)
    {
        context=c;
        preferences=c.getSharedPreferences(prefName, c.MODE_PRIVATE);
        prefEditor=preferences.edit();
    }
    public void setFirstLogin()
    {
        prefEditor.putBoolean(FirstLoginPrefs,false);
        prefEditor.commit();
    }
    public boolean isFirstLogin()
    {
        return preferences.getBoolean(FirstLoginPrefs,true);
    }
    public void setPreference(String s, String value)
    {
        prefEditor.putString(s,value);
        prefEditor.commit();
    }
    public String getPreferences(String prefName)
    {
        return preferences.getString(prefName,null);
    }
}
