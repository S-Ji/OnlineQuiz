package com.example.onlinequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.onlinequiz.Adapter.Common.TestAdapter;
import com.example.onlinequiz.Common.Commom;
import com.example.onlinequiz.Model.Test;
import com.example.onlinequiz.ViewHolder.Activity;

import java.util.ArrayList;

public class MyTestActivity extends Activity {

    ListView lvTest;
    TestAdapter testAdapter;
    ArrayList<Test> testArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        mapping();
        initListView();
    }

    private void initListView() {
        testArrayList = Commom.getCurrentUser().getTestManager().getTestArrayList();
        testAdapter = new TestAdapter(this, R.layout.test_item_layout, testArrayList);
        lvTest.setAdapter(testAdapter);
        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyTestActivity.this, TestDetailActivity.class);
                i.putExtra("testIndex", position);
                startActivity(i);
            }
        });
    }

    private void mapping(){
        lvTest = (ListView)findViewById(R.id.lvTest);
    }
}