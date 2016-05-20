package edu.calpoly.tzli.seek;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.params.Face;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by terrence on 4/18/16.
 */
public class MainActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener
{
    protected TextView dateTextView;
    protected Toolbar toolbar;
    protected Firebase myFirebaseRef;
    protected Button metFriends;
    protected Button friendsHistory;
    private Button friendsPicker;
    private FacebookProfile personal;
    public static String friend;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        getPersonalProfile();

    }

    public void initLayout() {
        setContentView(R.layout.activity_main);

        // Set toolbar
        toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        friendsPicker = (Button) findViewById(R.id.friendsButton);
        friendsPicker.setOnClickListener(this);
        dateTextView = (TextView) findViewById(R.id.date_textview);

        metFriends = (Button) findViewById(R.id.met_button);
        metFriends.setOnClickListener(this);

        friendsHistory = (Button) findViewById(R.id.history_button);
        friendsHistory.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(dpd != null) dpd.setOnDateSetListener(this);

        if (friend != null) friendsPicker.setText(friend);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
    }

    protected void initAddKeyListeners() {
        Button dateButton = (Button) findViewById(R.id.date_button);

        // Show a datepicker when the dateButton is clicked
        assert dateButton != null;
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
            case R.id.met_button:
                if (friendsPicker.getText().toString().length() > 0) {
                    String nameString = friendsPicker.getText().toString();
                    MetUpInformation m = new MetUpInformation(nameString);

                    myFirebaseRef.push().setValue(m.toString());

                    Toast.makeText(MainActivity.this, "Sent userId " + nameString, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Please find a friend", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.history_button:
                Intent j = new Intent(MainActivity.this, HistoryActivity.class);
                j.putExtra("personalName", personal.toString());
                j.putExtra("personalId", personal.getId());
                startActivity(j);
                break;
            case R.id.friendsButton:
                Intent i = new Intent(MainActivity.this, FriendsSearchActivity.class);
                startActivity(i);
                break;
        }
    }

    // get login user's profile
    public void getPersonalProfile() {
        GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {
                            String name = me.optString("name");
                            String id = me.optString("id");
                            try {
                                personal = new FacebookProfile(name, id);

                                myFirebaseRef = new Firebase("https://boiling-heat-1137.firebaseIO.com/" + personal.getId());

                                initLayout();
                                initAddKeyListeners();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
    }
}
