package com.ase.eu.travel_buddy;

import android.content.SharedPreferences;

/**
 * Created by Wolf on 3/31/2018.
 */

public class SharedPreferencesBuilder {

    SharedPreferences sharedPref;

    public SharedPreferencesBuilder() {
    }

    public SharedPreferencesBuilder(SharedPreferences sharedPreferences) {
        this.sharedPref = sharedPreferences;
    }

    public void saveInfo(String key, boolean value)
    {
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public void saveUserInfo(String userKey, String passKey, String username, String password){
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putString(userKey,username);
        editor.putString(passKey,password);
        editor.apply();
    }

    public Boolean loadInfo(String key) {
        Boolean value = this.sharedPref.getBoolean(key,false);
        return value;
    }

    public String loadUserInfo(String key){
        String value = this.sharedPref.getString(key,"");
        return value;
    }

    public void setCustomConfig() {
        SharedPreferences.Editor editor = this.sharedPref.edit();
        editor.putInt("customConfig",1);
        editor.apply();
    }

    public Boolean isCustomConfig()
    {
       return this.sharedPref.contains("customConfig");
    }
}
