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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Common.Helper;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.RandomAnswerQuestion;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 2000;
    final static long TIMEOUT = 12000;
    int progressValue = 0;

    CountDownTimer mCountDown;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;

    FirebaseDatabase database;
    DatabaseReference users;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;
    MediaPlayer correctAnswerMp3;
    MediaPlayer wrongAnswerMp3;
    Test test;
    QuestionInTest questionInTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        initTest();
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        users = database.getReference("Users");
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

        if (index < totalQuestion) {
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
        if (index < totalQuestion) {
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
        Log.d("xxx", "random :" + randomAnswerQuestion.getRandomAnswerOrder());
        Toast.makeText(this, "random", Toast.LENGTH_SHORT).show();
        btnA.setText(getCurrentQuestion().getA());
        btnB.setText(getCurrentQuestion().getB());
        btnC.setText(getCurrentQuestion().getC());
        btnD.setText(getCurrentQuestion().getD());
        questionInTest = new QuestionInTest();
        questionInTest.setQuestionId(getCurrentQuestion().getId());
        questionInTest.setAnswerOrder(randomAnswerQuestion.getRandomAnswerOrder());
    }

    private void displayQuestionNum() {
        txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
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
        test.setNumberOfQuestion(totalQuestion);
        test.setScore(score);
        test.setDate(Helper.getCurrentISODateString());
        Commom.getCurrentUser().getTestManager().add(test);
        saveUser();
        Log.d("xxx", "test manager size: " + Commom.getCurrentUser().getTestManager().getTestArrayList().size());

        Toast.makeText(this, "show info", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Done.class);
        Bundle dataSend = new Bundle();
        dataSend.putInt("SCORE", score);
        dataSend.putInt("TOTAL", totalQuestion);
        dataSend.putInt("CORRECTED", correctAnswer);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
    }

    private void saveUser() {
        User currentUser = Commom.getCurrentUser();
        users.child(currentUser.getUserName()).child("tests").setValue(currentUser.getTestManager().getSavingJsonString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("xxx", "tests updated: "+currentUser.getTestManager().getJsonArray().toString());
                    }
                });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        totalQuestion = Commom.testQuestionQty;
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

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);
    }

    // SUPPORTED METHOD
    private Question getCurrentQuestion() {
        return Commom.questionsList.get(index);
    }
}
