package com.example.onlinequiz.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionModel extends Model {
    private ICallback<Question> callback;

    public QuestionModel(Activity activity) {
        super(activity, "Questions");
        callback = (ICallback<Question>) activity;
    }

    public void listItemsByCategoryId(String categoryId, String tag) {
        getCollectionRef()
                .orderByChild("CategoryId")
                .equalTo(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Question> questionArrayList = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Question ques = postSnapshot.getValue(Question.class);
                            ques.setId(postSnapshot.getKey());
                            setIsImageAnswer(postSnapshot, ques);
                            setIsSpeechQuestion(postSnapshot, ques);
                            questionArrayList.add(ques);
                        }
                        callback.listCallBack(questionArrayList, tag);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void listSpeechEnglish(String tag) {
        getCollectionRef()
                .orderByChild("IsSpeechQuestion")
                .equalTo("true")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Question> questionArrayList = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Question ques = postSnapshot.getValue(Question.class);
                            ques.setId(postSnapshot.getKey());
                            setIsImageAnswer(postSnapshot, ques);
                            setIsSpeechQuestion(postSnapshot, ques);
                            questionArrayList.add(ques);
                        }
                        callback.listCallBack(questionArrayList, tag);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void setIsImageAnswer(DataSnapshot questionSnapshot, Question question) {
        DataSnapshot isImageAnswerSnapshot = questionSnapshot.child("IsImageAnswer");
        String isImageAnswer = "false";
        if (isImageAnswerSnapshot.exists()) {
            isImageAnswer = isImageAnswerSnapshot.getValue().toString();
        }
        question.setIsImageAnswer(isImageAnswer);
    }

    private void setIsSpeechQuestion(DataSnapshot questionSnapshot, Question question) {
        DataSnapshot isSpeechQuestionSnapshot = questionSnapshot.child("IsSpeechQuestion");
        String isSpeechQuestion = "false";
        if (isSpeechQuestionSnapshot.exists()) {
            isSpeechQuestion = isSpeechQuestionSnapshot.getValue().toString();
        }
        question.setIsSpeechQuestion(isSpeechQuestion);
    }
}
