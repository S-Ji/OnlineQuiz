package com.example.onlinequiz;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Common.Helper;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Database.UserModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.Test;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Playing extends Activity implements View.OnClickListener, ICallback<UserModel> {

    RelativeLayout rltMain, pictureAnswerContainer;
    LinearLayout textAnswerContainer;
    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    ImageView imgA, imgB, imgC, imgD;
    TextView txtScore, txtQuestionNum, question_text;
    MediaPlayer correctAnswerMp3;
    MediaPlayer wrongAnswerMp3;
    final static long INTERVAL = 2000;
    final static long TIMEOUT = 12000;
    int progressValue = 0;
    int index = 0, score = 0, thisQuestion = 0, correctAnswer;

    CountDownTimer mCountDown;

    Test test;
    QuestionInTest questionInTest;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        userModel = new UserModel(this);
        initTest();
        mapping();
        initEvents();
        initInternetStatusFragment();
    }

    private void initTest() {
        test = new Test();
    }

    private void initMp3() {
        correctAnswerMp3 = MediaPlayer.create(this, R.raw.correct);
        wrongAnswerMp3 = MediaPlayer.create(this, R.raw.wrong);
    }

    private void initEvents() {
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

        imgA.setOnClickListener(this);
        imgB.setOnClickListener(this);
        imgC.setOnClickListener(this);
        imgD.setOnClickListener(this);
    }

    // ON ANSWER SELECTED
    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        initMp3();

        if (index < getTotalQuestion()) {
            String userAnswer;
            if (getCurrentQuestion().getIsImageAnswer().equals("true")) {
                ArrayList<Integer> idArrayList = new ArrayList<>();
                idArrayList.add(R.id.imgA);
                idArrayList.add(R.id.imgB);
                idArrayList.add(R.id.imgC);
                idArrayList.add(R.id.imgD);
                int index = idArrayList.indexOf(v.getId());
                String letter = questionInTest.getAnswerOrder().get(index);
                userAnswer = getCurrentQuestion().getAnswerByLetter(letter);
            } else {
                Button clickedButton = (Button) v;
                userAnswer = clickedButton.getText().toString();
            }
            questionInTest.setUserAnswer(userAnswer);
            test.addQuestion(questionInTest);
            if (isCorrectAnswer(userAnswer)) onUserSelectCorrectAnswer();
            else onUserSelectWrongAnswer();
            //

            showQuestion(++index);
            displayScore();
        } else onDone();
    }

    private void onUserSelectCorrectAnswer() {
        wrongAnswerMp3.pause();
        correctAnswerMp3.start();
        score += 10;
        correctAnswer++;
    }

    private void onUserSelectWrongAnswer() {
        correctAnswerMp3.pause();
        wrongAnswerMp3.start();
    }

    private void displayScore() {
        txtScore.setText(String.format("%d", score));
    }

    private boolean isCorrectAnswer(String answer) {
        return (answer.trim().equals(getCurrentQuestion().getCorrectAnswer().trim()));
    }

    // SHOW QUESTION
    private void showQuestion(int index) {
        if (index < getTotalQuestion()) {
            thisQuestion++;
            displayQuestionNum();
            resetProgress();
            displayQuestion();

            // RANDOM ANSWER
            displayAnswer();
            //
            mCountDown.start();
        } else onDone();
    }

    private void displayAnswer() {
        ArrayList<String> answerOrder = Question.genRandomAnswerOrder();
        showAnswerContainerVisible();
        if (getCurrentQuestion().getIsImageAnswer().equals("true")) {
            // display picture answer
            displayPictureAnswer(answerOrder);

        } else {
            // display text answer
            displayTextAnswer(answerOrder);
        }

        questionInTest = new QuestionInTest();
        questionInTest.setQuestionId(getCurrentQuestion().getId());
        questionInTest.setAnswerOrder(answerOrder);
    }

    private void showAnswerContainerVisible() {
        if (getCurrentQuestion().getIsImageAnswer().equals("true")) {
            pictureAnswerContainer.setVisibility(View.VISIBLE);
            textAnswerContainer.setVisibility(View.GONE);
        } else {
            textAnswerContainer.setVisibility(View.VISIBLE);
            pictureAnswerContainer.setVisibility(View.GONE);
        }
    }

    // TEXT ANSWER
    private void displayTextAnswer(ArrayList<String> answerOrder) {
        displayTextAnswerByLetter(answerOrder.get(0), getCurrentQuestion().getA());
        displayTextAnswerByLetter(answerOrder.get(1), getCurrentQuestion().getB());
        displayTextAnswerByLetter(answerOrder.get(2), getCurrentQuestion().getC());
        displayTextAnswerByLetter(answerOrder.get(3), getCurrentQuestion().getD());
    }

    private Button getButtonByLetter(String letter) {
        Button result = null;
        Button[] buttonArr = new Button[]{btnA, btnB, btnC, btnD};
        int index = Question.getLetterIndex(letter);
        if (index >= 0) result = buttonArr[index];
        return result;
    }

    private void displayTextAnswerByLetter(String letter, String answer) {
        Button btn = getButtonByLetter(letter);
        if (btn != null) btn.setText(answer);
    }

    private void displayQuestionNum() {
        txtQuestionNum.setText(String.format("%d / %d", thisQuestion, getTotalQuestion()));
    }

    // PICTURE ANSWER
    private void displayPictureAnswer(ArrayList<String> answerOrder) {
        displayPictureAnswerByLetter(answerOrder.get(0), getCurrentQuestion().getA());
        displayPictureAnswerByLetter(answerOrder.get(1), getCurrentQuestion().getB());
        displayPictureAnswerByLetter(answerOrder.get(2), getCurrentQuestion().getC());
        displayPictureAnswerByLetter(answerOrder.get(3), getCurrentQuestion().getD());
    }

    private void displayPictureAnswerByLetter(String letter, String answer) {
        ImageView img = getImageViewByLetter(letter);
        if (img != null) {
            Picasso.with(getBaseContext())
                    .load(answer)
                    .into(img);
        }
    }

    private ImageView getImageViewByLetter(String letter) {
        ImageView result = null;
        ImageView[] imageViewArr = new ImageView[]{imgA, imgB, imgC, imgD};
        int index = Question.getLetterIndex(letter);
        if (index >= 0) result = imageViewArr[index];
        return result;
    }


    private void resetProgress() {
        progressBar.setProgress(0);
        progressValue = 0;
    }

    private void displayQuestion() {
        if (getCurrentQuestion().getIsImageQuestion().equals("true")) {
            //if question img
            Picasso.with(getBaseContext())
                    .load(getCurrentQuestion().getQuestion())
                    .into(question_image);
            question_image.setVisibility(View.VISIBLE);
            question_text.setVisibility(View.INVISIBLE);
        } else {
            //if question text, set img visible
            question_text.setText(getCurrentQuestion().getQuestion());
            question_image.setVisibility(View.INVISIBLE);
            question_text.setVisibility(View.VISIBLE);
        }
    }

    private void onDone() {
        test.setCategoryId(Commom.categoryId);
        test.setNumberOfQuestion(getTotalQuestion());
        test.setScore(score);
        test.setDate(Helper.getCurrentISODateString());
        Commom.getCurrentUser().getTestManager().add(test);
        Commom.questionsList.clear();
        userModel.updateCurrentUserTests(ModelTag.updateCurrentUserTests);

        Intent intent = new Intent(this, Done.class);
        Bundle dataSend = new Bundle();
        dataSend.putInt("SCORE", score);
        dataSend.putInt("TOTAL", getTotalQuestion());
        dataSend.putInt("CORRECTED", correctAnswer);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startTest();
    }

    private void startTest() {
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }

    private void mapping() {
        pictureAnswerContainer = (RelativeLayout) findViewById(R.id.pictureAnswerContainer);
        textAnswerContainer = (LinearLayout) findViewById(R.id.textAnswerContainer);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestionNum = (TextView) findViewById(R.id.txtTotalQuestion);
        question_text = (TextView) findViewById(R.id.question_text);
        question_image = (ImageView) findViewById(R.id.question_image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rltMain = (RelativeLayout) findViewById(R.id.rltMain);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);

        imgA = (ImageView) findViewById(R.id.imgA);
        imgB = (ImageView) findViewById(R.id.imgB);
        imgC = (ImageView) findViewById(R.id.imgC);
        imgD = (ImageView) findViewById(R.id.imgD);
    }

    private int getTotalQuestion() {
        return Commom.testQuestionQty;
    }

    // SUPPORTED METHOD
    private Question getCurrentQuestion() {
        //return Commom.questionsList.get(index);
        return Commom.questionsList.get(index);
    }

    @Override
    public void itemCallBack(UserModel item, String tag) {
    }

    @Override
    public void listCallBack(ArrayList<UserModel> items, String tag) {
    }
}
