package com.example.onlinequiz.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomAnswerQuestion {

    private ArrayList<String> randomAnswerOrder;

    public RandomAnswerQuestion() {
        onRandom();
    }

    private void onRandom(){
        setRandomAnswerOrder((ArrayList<String>) getDefaultAnswerOrder().clone());
        Collections.shuffle(randomAnswerOrder);
    }

    public ArrayList<String> getRandomAnswerOrder() {
        return randomAnswerOrder;
    }

    public void setRandomAnswerOrder(ArrayList<String> randomAnswerOrder) {
        this.randomAnswerOrder = randomAnswerOrder;
    }

    public ArrayList<String> getDefaultAnswerOrder() {
        ArrayList<String> result = new ArrayList<>();
        result.add("a");
        result.add("b");
        result.add("c");
        result.add("d");
        return result;
    }

}
