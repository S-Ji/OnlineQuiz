package com.example.onlinequiz.Model;

import android.util.Log;

import com.example.onlinequiz.Common.Helper;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Test {
    private String categoryId;
    private ArrayList<QuestionInTest> questionInTestArrayList = new ArrayList<>();
    private int score;
    private int numberOfQuestion;
    private String date;

    public Test(){ }

    public Test(JSONObject jsonObject){
        try {
            setCategoryId(jsonObject.getString("CategoryId"));
            setScore(jsonObject.getInt("Score"));
            setNumberOfQuestion(jsonObject.getInt("NumberOfQuestion"));
            setDate(jsonObject.getString("Date"));
            JSONArray questionJsonArray =  jsonObject.getJSONArray("Questions");
            for (int i =0; i<questionJsonArray.length(); i++){
                JSONObject questionObject = questionJsonArray.getJSONObject(i);
                QuestionInTest questionInTest = new QuestionInTest(questionObject);
                questionInTestArrayList.add(questionInTest);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Test(String categoryId, ArrayList<QuestionInTest> questionInTestArrayList, int score, int numberOfQuestion, String date) {
        this.setCategoryId(categoryId);
        this.setQuestions(questionInTestArrayList);
        this.setScore(score);
        this.setNumberOfQuestion(numberOfQuestion);
        this.setDate(date);
    }

    public ArrayList<QuestionInTest> getQuestions() {
        return questionInTestArrayList;
    }

    public void setQuestions(ArrayList<QuestionInTest> questionInTestArrayList) {
        this.questionInTestArrayList = questionInTestArrayList;
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
        this.questionInTestArrayList.add(question);
    }

    public String getInfo(){
        String result = categoryId+"; "+numberOfQuestion+"; "+score+"; "+date+"\n";
        for (QuestionInTest questionInTest:questionInTestArrayList){
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
            for (QuestionInTest questionInTest:questionInTestArrayList){
                jsonArray.put(questionInTest.getJsonObject());
            }
            obj.put("Questions", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void loadQuestions(){
        Log.d("xxx", "load questions called");
        for (QuestionInTest question:questionInTestArrayList){
            question.loadQuestion();
        }
    }

}
