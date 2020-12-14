package com.example.onlinequiz.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.User;
import com.example.onlinequiz.ViewHolder.Activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserModel extends Model {
    private ICallback<User> callback;

    public UserModel(Activity activity) {
        super(activity, "Users");
        callback = (ICallback<User>) activity;
    }

    public void updateCurrentUserTests(String tag) {
        User currentUser = Commom.getCurrentUser();
        getCollectionRef()
                .child(currentUser.getUserName())
                .child("tests")
                .setValue(currentUser.getTestManager().getSavingJsonString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("xxx", "tests size" + Commom.getCurrentUser().getTestManager().getTestArrayList().size());
                        Log.d("xxx", "tests updated: " + currentUser.getTestManager().getJsonArray().toString());
                    }
                });
    }

}
