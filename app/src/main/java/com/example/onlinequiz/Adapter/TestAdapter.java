package com.example.onlinequiz.Adapter;

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
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.R;

import java.util.ArrayList;

public class TestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Test> testArrayList;

    public TestAdapter(Context context, int layout, ArrayList<Test> testArrayList) {
        this.context = context;
        this.layout = layout;
        this.testArrayList = testArrayList;
    }

    @Override
    public int getCount() {
        return testArrayList.size();
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
        TextView txtCategory, txtScore, txtTotalScore, txtDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new TestAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            holder.txtCategory = (TextView) convertView.findViewById(R.id.txtCategory);
            holder.txtScore = (TextView) convertView.findViewById(R.id.txtScore);
            holder.txtTotalScore = (TextView) convertView.findViewById(R.id.txtTotalScore);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            convertView.setTag(holder);
        } else holder = (TestAdapter.ViewHolder) convertView.getTag();
        Test test = testArrayList.get(position);

        //
        String score = test.getScore() + "";
        String totalScore = "/" + (test.getNumberOfQuestion() * 10);
        String category = Question.getCategoryNameById(test.getCategoryId());
        String date = Helper.getTestDate(Helper.convertISODateStringToDate(test.getDate()));

        //
        holder.txtCategory.setText(category);
        holder.txtScore.setText(score);
        holder.txtTotalScore.setText(totalScore);
        holder.txtDate.setText(date);

        //
        Animation animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale_for_listview_item);
        convertView.startAnimation(animScale);
        return convertView;
    }
}
