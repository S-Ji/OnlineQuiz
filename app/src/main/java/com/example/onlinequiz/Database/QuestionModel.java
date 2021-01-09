package com.example.onlinequiz.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.onlinequiz.Common.Helper;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Activity;
import com.google.android.gms.tasks.OnSuccessListener;
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
                        ArrayList<Question> questionArrayList = getQuestionArrayListBySnapshot(snapshot);
                        callback.listCallBack(questionArrayList, tag);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void listSpeechEnglish(String tag) {
        getCollectionRef()
                .orderByChild("IsVoiceAnswer")
                .equalTo("true")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Question> questionArrayList = getQuestionArrayListBySnapshot(snapshot);
                        callback.listCallBack(questionArrayList, tag);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public ArrayList<Question> getQuestionArrayListBySnapshot(DataSnapshot snapshot) {
        ArrayList<Question> questionArrayList = new ArrayList<>();
        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            Question ques = Question.getQuestionByDataSnapshot(postSnapshot);
            questionArrayList.add(ques);
        }
        return questionArrayList;
    }
}
