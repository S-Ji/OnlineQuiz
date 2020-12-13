package com.example.onlinequiz.Model;

import com.example.onlinequiz.Common.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Test {
    private String categoryId;
    private ArrayList<QuestionInTest> questions;
    private int score;
    private int numberOfQuestion;
    private String date;

    public Test(){
        questions = new ArrayList<>();
    }

    public Test(String categoryId, ArrayList<QuestionInTest> questions, int score, int numberOfQuestion, String date) {
        this.setCategoryId(categoryId);
        this.setQuestions(questions);
        this.setScore(score);
        this.setNumberOfQuestion(numberOfQuestion);
        this.setDate(date);
    }

    public ArrayList<QuestionInTest> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionInTest> questions) {
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

    public void addQuestion(QuestionInTest question){
        this.questions.add(question);
    }


    public String getInfo(){
        String result = categoryId+"; "+numberOfQuestion+"; "+score+"; "+date+"\n";
        for (QuestionInTest questionInTest:questions){
            result+=questionInTest.getInfo()+"\n";
        }
        return result;
    }

    public JSONObject getJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("CategoryId", getCategoryId());
            obj.put("NumberOfQuestion", getNumberOfQuestion());
            obj.put("Score", getScore());
            obj.put("Date", getDate());
            JSONArray jsonArray = new JSONArray();
            for (QuestionInTest questionInTest:questions){
                jsonArray.put(questionInTest.getJsonObject());
            }
            obj.put("Questions", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
