package edu.calpoly.tzli.seek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class HistoryActivity extends AppCompatActivity {
    protected Firebase myFirebaseRef;
    protected ListView friend_history;
    protected ArrayAdapter<String> valuesAdapter;
    protected ArrayList<String> displayArray;
    protected String personalName;
    protected String personalId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalName = getIntent().getExtras().getString("personalName");
        personalId = getIntent().getExtras().getString("personalId");

        myFirebaseRef = new Firebase("https://boiling-heat-1137.firebaseIO.com/" + personalId);
        friend_history = (ListView) findViewById(R.id.friend_history);
//        displayArray = new ArrayList<>();
//
//
//
//        valuesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, displayArray);
//        friend_history.setAdapter(valuesAdapter);
//
//        setContentView(R.layout.activity_history);
        getPairings();

    }

    public void getPairings() {
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println(postSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}