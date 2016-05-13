package edu.calpoly.tzli.seek;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;

import java.util.Calendar;

/**
 * Created by terrence on 4/18/16.
 */
public class MainActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener
{
    protected TextView timeTextView;
    protected TextView dateTextView;
//    protected AutoCompleteTextView textView;
    protected Toolbar toolbar;
    protected Firebase myFirebaseRef;
    protected Button inviteFriends;
    protected Button friendsHistory;
    private Button friendsPicker;
    public static String friend;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://boiling-heat-1137.firebaseIO.com");

        initLayout();
        initAddKeyListeners();

        getFriends();

    }

    public void initLayout() {
        setContentView(R.layout.activity_main);

        // Set toolbar
        toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        // Get a reference to the AutoCompleteTextView in the layout
//        textView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
//        textView.setInputType(InputType.TYPE_CLASS_TEXT);
//
//        // Get the string array
//        String[] names = getResources().getStringArray(R.array.names_array);
//        // Create the adapter and set it to the AutoCompleteTextView
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
//        textView.setAdapter(adapter);
//        textView.setThreshold(1);

        friendsPicker = (Button) findViewById(R.id.friendsButton);
        friendsPicker.setOnClickListener(this);
        timeTextView = (TextView) findViewById(R.id.time_textview);
        dateTextView = (TextView) findViewById(R.id.date_textview);

        inviteFriends = (Button) findViewById(R.id.invite_button);
        inviteFriends.setOnClickListener(this);

        friendsHistory = (Button) findViewById(R.id.history_button);
        friendsHistory.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);

        if (friend != null) friendsPicker.setText(friend);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        timeTextView.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
    }

    protected void initAddKeyListeners() {
//        textView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                String value = textView.getText().toString();
//
//                if (!(value.equals(null) || value.equals(""))) {
//                    if (event.getAction() != KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
//
//                        InputMethodManager imm = (InputMethodManager)
//                                getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
//
//                        return true;
//                    }
//                }
//
//                return false;
//            }
//        });

        Button timeButton = (Button) findViewById(R.id.time_button);
        Button dateButton = (Button) findViewById(R.id.date_button);
        // Show a timepicker when the timeButton is clicked
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invite_button:
//                String nameString = textView.getText().toString();
//                myFirebaseRef.child("userId").setValue(nameString + ", " + dateTextView.getText() + ", " + timeTextView.getText());
                Toast.makeText(MainActivity.this, "Sent userId", Toast.LENGTH_SHORT).show();
                break;
            case R.id.history_button:
                myFirebaseRef = new Firebase("https://boiling-heat-1137.firebaseIO.com");
                myFirebaseRef.child("userId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onCancelled(FirebaseError error) { }
                });
                break;
            case R.id.friendsButton:
                Intent i = new Intent(MainActivity.this, FriendsSearchActivity.class);
                startActivity(i);
                break;
        }
    }

    private void getFriends() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        GraphRequest.newMyFriendsRequest(accesstoken,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                        System.out.println("jsonArray: " + jsonArray);
                        System.out.println("GraphResponse: " + response);
                    }
                }).executeAsync();
    }
}
