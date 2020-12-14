package com.example.onlinequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.onlinequiz.Adapter.Common.TestAdapter;
import com.example.onlinequiz.Adapter.Common.TestQuestionAdapter;
import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.Test;

import java.util.ArrayList;

public class TestDetailActivity extends AppCompatActivity {

    ListView lvQuestion;
    TestQuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        mapping();
        setupTest();
        initListView();
    }

    private void initListView() {
        questionAdapter = new TestQuestionAdapter(this, R.layout.test_question_item_layout, Commom.getTest().getQuestions());
        lvQuestion.setAdapter(questionAdapter);
    }

    private void setupTest() {
        if (Commom.getTest() != null) {
            try{
                Toast.makeText(this, Commom.getTest().getQuestions().get(0).getQuestion().getQuestion(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                exitAndToastErrMessage();
                e.printStackTrace();
            }
        } else exitAndToastErrMessage();
    }

    private void mapping() {
        lvQuestion = (ListView) findViewById(R.id.lvQuestion);
    }

    private void exitAndToastErrMessage(){
        finish();
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}