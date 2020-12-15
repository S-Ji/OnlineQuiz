package com.example.onlinequiz.Database;

import com.example.onlinequiz.Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract  class Model {
    private FirebaseDatabase database;
    private DatabaseReference collectionRef;

    protected Activity activity;
    protected  String collection;

    public Model(Activity activity, String collection){
        database = FirebaseDatabase.getInstance();
        setActivity(activity);
        setCollection(collection);
        setCollectionRef(database.getReference(getCollection()));
    }

    public DatabaseReference getCollectionRef() {
        return collectionRef;
    }

    public void setCollectionRef(DatabaseReference collectionRef) {
        this.collectionRef = collectionRef;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
