package com.example.onlinequiz.Model;

public class QuestionInTestManager {
    private QuestionInTest[] questionInTestArr;

    public QuestionInTestManager() {
    }

    public QuestionInTestManager(QuestionInTest[] questionInTestArr) {
        setQuestionInTestArr(questionInTestArr);
    }

    public QuestionInTest[] getQuestionInTestArr() {
        return questionInTestArr;
    }

    public void setQuestionInTestArr(QuestionInTest[] questionInTestArr) {
        this.questionInTestArr = questionInTestArr;
    }

}
