package com.example.onlinequiz.Common;

import android.util.Log;

import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commom {
    public static String categoryId;
    public static User currentUser;
    public static int testQuestionQty = 5;
    public static List<Question> questionsList = new ArrayList<>();
    public static List<Question> questionsRight = new ArrayList<>();
    public static List<Question> questionsWrong = new ArrayList<>();

    public static void shuffleQuestionList(){
        Log.d("xxx", "before shuffle "+Commom.questionsList);
        Collections.shuffle(Commom.questionsList);
        Log.d("xxx", "after shuffle "+Commom.questionsList);
    }

}
