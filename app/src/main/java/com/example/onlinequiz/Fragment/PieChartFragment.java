package com.example.onlinequiz.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.onlinequiz.Common.Common;
import com.example.onlinequiz.Interface.IFragmentCommunicate;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends Fragment {

    public static String fragmentTag = "pie-chart-fragment";
    View v;
    AnyChartView anyChartView;
    TextView txtLoading;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        mapping();
        anyChartView.setVisibility(View.GONE);
        txtLoading.setVisibility(View.VISIBLE);

        ArrayList<Test> testArrayList = Common.getCurrentUser().getTestManager().getTestArrayList();
        int correctCount = 0;
        int incorrectCount = 0;
        for (int i = 0; i < testArrayList.size(); i++) {
            Test test = testArrayList.get(i);
            int correctInOneTest = test.getScore() / 10;
            int incorrectInOneTest = test.getNumberOfQuestion() - correctInOneTest;
            correctCount += correctInOneTest;
            incorrectCount += incorrectInOneTest;
        }
        showPieChart(incorrectCount, correctCount, testArrayList.size());
        return v;
    }

    private void showPieChart(int incorrectCount, int correctCount, int totalTest) {
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Correct answer", correctCount));
        data.add(new ValueDataEntry("Incorrect answer", incorrectCount));
        anyChartView.setChart(pie);
        pie.data(data);
        anyChartView.setVisibility(View.VISIBLE);
        txtLoading.setVisibility(View.GONE);
    }

    private void mapping() {
        txtLoading = (TextView) v.findViewById(R.id.txtLoading);
        anyChartView = (AnyChartView) v.findViewById(R.id.any_chart_view);
    }
}