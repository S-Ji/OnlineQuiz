package com.example.onlinequiz.Adapter.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.onlinequiz.Common.Helper;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
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


    class ViewHolder{
        TextView txtQuestion, txtA, txtB, txtC, txtD;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestQuestionAdapter.ViewHolder holder;
        if (convertView == null){
            holder = new TestQuestionAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            holder.txtQuestion  = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.txtA  = (TextView) convertView.findViewById(R.id.txtA);
            holder.txtB  = (TextView) convertView.findViewById(R.id.txtB);
            holder.txtC  = (TextView) convertView.findViewById(R.id.txtC);
            holder.txtD  = (TextView) convertView.findViewById(R.id.txtD);
            convertView.setTag(holder);
        }else{
            holder = (TestQuestionAdapter.ViewHolder) convertView.getTag();
        }

        QuestionInTest questionInTest = questionInTestArrayList.get(position);
        int no = position+1;
        String question = no+". What is the part of human body?";
        holder.txtQuestion.setText(question);
        holder.txtA.setText("A. Plane");
        holder.txtB.setText("B. Hand");
        holder.txtC.setText("C. Chess");
        holder.txtD.setText("D. Subject");
        /*
        Animation animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale_for_listview_item);
        convertView.startAnimation(animScale);
        */
        return convertView;
    }

}
