package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 2000;
    final static long TIMEOUT = 12000;
    int progressValue = 0;

    CountDownTimer mCountDown;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;

    FirebaseDatabase database;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text;
    MediaPlayer correctAnswerMp3;
    MediaPlayer wrongAnswerMp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        mapping();
        initEvents();
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
            displayAnswer();
            mCountDown.start();
        } else onDone();
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

    private void displayAnswer() {
        btnA.setText(getCurrentQuestion().getA());
        btnB.setText(getCurrentQuestion().getB());
        btnC.setText(getCurrentQuestion().getC());
        btnD.setText(getCurrentQuestion().getD());
    }

    private void onDone() {
        Intent intent = new Intent(this, Done.class);
        Bundle dataSend = new Bundle();
        dataSend.putInt("SCORE", score);
        dataSend.putInt("TOTAL", totalQuestion);
        dataSend.putInt("CORRECTED", correctAnswer);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
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

    // SUPPORTED METHODS
    private Question getCurrentQuestion() {
        return Commom.questionsList.get(index);
    }
}
