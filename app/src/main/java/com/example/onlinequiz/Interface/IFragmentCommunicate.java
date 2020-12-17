package com.example.onlinequiz.Interface;

import com.google.gson.JsonObject;

import java.util.HashMap;

public interface IFragmentCommunicate {
    public void communicate(String value, String tag);

    public void communicate(JsonObject jsonObject, String tag);
}
