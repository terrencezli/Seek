package edu.calpoly.tzli.seek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.Firebase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by terrence on 5/22/16.
 */
public class MeetFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    private LinearLayout rlLayout;

    protected Firebase myFirebaseRef;
    protected Button metFriends;
    private Button friendsPicker;
    private Button datePicker;
    private FacebookProfile personal;
    public static FacebookProfile friend;
    public static ImageView friendImage;
    public static Date chosenDate;

    public MeetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getContext());
        getPersonalProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rlLayout = (LinearLayout) inflater.inflate(R.layout.fragment_meet, container, false);

        return rlLayout;
    }

    public void initLayout() {

        friendsPicker = (Button) rlLayout.findViewById(R.id.friendsButton);
        friendsPicker.setOnClickListener(this);
        datePicker = (Button) rlLayout.findViewById(R.id.date_button);
        datePicker.setOnClickListener(this);

        metFriends = (Button) rlLayout.findViewById(R.id.met_button);
        metFriends.setOnClickListener(this);

        friendImage = (ImageView) rlLayout.findViewById(R.id.profile_picture);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (dpd != null) dpd.setOnDateSetListener(this);

        if (friend != null) {
            friendsPicker.setText(friend.getName());
            Glide
                .with(getActivity())
                .load(friend.getImageUrl())
                .placeholder(R.drawable.profile_picture) // can also be a drawable
                .into(friendImage);
        }

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    }

    protected void initAddKeyListeners() {
        Button dateButton = (Button) rlLayout.findViewById(R.id.date_button);

        // Show a datepicker when the dateButton is clicked
        assert dateButton != null;
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.met_button:
                if (!friendsPicker.getText().toString().equals("Pick a friend") && chosenDate != null) {
                    MeetUp m = new MeetUp(friend.getName(), friend.getId());
                    m.setDate(chosenDate.toString());

                    myFirebaseRef.push().setValue(m);

                    Toast.makeText(getContext(), "Sent userId " + friend.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please find a friend", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.friendsButton:
                Intent i = new Intent(getContext(), FriendSearchActivity.class);
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
                                HistoryFragment.personalName = name;
                                HistoryFragment.personalId = id;

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

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    android.app.DatePickerDialog.OnDateSetListener ondate = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            chosenDate = new Date(year, monthOfYear, dayOfMonth);

            datePicker.setText(chosenDate.toString().substring(0, 10));
        }
    };
}

