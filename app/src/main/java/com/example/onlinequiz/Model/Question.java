package com.example.onlinequiz.Model;

import com.example.onlinequiz.Common.Helper;
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
    private String IsAudioQuestion;
    private int TimeLimit;


    private String IsVoiceAnswer;
    private String IsImageAnswer;

    private static String[] categoryArr = new String[]{"English Easy", "English Normal", "English Hard", "Memes", "Games", "English Speech"};

    public Question() {
    }

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

    public String getIsVoiceAnswer() {
        return IsVoiceAnswer;
    }

    public void setIsVoiceAnswer(String isVoiceAnswer) {
        IsVoiceAnswer = isVoiceAnswer;
    }

    // GETTER & SETTER
    public String getIsAudioQuestion() {
        return IsAudioQuestion;
    }

    public void setIsAudioQuestion(String isAudioQuestion) {
        IsAudioQuestion = isAudioQuestion;
    }

    public int getTimeLimit() {
        return TimeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        TimeLimit = timeLimit;
    }

    public static Question getQuestionByDataSnapshot(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            String id = dataSnapshot.getKey();
            String question = dataSnapshot.child("Question").getValue().toString();
            String correctAnswer = dataSnapshot.child("CorrectAnswer").getValue().toString();
            String answerA = dataSnapshot.child("A").getValue().toString();
            String answerB = dataSnapshot.child("B").getValue().toString();
            String answerC = dataSnapshot.child("C").getValue().toString();
            String answerD = dataSnapshot.child("D").getValue().toString();
            String isImageQuestion = Helper.getStringByDataSnapshot(dataSnapshot, "IsImageQuestion", "false");
            String isImageAnswer = Helper.getStringByDataSnapshot(dataSnapshot, "IsImageAnswer", "false");
            String isVoiceAnswer = Helper.getStringByDataSnapshot(dataSnapshot, "IsVoiceAnswer", "false");
            String isAudioQuestion = Helper.getStringByDataSnapshot(dataSnapshot, "IsAudioQuestion", "false");
            int timeLimit = Helper.getIntByDataSnapshot(dataSnapshot, "TimeLimit", 15);
            String categoryId = dataSnapshot.child("CategoryId").getValue().toString();
            Question qt = new Question(question, answerA, answerB, answerC, answerD, correctAnswer, categoryId, isImageQuestion);
            qt.setIsImageAnswer(isImageAnswer);
            qt.setIsVoiceAnswer(isVoiceAnswer);
            qt.setIsAudioQuestion(isAudioQuestion);
            qt.setTimeLimit(timeLimit);
            qt.setId(id);
            return qt;
        }
        return null;
    }


    public String getQuestionType() {
        String result;
        if (getIsImageQuestion().equals("true")) result = "picture";
        else if (getIsAudioQuestion().equals("true")) result = "audio";
        else result = "text";
        return result;
    }

    public String getAnswerType() {
        String result;
        if (getIsImageAnswer().equals("true")) result = "picture";
        else if (getIsVoiceAnswer().equals("true")) result = "voice";
        else result = "text";
        return result;
    }

}
