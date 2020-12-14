package com.example.onlinequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.onlinequiz.Adapter.Common.TestAdapter;
import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Database.QuestionModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.ViewHolder.Activity;

import java.util.ArrayList;

public class MyTestActivity extends Activity implements ICallback<Question> {

    RelativeLayout rltMain, rltMessage;
    ListView lvTest;
    TestAdapter testAdapter;
    ArrayList<Test> testArrayList;

    QuestionModel questionModel;

    boolean isLoadingTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        questionModel = new QuestionModel(this);
        mapping();
        testArrayList = Commom.getCurrentUser().getTestManager().getTestArrayList();
        if (testArrayList.size() > 0) {
            rltMessage.setVisibility(View.GONE);
            initListView();
        } else rltMain.setVisibility(View.GONE);
    }

    private void initListView() {
        testAdapter = new TestAdapter(this, R.layout.test_item_layout, testArrayList);
        lvTest.setAdapter(testAdapter);
        initListViewEvents();
    }

    private void initListViewEvents() {
        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isLoadingTest) {
                    loadTest(testArrayList.get(position));
                }
                isLoadingTest = true;
            }
        });
    }

    private void loadTest(Test test) {
        Commom.setTest(test);
        questionModel.listItemsByCategoryId(Commom.test.getCategoryId(), ModelTag.listQuestionsByCategoryIdForDisplayTestHistory);
    }

    private void mapping() {
        lvTest = (ListView) findViewById(R.id.lvTest);
        rltMain = (RelativeLayout) findViewById(R.id.rltMain);
        rltMessage = (RelativeLayout) findViewById(R.id.rltMessage);
    }

    // CALLBACK
    @Override
    public void itemCallBack(Question item, String tag) {

    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag.equals(ModelTag.listQuestionsByCategoryIdForDisplayTestHistory))
            onListItemCallback(items);
    }

    private void onListItemCallback(ArrayList<Question> questionArrayList) {
        Log.d("xxx", "test question: " + questionArrayList.size());
        ArrayList<String> questionIds = Commom.test.getQuestionIdArrayList();
        for (QuestionInTest questionInTest : Commom.getTest().getQuestions()) {
            questionInTest.setQuestion(getQuestionById(questionArrayList, questionInTest.getQuestionId()));
        }
        Intent i = new Intent(MyTestActivity.this, TestDetailActivity.class);
        startActivity(i);
    }

    private Question getQuestionById(ArrayList<Question> questionArrayList, String id) {
        for (Question question : questionArrayList) {
            if (question.getId().equals(id)) return question;
        }
        return null;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isLoadingTest = false;
    }
}