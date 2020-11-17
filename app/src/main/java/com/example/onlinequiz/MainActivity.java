package com.example.onlinequiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
MaterialEditText edtNewUser, edtNewPassword, edtNewEmail;
MaterialEditText edtUser, edtPassword;
Button btnSignUp,btnSignIn;
FirebaseDatabase database;
DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");
        edtUser=(MaterialEditText)findViewById(R.id.edtUser);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);
        btnSignIn=(Button)findViewById(R.id.btn_sign_in);
        btnSignUp=(Button)findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }


        });

    }
    private void signIn(String user, String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        User login = snapshot.child(user).getValue(User.class);
                        if(login.getPassWord().equals(pwd)){
                            Intent homeActivity = new Intent(MainActivity.this,Home.class);
                            Commom.currentUser=login;
                            startActivity(homeActivity);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Wrong Password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Enter your user name!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"User is not exists!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showSignUpDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
          alertDialog.setTitle("Sign Up");
          alertDialog.setMessage("Please fill full information");
        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);
        edtNewUser=(MaterialEditText)sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail=(MaterialEditText)sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword=(MaterialEditText)sign_up_layout.findViewById(R.id.edtNewPassword);
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
                        if(snapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this,"User already exists!", Toast.LENGTH_SHORT).show();
                        else {
                            users.child(user.getUserName())
                                    .setValue(user);
                            Toast.makeText(MainActivity.this,"User registration success!", Toast.LENGTH_SHORT).show();
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
}