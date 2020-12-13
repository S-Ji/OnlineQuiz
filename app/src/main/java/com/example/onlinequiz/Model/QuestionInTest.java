package com.example.onlinequiz.Model;

public class QuestionInTest {
    private String questionId;
    private String[] answerOrder;
    private String userAnswer;
    private Question question;

    public QuestionInTest(String questionId, String answerOrder, String userAnswer){
        this.setQuestionId(questionId);
        this.setAnswerOrder(answerOrder.split("\\|"));
        this.setUserAnswer(userAnswer);
    }

    private void loadQuestion(){
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String[] getAnswerOrder() {
        return answerOrder;
    }

    public void setAnswerOrder(String[] answerOrder) {
        this.answerOrder = answerOrder;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

}
