package com.example.onlinequiz.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionInTest {
    private String questionId;
    private ArrayList<String> answerOrder;
    private String userAnswer;
    private Question question;

    public QuestionInTest(){}

    public QuestionInTest(String questionId, String answerOrder, String userAnswer){
        this.setQuestionId(questionId);
        //this.setAnswerOrder(answerOrder.split("\\|"));
        this.setUserAnswer(userAnswer);
        this.loadQuestion();
    }

    private void loadQuestion(){
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public ArrayList<String> getAnswerOrder() {
        return answerOrder;
    }

    public void setAnswerOrder(ArrayList<String> answerOrder) {
        this.answerOrder = answerOrder;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getInfo(){
        return questionId+"; "+answerOrder+"; "+userAnswer;
    }

    public JSONObject getJsonObject(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("questionId", questionId);
            obj.put("answerOrder", getSavingAnswerOrder());
            obj.put("userAnswer", getUserAnswer());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private String getSavingAnswerOrder(){
        String divider = "|";
        String result = answerOrder.get(0)+divider+answerOrder.get(1)+divider+answerOrder.get(2)+divider+answerOrder.get(3);
        return result;
    }

}
