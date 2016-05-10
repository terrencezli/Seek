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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myFirebaseRef = new Firebase("boiling-heat-1137.firebaseIO.com");
        friend_history = (ListView) findViewById(R.id.friend_history);
        displayArray = new ArrayList<>();
        valuesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,displayArray);
        friend_history.setAdapter(valuesAdapter);

        myFirebaseRef.child("userId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }
            @Override public void onCancelled(FirebaseError error) { }
        });

        setContentView(R.layout.activity_history);

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