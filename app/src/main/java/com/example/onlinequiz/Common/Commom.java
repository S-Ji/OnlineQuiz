package com.example.onlinequiz.Common;

import android.util.Log;

import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commom {
    public static String categoryId, categoryName;
    public static User currentUser;
    public static int testQuestionQty = 5;
    public static List<Question> questionsList = new ArrayList<>();
    public static List<Question> questionsRight = new ArrayList<>();
    public static List<Question> questionsWrong = new ArrayList<>();

    public static void shuffleQuestionList(){
        Collections.shuffle(Commom.questionsList);
    }
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        Commom.currentUser = currentUser;
    }
}
