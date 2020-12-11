package com.example.onlinequiz.Common;

import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Commom {
    public static String categoryId, categoryName;
    public static User currentUser;
    public static List<Question> questionsList = new ArrayList<>();
    public static List<Question> questionsRight = new ArrayList<>();
    public static List<Question> questionsWrong = new ArrayList<>();
}
