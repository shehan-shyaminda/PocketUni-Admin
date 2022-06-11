package com.codelabs.pocketuni_admin.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.codelabs.pocketuni_admin.models.Admin;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    public static final String PREF_FILE = "POCKET_UNI_PREF_FILE";
    public static final String PREF_IS_SUPER_ADMIN = "POCKET_UNI_PREF_FILE_IS_SUPER_ADMIN";
    public static final String USER_DETAILS = "POCKET_UNI_PREF_FILE_USER";
    public static final String USER_ID = "POCKET_UNI_PREF_FILE_USER_ID";
    public static final String USER_LOGGED_IN = "POCKET_UNI_PREF_FILE_LOGGED_IN";

    public static SharedPreferences sharedPreferences;
    public static Gson gson;

    public SharedPreferencesManager (Context context){
        sharedPreferences = context.getSharedPreferences(SharedPreferencesManager.PREF_FILE, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void savePreferences(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getPreferences(String key){
        return sharedPreferences.getString(key,"");
    }

    public void savePreferences(String key, Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBooleanPreferences(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void saveUserDataPreferences(String key, Admin adminObject){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(adminObject);
        editor.putString(key, json);
        editor.commit();
    }

    public Admin getUserDataPreferences(String key){
        String json = sharedPreferences.getString(key, "");
        Admin user = gson.fromJson(json, Admin.class);
        if (user != null) {
            return user;
        }else{
            return null;
        }
    }

    public void clearPreferences(String key){
        sharedPreferences.edit().remove(key).apply();
    }
}
