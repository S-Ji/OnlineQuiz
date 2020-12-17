package com.example.onlinequiz;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.onlinequiz.Adapter.TestQuestionAdapter;
import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Common.Message;

public class TestDetailActivity extends Activity {

    ListView lvQuestion;
    TestQuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        checkValidTest();
        mapping();
        initListView();
        initInternetStatusFragment();
    }

    private void initListView() {
        questionAdapter = new TestQuestionAdapter(this, R.layout.test_question_item_layout, Common.getTest().getQuestions());
        lvQuestion.setAdapter(questionAdapter);
    }

    private void checkValidTest() {
        boolean isValidTest = false;
        if (Common.getTest() != null) {
            if (Common.getTest().getQuestions().size() > 0) isValidTest = true;
        }
        if (!isValidTest) exitAndToastErrMessage();
    }

    private void mapping() {
        lvQuestion = (ListView) findViewById(R.id.lvQuestion);
    }

    private void exitAndToastErrMessage() {
        finish();
        Toast.makeText(this, Message.failedToDisplayTestDetail, Toast.LENGTH_SHORT).show();
    }
}