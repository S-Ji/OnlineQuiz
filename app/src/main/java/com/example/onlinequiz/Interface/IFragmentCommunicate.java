package com.example.onlinequiz.Interface;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

public interface IFragmentCommunicate {
    public void communicate(String value, String tag);

    public void communicate(JSONObject jsonObject, String tag);
}
