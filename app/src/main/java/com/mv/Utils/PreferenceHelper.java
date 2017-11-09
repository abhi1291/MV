package com.mv.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by acer on 8/9/2017.
 */

public class PreferenceHelper {
    private static final String PREFER_NAME = "MV";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String TEMPLATENAME = "templatename";
    public static final String TEMPLATEID = "templateid";
    public static final String COMMUNITYID = "communityid";
    public static final String TOKEN = "Token";
    public static final String CONTENTISSYNCHED = "CONTENTISSYNCHED";

    public static final String UserData = "UserData";
    public static final String UserRole = "UserRole";
    public static final String AccessToken = "AccessToken";
    public static final String InstanceUrl = "InstanceUrl";
    public static final String SalesforceUserId = "SalesforceUserId";
    public static final String SalesforceUsername = "SalesforceUsername";
    public static final String SalesforcePassword = "SalesforcePassword";

    public PreferenceHelper(Context cntx) {
        this.context = cntx;
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void insertString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void insertInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void insertBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, true);
    }

    public void clearPrefrences() {
        editor.clear();
        editor.commit();
    }

}
