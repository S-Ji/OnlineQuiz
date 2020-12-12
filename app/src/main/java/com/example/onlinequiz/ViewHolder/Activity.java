package com.example.onlinequiz.ViewHolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class Activity extends AppCompatActivity {
    public SharedPreferences getSharedParams() {
        SharedPreferences sharedPreferences = getSharedPreferences("global-package", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void sharedParamsPutInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedParams().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void sharedParamsPutString(String key, String value) {
        SharedPreferences.Editor editor = getSharedParams().edit();
        editor.putString(key, value);
        editor.apply();
    }
}
