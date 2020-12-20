package com.example.onlinequiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.onlinequiz.Adapter.TestAdapter;
import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Common.SharedPreferencesKey;
import com.example.onlinequiz.Database.QuestionModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.Model.TestManager;

import java.util.ArrayList;

public class MyTestActivity extends Activity implements ICallback<Question> {

    RelativeLayout rltMain, rltMessage;
    Spinner spinnerSort;
    ListView lvTest;
    TestAdapter testAdapter;
    ArrayList<Test> testArrayList;

    QuestionModel questionModel;
    ArrayAdapter<String> adapter;
    ArrayList<String> sortArrayList;

    boolean isLoadingTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        questionModel = new QuestionModel(this);
        mapping();
        setupTestArrayList();
        checkTestArrayList();
        initInternetStatusFragment();
        initSortSpinner();
    }

    private void setupTestArrayList() {
        onSort();
        testArrayList = Common.getCurrentUser().getTestManager().getTestArrayList();
    }

    private void checkTestArrayList() {
        if (testArrayList.size() > 0) {
            rltMessage.setVisibility(View.GONE);
            initListView();
        } else rltMain.setVisibility(View.GONE);
    }

    private void initSortSpinner() {
        sortArrayList = TestManager.getSortArrayList();
        adapter = new ArrayAdapter<String>(MyTestActivity.this,
                android.R.layout.simple_spinner_item, sortArrayList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        initSpinnerSortClicked();
        spinnerSort.setSelection(getCurrentSortIndex());
    }

    private void onSort() {
        String sortValue = getCurrentSortValue();
        if (sortValue.equals("Latest test")) {
            Common.getCurrentUser().getTestManager().sortByDate();
        } else if (sortValue.equals("Highest test score")) {
            Common.getCurrentUser().getTestManager().sortByScore();
        }
    }

    private void initSpinnerSortClicked() {
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedParamsPutString(SharedPreferencesKey.myTestSortField, sortArrayList.get(position));
                onSort();
                testAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        Common.setTest(test);
        String categoryName = Question.getCategoryNameById(Common.getTest().getCategoryId());
        if (categoryName.equals("English Speech")) {
            questionModel.listSpeechEnglish(ModelTag.listQuestionsByCategoryIdForDisplayTestHistory);
        } else {
            questionModel.listItemsByCategoryId(Common.test.getCategoryId(), ModelTag.listQuestionsByCategoryIdForDisplayTestHistory);
        }
    }

    private void mapping() {
        lvTest = (ListView) findViewById(R.id.lvTest);
        rltMain = (RelativeLayout) findViewById(R.id.rltMain);
        rltMessage = (RelativeLayout) findViewById(R.id.rltMessage);
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
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
        ArrayList<String> questionIds = Common.test.getQuestionIdArrayList();
        for (QuestionInTest questionInTest : Common.getTest().getQuestions()) {
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

    private int getCurrentSortIndex() {
        int index = sortArrayList.indexOf(getCurrentSortValue());
        return index;
    }

    private String getCurrentSortValue() {
        String value = sharedParamsGetString(SharedPreferencesKey.myTestSortField, "Latest test");
        return value;
    }
}