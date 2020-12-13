package com.example.onlinequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.Test;

public class TestDetailActivity extends AppCompatActivity {

    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        setupTest();
    }

    private void setupTest(){
        Intent i = getIntent();
        int testIndex = i.getIntExtra("testIndex", -1);
        if (testIndex >= 0){
            test = Commom.getCurrentUser().getTestManager().getTestArrayList().get(testIndex);
            test.loadQuestions();
            Toast.makeText(this, "Score: "+test.getScore(), Toast.LENGTH_SHORT).show();
        }else{
            finish();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}