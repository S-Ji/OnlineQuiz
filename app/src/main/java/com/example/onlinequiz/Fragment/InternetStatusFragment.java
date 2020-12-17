package com.example.onlinequiz.Fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.onlinequiz.R;

public class InternetStatusFragment extends MyFragment {

    TextView txtConnected, txtNotConnected;
    View v;
    boolean isPristine = true;    boolean prevStatus = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_internet_status, container, false);
        mapping();
        return v;
    }

    private void mapping() {
        txtConnected = (TextView) v.findViewById(R.id.txtConnected);
        txtNotConnected = (TextView) v.findViewById(R.id.txtNotConnected);
    }

    public void onInternetStatusChange(boolean isConnected) {
        v.setVisibility(View.GONE);
        if (!(isConnected && isPristine)) {
            if (isConnected) {
                txtConnected.setVisibility(View.VISIBLE);
                txtNotConnected.setVisibility(View.GONE);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                hideWithAnimation();
                                Log.d("xxx", "connected");
                            }
                        }, 500);
            } else {
                txtNotConnected.setVisibility(View.VISIBLE);
                txtConnected.setVisibility(View.GONE);
                showWithAnimation();
                Log.d("xxx", "not connected");
            }
            v.setVisibility(View.VISIBLE);
        }
        isPristine = false;
    }

    public void hideWithAnimation() {
        Animation animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_down_for_hiding);
        v.startAnimation(animScale);
    }

    public void showWithAnimation() {
        Animation animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_up_for_showing);
        v.startAnimation(animScale);
    }

    public void setPristine(boolean value) {
        isPristine = value;
    }
}