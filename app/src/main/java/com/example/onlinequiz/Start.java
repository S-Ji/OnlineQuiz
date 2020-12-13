package com.example.onlinequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

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
    RadioGroup radioGroupQuestionQty;

    FirebaseDatabase database;
    DatabaseReference questions;
    int questionQty = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mapping();
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        loadQuestion(Commom.categoryId);
        initEvents();
    }

    private void initEvents(){
        onBtnPlayClicked();
        onQuestionQtyRadioChecked();
    }

    private void onQuestionQtyRadioChecked(){
        radioGroupQuestionQty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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

    private void onBtnPlayClicked(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.crash);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commom.testQuestionQty = (questionQty<=Commom.questionsList.size()) ? questionQty : Commom.questionsList.size();
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
                mp.start();
                finish();
            }
        });
    }

    private void mapping(){
        btnPlay = (Button)findViewById(R.id.btnPlay);
        radioGroupQuestionQty = (RadioGroup)findViewById(R.id.radioGroupQuestionQty);
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
                    ques.setId(postSnapshot.getKey());
                    Commom.questionsList.add(ques);
                }
                // RANDOM LIST
                Commom.shuffleQuestionList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //random list
        Collections.shuffle(Commom.questionsList);
    }
}