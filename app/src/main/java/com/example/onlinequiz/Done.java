package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {
    
    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);
        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.crash);
        final MediaPlayer mp3 = MediaPlayer.create(this, R.raw.wow);
        final MediaPlayer mp4 = MediaPlayer.create(this, R.raw.meme);
        final MediaPlayer mp5 = MediaPlayer.create(this, R.raw.omg);
        final MediaPlayer mp6 = MediaPlayer.create(this, R.raw.johnsena);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this,Home.class);
                mp4.pause();
                mp3.pause();
                mp5.pause();
                mp6.pause();
                mp.start();
                startActivity(intent);
                finish();
            }
        });

        //get data from bundle, set to view
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECTED");
            if(score < 50) {
                mp4.start();
            }
            if(score >= 50 && score <= 70) {
                mp3.start();
            }
            if(score > 70 && score <= 90) {
                mp5.start();
            }
            if(score > 90) {
                mp6.start();
            }
            txtResultScore.setText(String.format("SCORE : %d",score));
            getTxtResultQuestion.setText(String.format("CORRECTED : %d / %d",correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //upload point each user to firebase
            question_score.child(String.format("%s_%s", Commom.currentUser.getUserName(),
                                                Commom.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Commom.currentUser.getUserName(),
                                                Commom.categoryId),
                                                Commom.currentUser.getUserName(),
                                                String.valueOf(score),
                                                Commom.categoryId,
                                                Commom.categoryName));


        }
    }


}