package com.example.onlinequiz.Model;

import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TestManager {
    private int max = 30;
    private ArrayList<Test> testArrayList = new ArrayList<>();

    public TestManager(JSONArray jsonArray) {
        int length = (jsonArray.length() < max) ? jsonArray.length() : max;
        for (int i = 0; i < length; i++) {
            // Test
            try {
                JSONObject testObject = jsonArray.getJSONObject(i);
                Test test = new Test(testObject);
                testArrayList.add(test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public TestManager() {
    }

    public void sortByDate() {
        Collections.sort(testArrayList, new Comparator<Test>() {
            @Override
            public int compare(Test a, Test b) {
                return -(a.getDate().compareTo(b.getDate()));
            }
        });
    }

    public void sortByScore() {
        Collections.sort(testArrayList, new Comparator<Test>() {
            @Override
            public int compare(Test a, Test b) {
                return -(a.getScore() - b.getScore());
            }
        });
    }

    public void add(Test test) {
        testArrayList.add(0, test);
        if (testArrayList.size() > max) remove(max);
    }

    public void remove(int index) {
        this.testArrayList.remove(index);
    }

    public JSONArray getJsonArray() {
        JSONArray jsonArray = new JSONArray();
        for (Test test : testArrayList) {
            jsonArray.put(test.getJsonObject());
        }
        return jsonArray;
    }

    public String getSavingJsonString() {
        String jsonString = "\"" + getJsonArray().toString() + "\"";
        return jsonString;
    }

    public ArrayList<Test> getTestArrayList() {
        return testArrayList;
    }

    public static ArrayList<String> getSortArrayList() {
        ArrayList<String> sortArrayList = new ArrayList<>();
        sortArrayList.add("Highest test score");
        sortArrayList.add("Latest test");
        return sortArrayList;
    }
}
