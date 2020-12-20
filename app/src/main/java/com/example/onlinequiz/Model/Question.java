package com.example.onlinequiz.Model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class Question {
    private String id;
    private String Question;
    private String A;
    private String B;
    private String C;
    private String D;
    private String CorrectAnswer;
    private String CategoryId;
    private String IsImageQuestion;
    private String IsSpeechQuestion;


    private String IsImageAnswer;

    private static String[] categoryArr = new String[]{"English Easy", "English Normal", "English Hard", "Memes", "Games", "English Speech"};

    public Question(){}

    public Question(String question, String a, String b, String c, String d, String correctAnswer, String categoryId, String isImageQuestion) {
        Question = question;
        A = a;
        B = b;
        C = c;
        D = d;
        CorrectAnswer = correctAnswer;
        this.CategoryId = categoryId;
        this.IsImageQuestion = isImageQuestion;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.CategoryId = categoryId;
    }

    public String getIsImageQuestion() {
        return IsImageQuestion;
    }

    public void setIsImageQuestion(String isImageQuestion) {
        this.IsImageQuestion = isImageQuestion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getCategoryNameById(String categoryId) {
        int realId = Integer.parseInt(categoryId.substring(1)) - 1;
        return categoryArr[realId];
    }

    public String getIsImageAnswer() {
        return IsImageAnswer;
    }

    public void setIsImageAnswer(String isImageAnswer) {
        IsImageAnswer = isImageAnswer;
    }

    public String getAnswerByLetter(String letter) {
        String result = null;
        switch (letter) {
            case "a":
                result = getA();
                break;
            case "b":
                result = getB();
                break;
            case "c":
                result = getC();
                break;
            case "d":
                result = getD();
                break;
        }
        return result;
    }

    public static ArrayList<String> getDefaultAnswerOrder() {
        ArrayList<String> result = new ArrayList<>();
        result.add("a");
        result.add("b");
        result.add("c");
        result.add("d");
        return result;
    }

    public static int getLetterIndex(String letter) {
        int index = getDefaultAnswerOrder().indexOf(letter);
        return index;
    }


    public static String getLetter(int index) {
        return getDefaultAnswerOrder().get(index);
    }

    public static ArrayList<String> genRandomAnswerOrder() {
        ArrayList<String> result = getDefaultAnswerOrder();
        Collections.shuffle(result);
        return result;
    }

    public String getIsSpeechQuestion() {
        return IsSpeechQuestion;
    }

    public void setIsSpeechQuestion(String isSpeechQuestion) {
        IsSpeechQuestion = isSpeechQuestion;
    }
}
