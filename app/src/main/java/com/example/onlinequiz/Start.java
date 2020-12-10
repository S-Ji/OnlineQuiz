package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Start extends AppCompatActivity {
    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestion(Commom.categoryId);
        btnPlay = (Button)findViewById(R.id.btnPlay);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.crash);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
                mp.start();
                finish();
            }
        });
    }
    private void loadQuestion(String categoryId) {
        // clear list if have old question
        if(Commom.questionsList.size() > 0){
            Commom.questionsList.clear();
        }
        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Question ques = postSnapshot.getValue(Question.class);
                    Commom.questionsList.add(ques);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //random list
        Collections.shuffle(Commom.questionsList);
    }
}