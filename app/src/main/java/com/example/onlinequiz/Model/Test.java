package com.example.onlinequiz.Model;

import java.util.Date;

public class Test {
    private String categoryId;
    private Question[] questions;
    private int score;
    private int numberOfQuestion;
    private String date;

    public Test(){}
    public Test(String categoryId, Question[] questions, int score, int numberOfQuestion, String date) {
        this.setCategoryId(categoryId);
        this.setQuestions(questions);
        this.setScore(score);
        this.setNumberOfQuestion(numberOfQuestion);
        this.setDate(date);
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(int numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
