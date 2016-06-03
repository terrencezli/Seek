package com.aacfslo.tzli.seek;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by terrence on 5/22/16.
 */
public class MeetFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    private ScrollView rlLayout;

    protected static Firebase myFirebaseRef;
    protected Button meetFriends;
    private Button friendsPicker;
    private Button datePicker;
    protected FacebookProfile personal;
    public static FacebookProfile friend;
    public static ImageView friendImage;
    public static Date chosenDate;
    private static final String _PERSONAL = "PERSONAL_STATE";
    private static final String _DATE = "DATE_STATE";
    private static final String _FRAGMENT_STATE = "FRAGMENT_STATE";
    private Bundle savedState = null;

    public MeetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rlLayout = (ScrollView) inflater.inflate(R.layout.fragment_meet, container, false);

        initLayout();
        initAddKeyListeners();

        personal = ((TabActivity) getActivity()).getPersonal();
        myFirebaseRef = new Firebase(TabActivity.FIREBASE_URL + personal.getId());

        if(savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle(_FRAGMENT_STATE);
        }
        if(savedState != null) {
            if (saveState().getString(_DATE) != null) {
                datePicker.setText(savedState.getString(_DATE));
            }
        }
        savedState = null;

        return rlLayout;
    }

    public void initLayout() {

        friendsPicker = (Button) rlLayout.findViewById(R.id.friendsButton);
        friendsPicker.setOnClickListener(this);
        datePicker = (Button) rlLayout.findViewById(R.id.date_button);
        datePicker.setOnClickListener(this);

        meetFriends = (Button) rlLayout.findViewById(R.id.meet_button);
        meetFriends.setOnClickListener(this);

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
            case R.id.meet_button:
                if (!friendsPicker.getText().toString().equals("Pick a friend") && chosenDate != null) {
                    MeetUp m = new MeetUp(friend.getName(), friend.getId());
                    MeetUp fm = new MeetUp(personal.getName(), personal.getId());
                    m.setDate(chosenDate.toString());
                    fm.setDate(chosenDate.toString());

                    Firebase friendBase = new Firebase(TabActivity.FIREBASE_URL + friend.getId());

                    myFirebaseRef.push().setValue(m);
                    friendBase.push().setValue(fm);

                    Toast.makeText(getContext(), "Sent request to " + friend.getName(), Toast.LENGTH_SHORT).show();

                    // reset image
                    Drawable myDrawable = getResources().getDrawable(R.drawable.profile_picture);
                    friendImage.setImageDrawable(myDrawable);

                    friendsPicker.setText("Pick a friend");
                    datePicker.setText("Pick a date");
                    friend = null;
                    chosenDate = null;
                } else {
                    Toast.makeText(getContext(), "Please find a friend or enter a date", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.friendsButton:
                Intent i = new Intent(getContext(), FriendSearchActivity.class);
                startActivity(i);
                break;
        }
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
        date.show(getFragmentManager(), "Datepickerdialog");
    }

    android.app.DatePickerDialog.OnDateSetListener ondate = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            chosenDate = new Date(year, monthOfYear, dayOfMonth);

            datePicker.setText(chosenDate.toString().substring(0, 10));
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putSerializable(_PERSONAL, personal);

        if (chosenDate != null) {
            state.putString(_DATE, chosenDate.toString().substring(0, 10));
        }
        return state;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putBundle(_FRAGMENT_STATE, (savedState != null) ? savedState : saveState());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }
}

