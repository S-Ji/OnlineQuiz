package com.example.onlinequiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Common.ModelTag;
import com.example.onlinequiz.Database.QuestionModel;
import com.example.onlinequiz.Interface.ICallback;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.Model.TestManager;

import java.util.ArrayList;
import java.util.List;

public class Start extends Activity implements ICallback<Question> {
    Button btnPlay;
    RadioGroup radioGroupQuestionQty;

    int questionQty = 5;
    boolean isDataLoaded = false;
    boolean isStartClicked = false;

    QuestionModel questionModel;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        questionModel = new QuestionModel(this);
        mapping();
        loadQuestion(Common.categoryId);
        initEvents();
        initInternetStatusFragment();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    private void initEvents() {
        onBtnPlayClicked();
        onQuestionQtyRadioChecked();
    }

    private void onQuestionQtyRadioChecked() {
        radioGroupQuestionQty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio5:
                        questionQty = 5;
                        break;
                    case R.id.radio10:
                        questionQty = 10;
                        break;
                    case R.id.radio15:
                        questionQty = 15;
                        break;
                }
            }
        });
    }

    private void onBtnPlayClicked() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartClicked = true;
                if (isDataLoaded) play();
            }
        });
    }

    private void setTestQuestionQty() {
        Common.testQuestionQty = (questionQty <= Common.questionsList.size()) ? questionQty : Common.questionsList.size();
    }

    private void mapping() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        radioGroupQuestionQty = (RadioGroup) findViewById(R.id.radioGroupQuestionQty);
    }

    private void loadQuestion(String categoryId) {
        String categoryName = Question.getCategoryNameById(categoryId);
        if (categoryName.equals("English Speech")) {
            questionModel.listSpeechEnglish(ModelTag.listQuestionForTest);
        } else {
            questionModel.listItemsByCategoryId(categoryId, ModelTag.listQuestionForTest);
        }
    }

    // CALLBACK
    @Override
    public void itemCallBack(Question item, String tag) {
    }

    @Override
    public void listCallBack(ArrayList<Question> items, String tag) {
        if (tag.equals(ModelTag.listQuestionForTest))
            onTestQuestionsCallback(items);
    }

    private void onTestQuestionsCallback(ArrayList<Question> questionArrayList) {
        Common.questionsList.clear();
        Common.questionsList.addAll(questionArrayList);
        Common.shuffleQuestionList();
        isDataLoaded = true;
        if (isStartClicked) play();
    }

    private ArrayList<Question> getDiffQuestionsWthPrevTest(ArrayList<Question> questionArrayList) {
        ArrayList<Question> result = null;
        TestManager testManager = Common.getCurrentUser().getTestManager();
        if (testManager.getTestArrayList().size() > 0) {
            Test latestTest = testManager.getLatestTest();
            if (questionArrayList.size() >= (latestTest.getQuestions().size() * 2)) {
                result = new ArrayList<>();
                ArrayList<String> ids = getIdsByQuestionInTestArrayList(latestTest.getQuestions());
                for (Question q : questionArrayList) {
                    if (ids.indexOf(q.getId()) < 0) result.add(q);
                    if (result.size() >= Common.testQuestionQty) break;
                }
            }
        }
        return result;
    }

    private ArrayList<String> getIdsByQuestionInTestArrayList(ArrayList<QuestionInTest> questionArrayList) {
        ArrayList<String> idArrayList = new ArrayList<>();
        for (QuestionInTest q : questionArrayList) {
            idArrayList.add(q.getQuestionId());
        }
        return idArrayList;
    }

    private void play() {
        setTestQuestionQty();
        ArrayList<Question> diffQuestions = getDiffQuestionsWthPrevTest((ArrayList<Question>) Common.questionsList);
        if (diffQuestions != null) {
            Common.questionsList.clear();
            Common.questionsList.addAll(diffQuestions);
        }

        MediaPlayer mp = MediaPlayer.create(this, R.raw.crash);
        Intent intent = new Intent(Start.this, Playing.class);
        startActivity(intent);
        mp.start();
        finish();
    }
}