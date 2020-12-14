package com.example.onlinequiz.Adapter.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.onlinequiz.Common.Helper;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.Model.RandomAnswerQuestion;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.R;

import java.util.ArrayList;

public class TestQuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QuestionInTest> questionInTestArrayList;

    public TestQuestionAdapter(Context context, int layout, ArrayList<QuestionInTest> questionInTestArrayList) {
        this.context = context;
        this.layout = layout;
        this.questionInTestArrayList = questionInTestArrayList;
    }

    @Override
    public int getCount() {
        return questionInTestArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder {
        TextView txtQuestion, txtA, txtB, txtC, txtD;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestQuestionAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new TestQuestionAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.txtA = (TextView) convertView.findViewById(R.id.txtA);
            holder.txtB = (TextView) convertView.findViewById(R.id.txtB);
            holder.txtC = (TextView) convertView.findViewById(R.id.txtC);
            holder.txtD = (TextView) convertView.findViewById(R.id.txtD);
            convertView.setTag(holder);
        } else {
            holder = (TestQuestionAdapter.ViewHolder) convertView.getTag();
        }

        QuestionInTest questionInTest = questionInTestArrayList.get(position);
        int no = position + 1;
        String question = no + ". " + questionInTest.getQuestion().getQuestion();
        holder.txtQuestion.setText(question);
        displayAnswer(questionInTest, holder);
        /*
        Animation animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale_for_listview_item);
        convertView.startAnimation(animScale);
        */
        return convertView;
    }

    private void displayAnswer(QuestionInTest questionInTest, ViewHolder holder) {
        ArrayList<String> answerOrder = questionInTest.getAnswerOrder();
        displayAnswerByLetter(answerOrder.get(0), questionInTest, holder);
        displayAnswerByLetter(answerOrder.get(1), questionInTest, holder);
        displayAnswerByLetter(answerOrder.get(2), questionInTest, holder);
        displayAnswerByLetter(answerOrder.get(3), questionInTest, holder);
    }

    @SuppressLint("ResourceAsColor")
    private void displayAnswerByLetter(String letter, QuestionInTest questionInTest, ViewHolder holder) {
        Question question = questionInTest.getQuestion();
        TextView txt = null;
        String answer = null;
        switch (letter) {
            case "a":
                txt = holder.txtA;
                answer = question.getA();
                break;
            case "b":
                txt = holder.txtB;
                answer = question.getB();
                break;
            case "c":
                txt = holder.txtC;
                answer = question.getC();
                break;
            case "d":
                txt = holder.txtD;
                answer = question.getD();
                break;
        }
        if (txt != null) {
            if ((question.getCorrectAnswer().equals(questionInTest.getUserAnswer())) && (question.getCorrectAnswer().equals(answer))) {
                // choose correct answer
                txt.setBackground(ContextCompat.getDrawable(context, R.drawable.choose_correct_test_quetsion_answer_button));
                txt.setTextColor(Color.WHITE);
            } else if (questionInTest.getUserAnswer().equals(answer)) {
                // choose wrong answer
                txt.setBackground(ContextCompat.getDrawable(context, R.drawable.choose_wrong_test_quetsion_answer_button));
                txt.setTextColor(Color.WHITE);
            } else {
                // default
                Log.d("xxx", "default answer: " + answer);
                txt.setBackground(ContextCompat.getDrawable(context, R.drawable.test_quetsion_answer_button));
                txt.setTextColor(Color.parseColor("#757575"));
            }
            txt.setText(answer);
        }
    }

}
