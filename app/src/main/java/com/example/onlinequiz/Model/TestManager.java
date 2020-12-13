package com.example.onlinequiz.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TestManager {
    private int max = 5;

    private ArrayList<Test> testArrayList;

    public TestManager(){
        testArrayList = new ArrayList<>();
    }

    public void sort(){
        Collections.sort(testArrayList, new Comparator<Test>() {
            @Override
            public int compare(Test a, Test b) {
                return -(a.getDate().compareTo(b.getDate()));
            }
        });
    }

    public void add(Test test){
        testArrayList.add(test);
        if (testArrayList.size() > max) remove(0);
    }

    public void remove(int index){
        this.testArrayList.remove(index);
    }

    public JSONArray getJsonArray(){
        JSONArray jsonArray = new JSONArray();
        for(Test test: testArrayList){
            jsonArray.put(test.getJsonObject());
        }
        return jsonArray;
    }

    public String getSavingJsonString(){
        String jsonString = "\""+getJsonArray().toString()+"\"";
        return jsonString;
    }

    public ArrayList<Test> getTestArrayList() {
        return testArrayList;
    }

    public void setTestArrayList(ArrayList<Test> testArrayList) {
        this.testArrayList = testArrayList;
    }

    public void setDataByJsonArray(JSONArray jsonArray){
        /*
        for (int i =0; i< jsonArray.length(); i++){
            // Test
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Test test = new Test
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
         */
    }

}
