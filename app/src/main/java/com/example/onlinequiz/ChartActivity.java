package com.example.onlinequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlinequiz.Fragment.PieChartFragment;
import com.example.onlinequiz.Fragment.VoiceFragment;

public class ChartActivity extends Activity {

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initChartFragment();
        mapping();
        initBtnBackClick();
    }

    private void initBtnBackClick(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initChartFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PieChartFragment pieChartFragment = new PieChartFragment();
        fragmentTransaction.replace(R.id.frameChart, pieChartFragment, "pie-chart-fragment");
        fragmentTransaction.commit();
    }

    private void mapping() {
        btnBack = (Button) findViewById(R.id.btnBack);
    }
}