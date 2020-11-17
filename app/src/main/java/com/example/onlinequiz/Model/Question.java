package com.example.onlinequiz.Model;

public class Question {
    private String Question, A,B,C,D,CorrectAnswer,CategoryId,IsImageQuestion;

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
}
