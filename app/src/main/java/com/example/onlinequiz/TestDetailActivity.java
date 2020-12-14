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
    ArrayList<QuestionInTest> questionArrayList;
    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        mapping();
        setupTest();
        initListView();
    }

    private void initListView() {
        questionArrayList = test.getQuestions();
        questionAdapter = new TestQuestionAdapter(this, R.layout.test_question_item_layout, questionArrayList);
        lvQuestion.setAdapter(questionAdapter);
        /*
        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyTestActivity.this, TestDetailActivity.class);
                i.putExtra("testIndex", position);
                startActivity(i);
            }
        });

         */
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

    private void mapping(){
        lvQuestion = (ListView)findViewById(R.id.lvQuestion);
    }


}