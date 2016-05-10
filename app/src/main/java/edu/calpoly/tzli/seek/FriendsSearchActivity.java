package edu.calpoly.tzli.seek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by micha on 5/9/2016.
 */
public class FriendsSearchActivity extends AppCompatActivity {

  ListView listView;
  ArrayAdapter<String> adapter;
  List<String> names;
  List<String> filteredNames;
  Toolbar toolbar;

  Button back;
  EditText search;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friends_picker);
    listView = (ListView) findViewById(R.id.list);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);


    search = (EditText) findViewById(R.id.search);
    search.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        filteredNames.clear();
        adapter.notifyDataSetChanged();

        if (s.length() == 0) {
          filteredNames.addAll(names);
          return;
        }
        String search = s.toString().replace(" ", "").toLowerCase();
        for (String name : names) {
          if (name.toLowerCase().contains(search)) {
            filteredNames.add(name);
          }
        }
        adapter.notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });


    names = Arrays.asList(getResources().getStringArray(R.array.names_array));
    filteredNames = new ArrayList<>(names);
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredNames);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.friend = filteredNames.get(position);
        onBackPressed();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // handle arrow click here
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }
}
