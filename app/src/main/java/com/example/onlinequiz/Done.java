package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends Activity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;
    MediaPlayer doneMusic;
    MediaPlayer startMusic;
    MediaPlayer mp3;
    MediaPlayer mp5;
    MediaPlayer mp6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        mapping();

        startMusic = MediaPlayer.create(this, R.raw.crash);
        doneMusic = MediaPlayer.create(this, R.raw.meme);
        mp3 = MediaPlayer.create(this, R.raw.wow);
        mp5 = MediaPlayer.create(this, R.raw.omg);
        mp6 = MediaPlayer.create(this, R.raw.johnsena);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, Home.class);
                doneMusic.pause();
                mp3.pause();
                mp5.pause();
                mp6.pause();
                startMusic.start();
                startActivity(intent);
                finish();
            }
        });

        //get data from bundle, set to view
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECTED");
            float percent = ((float) correctAnswer / totalQuestion) * 100;

            if (percent < 50) {
                doneMusic.start();
            } else if (percent <= 70) {
                mp3.start();
            } else if (percent <= 90) {
                mp5.start();
            } else {
                mp6.start();
            }
            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("CORRECTED : %d / %d", correctAnswer, totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //upload point each user to firebase
            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(),
                    Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                            Common.categoryId),
                            Common.currentUser.getUserName(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));


        }
        initInternetStatusFragment();
    }

    private void mapping() {
        txtResultScore = (TextView) findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView) findViewById(R.id.txtTotalQuestion);
        progressBar = (ProgressBar) findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doneMusic.reset();
        startMusic.reset();
        mp3.reset();
        mp5.reset();
        mp6.reset();
    }
}