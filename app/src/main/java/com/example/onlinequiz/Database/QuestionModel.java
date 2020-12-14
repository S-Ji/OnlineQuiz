package com.example.onlinequiz.Database;

import androidx.annotation.NonNull;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.ViewHolder.Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Question> questionArrayList = new ArrayList<>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Question ques = postSnapshot.getValue(Question.class);
                            ques.setId(postSnapshot.getKey());
                            questionArrayList.add(ques);
                            //Commom.questionsList.add(ques);
                        }
                        callback.listCallBack(questionArrayList, tag);
                        // RANDOM LIST
                        //Commom.shuffleQuestionList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
