package com.example.onlinequiz.Model;

import android.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userName;
    private String passWord;
    private String email;
    private TestManager testManager = new TestManager();

    public User() {
    }

    public User(String userName, String passWord, String email) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TestManager getTestManager() {
        return testManager;
    }

    public void setTestManager(TestManager testManager) {
        this.testManager = testManager;
    }

    public void setTestManagerByJsonString(String jsonString){
        try {
            String str = jsonString.substring(1,jsonString.length()-1);
            JSONArray jsonArray = new JSONArray(str);
            setTestManager(new TestManager(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getDocData() {
        HashMap<String, Object> docData = new HashMap<>();
        docData.put("email", getEmail());
        docData.put("passWord", getPassWord());
        docData.put("tests", getTestManager().getSavingJsonString());
        docData.put("userName", getUserName());
        return docData;
    }
}
