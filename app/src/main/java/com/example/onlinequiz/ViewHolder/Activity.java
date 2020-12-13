package com.example.onlinequiz.ViewHolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        return inetAddress!=null && !inetAddress.equals("");
    }

    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
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

    protected void doSomethingOnNetworkChange(){
        /*
        if (internetConnectionAvailable()){
            Toast.makeText(this, "has internet", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "no internet", Toast.LENGTH_SHORT).show();
        }

         */
    };
}
