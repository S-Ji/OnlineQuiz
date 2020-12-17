package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Database.QuestionModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;

import java.util.ArrayList;

public class Start extends Activity implements ICallback<Question> {
    Button btnPlay;
    RadioGroup radioGroupQuestionQty;

    int questionQty = 5;
    boolean isDataLoaded = false;
    boolean isStartClicked = false;

    QuestionModel questionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        questionModel = new QuestionModel(this);
        mapping();
        loadQuestion(Common.categoryId);
        initEvents();
        initInternetStatusFragment();
    }

    private void initEvents() {
        onBtnPlayClicked();
        onQuestionQtyRadioChecked();
    }

    private void onQuestionQtyRadioChecked() {
        radioGroupQuestionQty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio5:
                        questionQty = 5;
                        break;
                    case R.id.radio10:
                        questionQty = 10;
                        break;
                    case R.id.radio15:
                        questionQty = 15;
                        break;
                }
            }
        });
    }

    private void onBtnPlayClicked() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartClicked = true;
                if (isDataLoaded) play();
            }
        });
    }

    private void setTestQuestionQty(){
        Common.testQuestionQty = (questionQty <= Common.questionsList.size()) ? questionQty : Common.questionsList.size();
    }

    private void mapping() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        radioGroupQuestionQty = (RadioGroup) findViewById(R.id.radioGroupQuestionQty);
    }

    private void loadQuestion(String categoryId) {
        questionModel.listItemsByCategoryId(categoryId, ModelTag.listQuestionsByCategoryIdForDisplayTestQuestions);
    }

    // CALLBACK
    @Override
    public void itemCallBack(Question item, String tag) {
    }
    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag.equals(ModelTag.listQuestionsByCategoryIdForDisplayTestQuestions)) onTestQuestionsCallback(items);
    }

    private void onTestQuestionsCallback(ArrayList<Question> questionArrayList) {
        Common.questionsList.clear();
        Common.questionsList.addAll(questionArrayList);
        Common.shuffleQuestionList();
        isDataLoaded = true;
        if (isStartClicked) play();
    }

    private void play() {
        setTestQuestionQty();
        MediaPlayer mp = MediaPlayer.create(this, R.raw.crash);
        Intent intent = new Intent(Start.this, Playing.class);
        startActivity(intent);
        mp.start();
        finish();
    }
}