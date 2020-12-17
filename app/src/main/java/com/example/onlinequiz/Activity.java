package com.example.onlinequiz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.SharedPreferencesKey;
import com.example.onlinequiz.Fragment.InternetStatusFragment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Activity extends AppCompatActivity {
    FragmentManager fragmentManager = getFragmentManager();
    InternetStatusFragment internetStatusFragment;

    // SHARED PREFERENCES
    public SharedPreferences getSharedParams() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesKey.globalPackage, Context.MODE_PRIVATE);
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

    public String sharedParamsGetString(String key, String defaultValue) {
        String result = null;
        SharedPreferences sharedPreferences = getSharedParams();
        result = sharedPreferences.getString(key, defaultValue);
        return result;

    }


    // INTERNET
    public boolean internetConnectionAvailable() {
        int timeOut = 3000;
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doSomethingOnNetworkChange();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    protected void doSomethingOnNetworkChange() {
        if (internetStatusFragment != null) {
            internetStatusFragment.onInternetStatusChange(internetConnectionAvailable());
        }
    }

    protected void initInternetStatusFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        internetStatusFragment = new InternetStatusFragment();
        fragmentTransaction.replace(R.id.frameInternetStatus, internetStatusFragment, "internet-status");
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (internetStatusFragment != null) internetStatusFragment.setPristine(true);
    }
}
