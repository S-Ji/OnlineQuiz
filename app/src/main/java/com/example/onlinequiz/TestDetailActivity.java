package com.example.onlinequiz;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.onlinequiz.Adapter.TestQuestionAdapter;
import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Common.Message;

import java.util.Locale;

public class TestDetailActivity extends Activity {

    ListView lvQuestion;
    TestQuestionAdapter questionAdapter;
    TextToSpeech tts;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        initTextToSpeech();
        checkValidTest();
        mapping();
        initBtnBackClick();
        initListView();
        initInternetStatusFragment();
    }

    private void initBtnBackClick(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(TestDetailActivity.this, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initListView() {
        questionAdapter = new TestQuestionAdapter(this, R.layout.test_question_item_layout, Common.getTest().getQuestions(), tts);
        lvQuestion.setAdapter(questionAdapter);
    }

    private void checkValidTest() {
        boolean isValidTest = false;
        if (Common.getTest() != null) {
            if (Common.getTest().getQuestions().size() > 0) isValidTest = true;
        }
        if (!isValidTest) exitAndToastErrMessage();
    }

    private void mapping() {
        lvQuestion = (ListView) findViewById(R.id.lvQuestion);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void exitAndToastErrMessage() {
        finish();
        Toast.makeText(this, Message.failedToDisplayTestDetail, Toast.LENGTH_SHORT).show();
    }


}