package com.example.onlinequiz.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.onlinequiz.Interface.IFragmentCommunicate;
import com.example.onlinequiz.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceFragment extends MyFragment {

    View v;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;
    private IFragmentCommunicate communicate;
    public static String fragmentTag = "voice-fragment";
    boolean isMicOn = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_voice, container, false);
        communicate = (IFragmentCommunicate) getActivity();

        //
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        editText = v.findViewById(R.id.edtVoice);
        micButton = v.findViewById(R.id.button);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());


        initOnMicButtonTouched();
        initRecognitionListener();
        hideVoiceEdt();
        return v;
    }

    private void initOnMicButtonTouched() {
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMicOn == true) {
                    //onStopListening();
                } else {
                    Log.d("xxx", "enable mic "+isMicOn);
                    micButton.setImageResource(R.drawable.blue_mic);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
            }
        });
    }

    public void onStopListening() {
        speechRecognizer.stopListening();
    }

    private void initRecognitionListener() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("xxx", "on ready");
                isMicOn = true;
            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }


            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
                Log.d("xxx", "error");
                onSendResult("");
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d("xxx", "on result called");
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String text = data.get(0);
                onSendResult(text);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void reset() {
        micButton.setImageResource(R.drawable.black_mic);
        isMicOn = false;
    }

    private void onSendResult(String value) {
        Log.d("xxx", "on send result called");
        micButton.setImageResource(R.drawable.black_mic);
        editText.setText(value);
        communicate.communicate(value, VoiceFragment.fragmentTag);
        reset();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    public void hideVoiceEdt() {
        editText.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            Log.d("xxx", "play called");
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", getActivity().getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer.reset();
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            Log.d("xxx", "play err: " + s);
            ex.printStackTrace();
        }
    }
}