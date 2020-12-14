package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Database.QuestionModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.ViewHolder.Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

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
        loadQuestion(Commom.categoryId);
        initEvents();
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
        Commom.testQuestionQty = (questionQty <= Commom.questionsList.size()) ? questionQty : Commom.questionsList.size();
    }

    private void mapping() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        radioGroupQuestionQty = (RadioGroup) findViewById(R.id.radioGroupQuestionQty);
    }

    private void loadQuestion(String categoryId) {
        questionModel.listItemsByCategoryId(categoryId, ModelTag.listTestQuestions);
    }

    // CALLBACK
    @Override
    public void itemCallBack(Question item, String tag) {
    }
    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag.equals(ModelTag.listTestQuestions)) onTestQuestionsCallback(items);
    }

    private void onTestQuestionsCallback(ArrayList<Question> questionArrayList) {
        Commom.questionsList.clear();
        Commom.questionsList.addAll(questionArrayList);
        Commom.shuffleQuestionList();
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