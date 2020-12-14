package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Common.Helper;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Database.UserModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.RandomAnswerQuestion;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.Model.User;
import com.example.onlinequiz.ViewHolder.Activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Playing extends Activity implements View.OnClickListener, ICallback<UserModel> {

    RelativeLayout rltMain;
    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;
    MediaPlayer correctAnswerMp3;
    MediaPlayer wrongAnswerMp3;
    final static long INTERVAL = 2000;
    final static long TIMEOUT = 12000;
    int progressValue = 0;
    int index = 0, score = 0, thisQuestion = 0, correctAnswer;

    CountDownTimer mCountDown;

    Test test;
    QuestionInTest questionInTest;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        userModel = new UserModel(this);
        initTest();
        mapping();
        initEvents();
    }

    private void initTest() {
        test = new Test();
    }

    private void initMp3() {
        correctAnswerMp3 = MediaPlayer.create(this, R.raw.correct);
        wrongAnswerMp3 = MediaPlayer.create(this, R.raw.wrong);
    }

    private void initEvents() {
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    // ON ANSWER SELECTED
    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        initMp3();

        if (index < getTotalQuestion()) {
            Button clickedButton = (Button) v;
            String userAnswer = clickedButton.getText().toString();
            questionInTest.setUserAnswer(userAnswer);
            test.addQuestion(questionInTest);
            if (isCorrectAnswer(userAnswer)) onUserSelectCorrectAnswer();
            else onUserSelectWrongAnswer();
            //

            showQuestion(++index);
            displayScore();
        } else onDone();
    }

    private void onUserSelectCorrectAnswer() {
        wrongAnswerMp3.pause();
        correctAnswerMp3.start();
        score += 10;
        correctAnswer++;
    }

    private void onUserSelectWrongAnswer() {
        correctAnswerMp3.pause();
        wrongAnswerMp3.start();
    }

    private void displayScore() {
        txtScore.setText(String.format("%d", score));
    }

    private boolean isCorrectAnswer(String answer) {
        return (answer.trim().equals(getCurrentQuestion().getCorrectAnswer().trim()));
    }


    // SHOW QUESTION
    private void showQuestion(int index) {
        if (index < getTotalQuestion()) {
            thisQuestion++;
            displayQuestionNum();
            resetProgress();
            displayQuestion();

            // RANDOM ANSWER
            displayAnswer();
            //
            mCountDown.start();
        } else onDone();
    }

    private void displayAnswer() {
        RandomAnswerQuestion randomAnswerQuestion = new RandomAnswerQuestion();
        ArrayList<String> answerOrder = randomAnswerQuestion.getRandomAnswerOrder();
        displayAnswerByLetter(answerOrder.get(0), getCurrentQuestion().getA());
        displayAnswerByLetter(answerOrder.get(1), getCurrentQuestion().getB());
        displayAnswerByLetter(answerOrder.get(2), getCurrentQuestion().getC());
        displayAnswerByLetter(answerOrder.get(3), getCurrentQuestion().getD());
        questionInTest = new QuestionInTest();
        questionInTest.setQuestionId(getCurrentQuestion().getId());
        questionInTest.setAnswerOrder(randomAnswerQuestion.getRandomAnswerOrder());
    }

    private void displayAnswerByLetter(String letter, String answer) {
        Button btn = null;
        switch (letter) {
            case "a":
                btn = btnA;
                break;
            case "b":
                btn = btnB;
                break;
            case "c":
                btn = btnC;
                break;
            case "d":
                btn = btnD;
                break;
        }
        if (btn != null) btn.setText(answer);
    }

    private void displayQuestionNum() {
        txtQuestionNum.setText(String.format("%d / %d", thisQuestion, getTotalQuestion()));
    }

    private void resetProgress() {
        progressBar.setProgress(0);
        progressValue = 0;
    }

    private void displayQuestion() {
        if (getCurrentQuestion().getIsImageQuestion().equals("true")) {
            //if question img
            Picasso.with(getBaseContext())
                    .load(getCurrentQuestion().getQuestion())
                    .into(question_image);
            question_image.setVisibility(View.VISIBLE);
            question_text.setVisibility(View.INVISIBLE);
        } else {
            //if question text, set img visible
            question_text.setText(getCurrentQuestion().getQuestion());
            question_image.setVisibility(View.INVISIBLE);
            question_text.setVisibility(View.VISIBLE);
        }
    }

    private void onDone() {
        test.setCategoryId(Commom.categoryId);
        test.setNumberOfQuestion(getTotalQuestion());
        test.setScore(score);
        test.setDate(Helper.getCurrentISODateString());
        Commom.getCurrentUser().getTestManager().add(test);
        Commom.questionsList.clear();
        userModel.updateCurrentUserTests(ModelTag.updateCurrentUserTests);


        Intent intent = new Intent(this, Done.class);
        Bundle dataSend = new Bundle();
        dataSend.putInt("SCORE", score);
        dataSend.putInt("TOTAL", getTotalQuestion());
        dataSend.putInt("CORRECTED", correctAnswer);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startTest();
    }

    private void startTest() {
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }

    private void mapping() {
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestionNum = (TextView) findViewById(R.id.txtTotalQuestion);
        question_text = (TextView) findViewById(R.id.question_text);
        question_image = (ImageView) findViewById(R.id.question_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rltMain = (RelativeLayout) findViewById(R.id.rltMain);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);
    }

    private int getTotalQuestion() {
        return Commom.testQuestionQty;
    }

    // SUPPORTED METHOD
    private Question getCurrentQuestion() {
        //return Commom.questionsList.get(index);
        return Commom.questionsList.get(index);
    }

    @Override
    public void itemCallBack(UserModel item, String tag) { }

    @Override
    public void listCallBack(ArrayList<UserModel> items, String tag) { }
}
