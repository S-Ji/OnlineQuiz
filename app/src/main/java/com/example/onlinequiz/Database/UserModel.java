package com.example.onlinequiz.Database;


import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.User;
import com.example.onlinequiz.Activity;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserModel extends Model {
    private ICallback<User> callback;

    public UserModel(Activity activity) {
        super(activity, "Users");
        callback = (ICallback<User>) activity;
    }

    public void updateCurrentUserTests(String tag) {
        User currentUser = Common.getCurrentUser();
        getCollectionRef()
                .child(currentUser.getUserName())
                .child("tests")
                .setValue(currentUser.getTestManager().getSavingJsonString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

}
