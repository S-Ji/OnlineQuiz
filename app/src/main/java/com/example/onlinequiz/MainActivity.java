package com.example.onlinequiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Common.Message;
import com.example.onlinequiz.Common.SharedPreferencesKey;
import com.example.onlinequiz.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail;
    MaterialEditText edtUser, edtPassword;
    Button btnSignUp, btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;
    boolean isLoadingUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        checkAuth();
        initEvents();
        initInternetStatusFragment();
    }

    private void initEvents() {
        onSignUpBtnClick();
        onSignInBtnClick();
    }

    private void onSignUpBtnClick() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });
    }

    private void onSignInBtnClick() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadingUser) {
                    if (internetConnectionAvailable()) {
                        String username = edtUser.getText().toString();
                        if (username.trim() != "")
                            signIn(username, edtPassword.getText().toString());
                        else
                            Toast.makeText(MainActivity.this, Message.enterUsername, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, Message.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAuth() {
        SharedPreferences sharedPreferences = getSharedParams();
        if (sharedPreferences != null) {
            String loggedUsername = sharedPreferences.getString("loggedUsername", "");
            String loggedPassword = sharedPreferences.getString("loggedPassword", "");
            if (!loggedUsername.equals("") && !loggedPassword.equals("")) {
                edtUser.setText(loggedUsername);
                edtPassword.setText(loggedPassword);
                signIn(loggedUsername, loggedPassword);
            }
        }
    }

    private void signIn(String username, String pwd) {
        if (isValidUsername(username)) {
            isLoadingUser = true;
            users.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String msg = "";
                    if (snapshot.exists()) {
                        User loggedUser = snapshot.getValue(User.class);
                        if (loggedUser.getPassWord().equals(pwd)) {
                            saveLoggedUserInfo(username, pwd);
                            Common.currentUser = loggedUser;
                            setUserTests(snapshot);

                            // start home activity
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            startActivity(homeActivity);
                            finish();
                        } else msg = Message.wrongPassword;
                    } else msg = Message.userNotExist;
                    if (msg != "") {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        isLoadingUser = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, Message.checkInternet, Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(this, Message.usernameInvalid, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }

    private void setUserTests(DataSnapshot userSnapshot) {
        DataSnapshot testsSnapshot = userSnapshot.child("tests");
        String testsJsonString = (testsSnapshot.exists())
                ? testsSnapshot.getValue().toString()
                : "[]";
        Common.currentUser.setTestManagerByJsonString(testsJsonString);
    }

    private void saveLoggedUserInfo(String username, String password) {
        sharedParamsPutString(SharedPreferencesKey.loggedUsername, username);
        sharedParamsPutString(SharedPreferencesKey.loggedPassword, password);
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage(Message.fillForm);
        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);
        edtNewUser = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewPassword);
        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_account_circle_24);
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = new User(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this, Message.userAlreadyExist, Toast.LENGTH_SHORT).show();
                        else {
                            users.child(user.getUserName()).setValue(user.getDocData());
                            Toast.makeText(MainActivity.this, Message.userRegistrationSuccess, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void mapping() {
        edtUser = (MaterialEditText) findViewById(R.id.edtUser);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}