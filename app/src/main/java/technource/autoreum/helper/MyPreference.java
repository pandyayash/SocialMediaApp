package technource.autoreum.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by technource on 17/7/17.
 */

public class MyPreference {

    private SharedPreferences preferences;
    Context context;
    public MyPreference(Context context) {
        this.context =context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveStringReponse(String key, String res) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, res);
        editor.commit();
    }

    public void removeStringReponse() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getStringReponse(String key){
        return preferences.getString(key, "");
    }
    public void removeParticularStringReponse(String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(value);
        editor.commit();
    }
    public void saveBooleanReponse(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void removeBooleanReponse() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    public void SaveArray(String key,String[] value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key + "_size", value.length);
        for(int i=0;i<value.length;i++)
            editor.putString(key + "_" + i, value[i]);
        editor.commit();
    }
    public String[] getArray(String key)
    {
        int size = preferences.getInt(key + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = preferences.getString(key + "_" + i, null);
        return array;
    }

    public boolean getBooleanReponse(String key){
        return preferences.getBoolean(key, false);
    }

    public void saveIntegerReponse(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void removeIntegerReponse() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public int getIntegerReponse(String key){
        return preferences.getInt(key, 0);
    }

}
